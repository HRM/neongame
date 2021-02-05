package Game.Directors;

import java.util.LinkedList;

import Engine.Animation.AnimFloat;
import Engine.Animation.AnimFloatMD;
import Engine.Context;
import Engine.Element;
import Engine.Events.EventType.PointerEvent;
import Engine.Timing.TimingHandler;
import Engine.Timing.TimingNode;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.EngieParts.UiEngine.UiElements.Dummy;
import Game.EngieParts.UiEngine.UiElements.IconButton;
import Game.EngieParts.UiEngine.UiElements.Text;
import Game.GameElements.GUI.FontSet;
import Game.GameElements.LineGameParts.LineGameNode;
import Game.GameElements.Save.NodeData;
import Game.GameElements.Save.SaveData;
import Game.GameEvents.IntEvent;
import Game.PlayerData;

import static android.content.Context.MODE_PRIVATE;

public class LineGameStarter extends Element {
    private static int levelSizeH=10;
    private static int levelSizeW=6;
    private static float startFillRate =2.5f;
    private static float pointTimeMultipler=0.007f;
    private static float endFillRate=0.6f;
    private static int spawnDiffTypes =3;
    private static float extraChanceEnd=0.5f;
    private static float extraChanceStart=-0.2f;

    private static float spaceFromTop=2*pke();
    private static float spaceFromBottom=20*pke();

    private VerticalRList mainList=new VerticalRList( GL2F.getDefWidth(),spaceFromBottom-2*pke(),0,-GL2F.getDefHeight()/2f+spaceFromBottom/2f );
    private Text score=new Text(15*pke(),"0");
    private Text scoreAdded=new Text(15*pke(),"0");

    private float ns;
    private float pcx;
    private float pcy;
    private float x0;
    private float y0;

    private LineGameNode[][] ground=null;

    private float[] curPos;

    private AnimFloat fillCounter;
    private AnimFloat pointSizeMultipler;

    private int rfx;
    private int rfy;

    private int points=0;
    private boolean gameLost=false;

    private TimingNode spawnTimer;

    private LinkedList<float[]> lineList=new LinkedList<>(  );

    private AnimFloat visibility;

    private int bonusCount=0;

    private int activeColor =0;
    private int colorCounts[]=new int[]{8,8,8};


    private int[][] initialVals=new int[][]{
            new int[]{ 0, 0, 0, 1, 1, 1}  ,
            new int[]{ 0, 2, 2, 2, 2, 1}  ,
            new int[]{ 0, 2, 2, 2, 2, 1}  ,
            new int[]{ 0, 0, 0, 1, 1, 1}  ,
    };
    private int[][] pointerPaths=new int[][]{
            new int[]{0,2, 0,2 ,0,1 ,0,0 ,1,0 ,2,0 ,3,0 ,3,1 ,3,2 ,3,2},
            new int[]{0,3 ,0,3 ,0,4 ,0,5 ,1,5 ,2,5 ,3,5 ,3,4 ,3,3 ,3,3},
            new int[]{1,1, 1,1 ,1,2 ,1,3 ,1,4 ,0,4 ,0,3 ,0,2 ,0,1 ,0,1}
    };
    private float[] pointerSizeMods=new float[]{1.5f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1.5f};
    private float[] pointerVis=new float[]{0f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0f};

    private int curMFrame=0;

    private void nextFrame(){
        curMFrame++;
        curMFrame%=pointerPaths[activeColor].length/2;
        pointerPos.animateTo(convertIdToCord(pointerPaths[activeColor][curMFrame*2+1],pointerPaths[activeColor][curMFrame*2]));
        pointerSizeMod.animateTo(pointerSizeMods[curMFrame]);
        pointerVisMod.animateTo(pointerVis[curMFrame]);
    }
    private void resetFrame(){
        pointerPos.setVal(convertIdToCord(pointerPaths[activeColor][1],pointerPaths[activeColor][0]));
        pointerSizeMod.setVal(pointerSizeMods[0]);
        pointerVisMod.setVal(pointerVis[0]);

        curMFrame=1;

        pointerPos.animateTo(convertIdToCord(pointerPaths[activeColor][curMFrame*2+1],pointerPaths[activeColor][curMFrame*2]));
        pointerSizeMod.animateTo(pointerSizeMods[curMFrame]);
        pointerVisMod.animateTo(pointerVis[curMFrame]);
    }

    private AnimFloatMD pointerPos=getFloatAnimatorMD(new float[]{pointerPaths[0][0],pointerPaths[0][1]},0.2f);
    private AnimFloat pointerSizeMod=getFloatAnimator(1f,0.2f);
    private AnimFloat pointerVisMod=getFloatAnimator(0.0f,0.2f);

    private TimingNode pMover;

    float getSpawnTimeByPoints(int points){
        return Math.max( startFillRate -(float)Math.log(points*pointTimeMultipler+1),endFillRate);
    }
    float[] convertIdToCord(int i,int j){
        return new float[]{x0+pcx*i,y0+pcy*j};
    }

    public LineGameStarter(){
        callEvent( "fadeBouncer",null );
        if(GL2F.getDefWidth()/levelSizeW*levelSizeH<GL2F.getDefHeight()-spaceFromTop-spaceFromBottom){
            pcx= GL2F.getDefWidth()/levelSizeW;
            pcy= GL2F.getDefWidth()/levelSizeW;
        }
        else{
            pcx= (GL2F.getDefHeight()-spaceFromTop-spaceFromBottom)/levelSizeH;
            pcy= (GL2F.getDefHeight()-spaceFromTop-spaceFromBottom)/levelSizeH;
        }
        x0=-pcx*levelSizeW/2f+pcx/2f;
        y0=-pcy*levelSizeH/2f+pcy/2f+(spaceFromBottom-spaceFromTop)/2f;

        ns=pcx*0.4f;

        visibility=getFloatAnimator( 0,0.2f );
        visibility.animateTo( 1 );

        ground=new LineGameNode[levelSizeW][];
        for(int i=0;i<levelSizeW;i++){
            ground[i]=new LineGameNode[levelSizeH];
            for(int j=0;j<initialVals.length;j++){
                if(initialVals[initialVals.length-j-1][i]==-1)continue;
                ground[i][j]=new LineGameNode( initialVals[initialVals.length-j-1][i],new float[]{x0+pcx*i,y0+pcy*j},ns );
                ground[i][j].setExtra( 0 );
            }
        }

        mainList.addElement( new IconButton( 13*pke(),13*pke(),0x08b,()->{
            processGameLost();
            fadeAll();
            visibility.animateTo( 0,0.2f,()->{
                new Menu();
                callEvent( "showBouncer",null );
                mainList.remove();
                removeTimer(pMover);
                pointerVisMod.animateTo(0);
                disconnect();
            } );
        } ) );
        mainList.addElement( scoreAdded );
        mainList.addElement( score );
        mainList.addElement( new Dummy() );
        mainList.addElement( new Text(7*pke(), "Lvl: "+PlayerData.pointsCollected) );


        scoreAdded.getVisibility().setVal( 0 );

        addEventHandler( "pointerEvent",(e)->onPointerEvent( (PointerEvent)e ) );
        addEventHandler( "drawL3",(e)->onDraw() );
        addEventHandler( "drawBL3",(e)->onDrawBack() );
        addEventHandler( "drawLight",(e)->onDrawLight() );
        fillCounter=getFloatAnimator( 0, startFillRate );
        pointSizeMultipler=score.getSizeMod();

        pMover=setInterval(()->{nextFrame();},0.2f);
        addEventHandler("drawL4",(e)->{
            if(lineList.size()!=0)return;
            GL2F.setCol(GL2F.COOP.VGenOneOpa(pointerVisMod.getVal(),new float[4]));
            FontSet.icons.drawCharAbsVu((char)0x0f7,new float[]{pointerPos.getVal()[0],pointerPos.getVal()[1]-6*pke()},pointerSizeMod.getVal()*15*pke());
        });
    }


    private LineGameNode[] checkFull(){
        LinkedList<LineGameNode> atTop=new LinkedList<>( );
        for(int i=0;i<levelSizeW;++i){
            if(ground[i][levelSizeH-1]!=null){
                atTop.push( ground[i][levelSizeH-1] );
            }
        }
        return atTop.toArray( new LineGameNode[]{});
    }

    private void fadeAll(){
        for(int j=0;j<levelSizeH;++j)for(int i=0;i<levelSizeW;++i){
            if(ground[i][j]!=null){
                ground[i][j].fade();
            }
        }
    }

    private void processGameLost(){
        gameLost=true;
        while(lineList.size()>0){
            float[] pos=lineList.pollFirst();
            pos[0] = Math.round((pos[0] - x0) / pcx);
            pos[1] = Math.round((pos[1] - y0) / pcy);
            ground[(int) pos[0]][(int) pos[1]].bubbleDown();
        }
        fillCounter.setVal( 0 );
        spawnTimer=null;
    }

    private int processExtra(int x,int y,int type){
        int extraPoint=0;
        switch (type){
            case 1:
                if(x<levelSizeW){
                    for(int j=0;j<levelSizeH;++j){
                        if(ground[x][j]!=null){
                            ground[x][j].fadeTo(new float[]{x0+pcx*x,y0+pcy*y});
                            ground[x][j]=null;
                            extraPoint++;
                        }
                    }
                }
                //new HWave( new float[]{x0+pcx*x,y0+pcy*(levelSizeH-1)/2f},new float[]{pcx,levelSizeH*pcy} );
                break;
            case 2:
                if(y<levelSizeH){
                    for(int i=0;i<levelSizeW;++i){
                        if(ground[i][y]!=null){
                            ground[i][y].fadeTo(new float[]{x0+pcx*x,y0+pcy*y});
                            ground[i][y]=null;
                            extraPoint++;
                        }
                    }
                }
                //new HWave( new float[]{x0+pcx*(levelSizeW-1)/2f,y0+pcy*y},new float[]{levelSizeW*pcx,pcy});
                break;
            case 3:
                int area=Math.min(levelSizeW,levelSizeH)/4;
                for(int i=Math.max(x-area,0);i<=Math.min( levelSizeW-1,x+area );++i){
                    for(int j=Math.max(y-area,0);j<=Math.min( levelSizeH-1,y+area );++j){
                        if(ground[i][j]!=null){
                            ground[i][j].fadeTo(new float[]{x0+pcx*x,y0+pcy*y});
                            ground[i][j]=null;
                            extraPoint++;
                        }
                    }
                }
                //new HWave( new float[]{x0+(Math.min( levelSizeW-1,x+area )+Math.max(x-area,0))*pcx/2f,y0+(Math.min( levelSizeH-1,y+area )+Math.max(y-area,0))*pcy/2f},
                //       new float[]{(Math.min( levelSizeW-1,x+area )-Math.max(x-area,0)+1)*pcx,(Math.min( levelSizeH-1,y+area )-Math.max(y-area,0)+1)*pcy});
                break;
            default:
        }
        return extraPoint;
    }

    private void defragGround(){
        for(int i=0;i<levelSizeW;i++){
            int mc=0;
            for(int j=0;j<levelSizeH;j++){
                if(ground[i][j]==null)mc++;
                else{
                    if(mc>0) {
                        ground[i][j - mc] = ground[i][j];
                        ground[i][j - mc].getPos().animateTo( new float[]{x0 + pcx * i, y0 + pcy * (j - mc)} );
                        ground[i][j] = null;
                    }
                }
            }
        }
    }
    private void fillGround(){
        for(int i=0;i<levelSizeW;i++){
            for(int j=0;j<levelSizeH;j++){
                if(ground[i][j]==null) {
                    int val=(int)(Math.random()* spawnDiffTypes);
                    ground[i][j] = new LineGameNode( val, new float[]{x0 + pcx * i, GL2F.getDefHeight()/2f}, ns );
                    ground[i][j].getPos().animateTo( new float[]{x0 + pcx * i, y0 + pcy * j} );
                    ground[i][j].getCol().animateTo( GL2F.COOP.ModOpa( ground[i][j].getCol().getVal(),1,new float[4] ) );
                }
            }
        }
    }
    private void fillOneLine(){
        float extchance=0;
        for(int i=0;i<levelSizeW;i++){
            for(int j=0;j<levelSizeH;j++){
                if(ground[i][j]==null) {
                    int val=(int)(Math.random()* spawnDiffTypes);
                    int extra=Math.random()<extchance?(int)(Math.random()*3+1):0;
                    ground[i][j] = new LineGameNode( val, new float[]{x0 + pcx * i, GL2F.getDefHeight()/2f}, ns );
                    ground[i][j].getPos().animateTo( new float[]{x0 + pcx * i, y0 + pcy * j} );
                    ground[i][j].getCol().animateTo( GL2F.COOP.ModOpa( ground[i][j].getCol().getVal(),1,new float[4] ) );
                    ground[i][j].setExtra( extra );
                    break;
                }
            }
        }
    }

    public void onPointerEvent(PointerEvent pe){

        if(gameLost)return;
        if(pe.getAType()==0){
            curPos=new float[]{pe.getX(),pe.getY()};
            rfx=Math.round((pe.getX()-x0)/pcx);
            rfy=Math.round((pe.getY()-y0)/pcy);
            if(!((rfx<0||rfx>=levelSizeW)||(rfy<0|rfy>=levelSizeH))){
                if(ground[rfx][rfy]!=null){
                    if(ground[rfx][rfy].getVal()!=activeColor)return;
                    boolean contains=false;
                    for(float[] c:lineList){
                        if(c[0]==rfx*pcx+x0&&c[1]==rfy*pcy+y0){
                            contains=true;
                            break;
                        }
                    }
                    if(!contains){
                        callEvent( "playNoteFromChord",new IntEvent( lineList.size() ) );
                        lineList.addFirst( new float[]{rfx*pcx+x0,rfy*pcy+y0});
                        ground[rfx][rfy].bubbleUp();
                    }
                }
            }
        }
        if(pe.getAType()==1){
            if(lineList.size()>=colorCounts[activeColor] ) {
                int pointsPrev=points;
                points+=lineList.size();
                bonusCount+=lineList.size();
                LinkedList<int[]> extras=new LinkedList<>(  );
                int extraFound=0;
                while (lineList.size() > 0) {
                    float[] pos = lineList.pollLast();
                    pos[0] = Math.round((pos[0] - x0) / pcx);
                    pos[1] = Math.round((pos[1] - y0) / pcy);
                    if(ground[(int) pos[0]][(int) pos[1]]!=null) {
                        if (ground[(int) pos[0]][(int) pos[1]].getExtra() != 0) {
                            extras.add( new int[]{(int) pos[0], (int) pos[1], ground[(int) pos[0]][(int) pos[1]].getExtra()} );
                        }
                        ground[(int) pos[0]][(int) pos[1]].fade();
                        ground[(int) pos[0]][(int) pos[1]] = null;
                    }
                }
                while (extras.size()>0){
                    int[] ext=extras.pop();
                    extraFound+=processExtra(ext[0],ext[1],ext[2]);
                }
                points+=extraFound;
                if(extraFound>0){
                    callEvent( "playPupSound",null );
                }
                defragGround();
                pointSizeMultipler.animateTo( 1.3f,()->pointSizeMultipler.animateTo( 1 ) );
                score.setText( ""+points );
                scoreAdded.setText( "+"+(points-pointsPrev) );
                scoreAdded.getVisibility().animateTo( 1,0.25f,()->scoreAdded.getVisibility().animateTo( 0 ) );
                scoreAdded.getxPosMod().setVal( -10*pke() );
                scoreAdded.getxPosMod().animateTo( 0,0.5f );
                callEvent( "newChord",null );
                activeColor++;
            }
            else {
                while (lineList.size() > 0) {
                    float[] pos = lineList.pollFirst();
                    pos[0] = Math.round((pos[0] - x0) / pcx);
                    pos[1] = Math.round((pos[1] - y0) / pcy);
                    ground[(int) pos[0]][(int) pos[1]].bubbleDown();
                }
            }
            if(activeColor>2){
                new Menu();
                callEvent( "showBouncer",null );
                mainList.remove();
                removeTimer(pMover);
                pointerVisMod.animateTo(0);
                disconnect();
            }else {
                resetFrame();
                removeTimer(pMover);
                pMover = setInterval(() -> {
                    nextFrame();
                }, 0.2f);
            }
        }
        if(pe.getAType()==2){
            if(lineList.size()==0)return;
            curPos[0]=pe.getX();
            curPos[1]=pe.getY();
            int x1=Math.round((pe.getX()-x0)/pcx);
            int y1=Math.round((pe.getY()-y0)/pcy);
            float cx=x1*pcx+x0;
            float cy=y1*pcy+y0;
            float dx=pe.getX()-cx;
            float dy=pe.getY()-cy;
            float d2x=Math.abs(cx-lineList.peekFirst()[0]);
            float d2y=Math.abs(cy-lineList.peekFirst()[1]);

            if(Math.sqrt(dx*dx+dy*dy)<ns*2f&&d2x<=pcx+0.01f&&d2y<=pcy+0.01f&&(d2x==0^d2y==0)){
                if((x1<0||x1>=levelSizeW)||(y1<0||y1>=levelSizeH))return;
                else{
                    if(ground[x1][y1]==null)return;
                    if(ground[x1][y1].getVal()!=ground[rfx][rfy].getVal())return;
                }

                if(lineList.peekFirst()[0]==cx&&lineList.peekFirst()[1]==cy)return;

                if(lineList.size()>1) {
                    float[] prev = lineList.get( 1 );
                    if(prev[0]==cx&&prev[1]==cy){
                        float pos[]=lineList.pollFirst();
                        pos[0] = Math.round((pos[0] - x0) / pcx);
                        pos[1] = Math.round((pos[1] - y0) / pcy);
                        ground[(int) pos[0]][(int) pos[1]].bubbleDown();
                        callEvent( "playNoteFromChord",new IntEvent( lineList.size() ) );
                        return;
                    }
                }

                boolean contains=false;
                for(float[] c:lineList){
                    if(c[0]==cx&&c[1]==cy){
                        contains=true;
                        break;
                    }
                }

                if(!contains&&ground[x1][y1].getVal()==activeColor){
                    callEvent( "playNoteFromChord",new IntEvent( lineList.size() ) );
                    lineList.addFirst( new float[]{cx,cy} );
                    ground[x1][y1].bubbleUp();
                }
            }
        }
    }

    private void onDraw(){

        GL2F.setCol( GL2F.COOP.VGenOneOpa( visibility.getVal(),new float[4] ) );

        if(lineList.size()>0) {
            GL2F.setSfe( false );
            GL2F.setFade( 0.1f );
            GL2F.setThickness( pke()/4f );
            float[] prev = null;
            for (float[] cord : lineList) {
                if (prev != null)
                    GL2F.drawLine( prev, cord );
                prev = cord;
            }
            GL2F.drawLine( lineList.peekFirst(), curPos );
        }
        GL2F.setThickness( pke()/4f );
        GL2F.setFade( 0.1f );

        float xFrom=x0-pcx/2f;
        float xTo=xFrom+pcx*levelSizeW*fillCounter.getVal();
        float yPos=y0-pcy/2f+levelSizeH*pcy;
        GL2F.drawLine( new float[]{xFrom,yPos},new float[]{xTo,yPos} );
    }

    private void onDrawLight(){
        if(lineList.size()>0){
            GL2F.setThickness(pke()/4f );
            GL2F.setSfe( true );
            GL2F.setFade(20*pke());
            GL2F.setSFade(2*pke());
            float[] fl1=new float[4];
            GL2F.setSCol( GL2F.COOP.Multiply( GL2F.COOP.V4ONES,visibility.getVal(),fl1) );
            GL2F.setCol( GL2F.COOP.Multiply( GL2F.COOP.V4ONES,0.8f*visibility.getVal(),fl1) );

            float[] prev=null;
            for(float[] cord:lineList){
                if(prev!=null) {
                    GL2F.drawLine( prev,cord );
                }
                prev=cord;
            }

            GL2F.drawLine( lineList.peekFirst(),curPos );

            GL2F.setSfe( false );

        }
    }

    private void onDrawBack(){

        GL2F.setThickness( pke()/4f );
        GL2F.setFade( 0.1f );
        GL2F.setCol( GL2F.COOP.VGenOneOpa( visibility.getVal(),new float[4] ) );

        float xFrom=x0-pcx/2f;
        float xTo=xFrom+pcx*levelSizeW;
        for(int i=0;i<=levelSizeH;i++){
            float yPos=y0-pcy/2f+i*pcy;
            GL2F.drawLine( new float[]{xFrom,yPos},new float[]{xTo,yPos} );
        }

        float yFrom=y0-pcy/2f;
        float yTo=yFrom+pcy*levelSizeH;
        for(int i=0;i<=levelSizeW;i++){
            float xPos=x0-pcx/2f+i*pcx;
            GL2F.drawLine( new float[]{xPos,yFrom},new float[]{xPos,yTo} );
        }
    }

}
