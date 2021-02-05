package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import Engine.Element;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.Positionable;
import Game.EngieParts.UiEngine.UiBlock;
import Game.GameElements.Achive.NodeOptionPack;

public class GameNode extends UiBlock {

    private AnimFloat visibility=getFloatAnimator( 0,0.2f );

    private int val;
    private NodeOptionPack nop;
    private float[] color=new float[]{1f,1f,1f,1f};

    public GameNode(NodeOptionPack nop,int val,float size) {
        this.nop=nop;
        this.val=val;
        GL2F.COOP.GetColorByHue( nop.nodeColors[val],1,color );
        visibility.animateTo( 1 );

        RealSize[0]=size;
        RealSize[1]=size;

        addEventHandler( "drawL1",(e)->onDraw() );
        addEventHandler( "drawLight",(e)->onDrawLight() );
    }

    private static float sq2=(float)Math.sqrt( 2 );

    private void drawShape(){
        switch (nop.nodeShape) {
            case 1:   GL2F.drawRect( new float[]{size[0] * fitSizeMod, size[0] * fitSizeMod}, pos, 0 ); break;
            case 2:   GL2F.drawCircle( pos,size[0] * fitSizeMod );break;
            case 3:   GL2F.drawRect( new float[]{size[0] * fitSizeMod/2*sq2, size[0] * fitSizeMod/2*sq2}, pos, (float)Math.PI/4 ); break;
        }
    }

    private void onDraw(){
        if(parentVisibility==0)return;
        GL2F.setSqf( false );
        GL2F.setSfe( false );
        GL2F.setFade( 0.1f );
        GL2F.setFill( true );
        GL2F.setCol( GL2F.COOP.ModOpa(color,visibility.getVal()*parentVisibility*0.1f,new float[4]) );

        drawShape();

        GL2F.setFill( false );

        GL2F.setThickness(pke()/4 );
        GL2F.setFade( 0.1f );

        if(visibility.getVal()*parentVisibility!=1.0f)
            GL2F.setCol( GL2F.COOP.ModOpa(color,visibility.getVal()*parentVisibility,new float[4]) );
        else
            GL2F.setCol(color);

        drawShape();

        GL2F.setThickness(0.1f );
        GL2F.setFade( pke() );

        if(visibility.getVal()*parentVisibility!=1.0f)
            GL2F.setCol( GL2F.COOP.VGenOneOpa(visibility.getVal()*parentVisibility,new float[4]  ) );
        else
            GL2F.setCol( GL2F.COOP.V4ONES);

        drawShape();

    }
    private void onDrawLight(){

        if(parentVisibility==0)return;
        GL2F.setSqf( true );
        GL2F.setFill( false );
        GL2F.setSfe( true );
        GL2F.setThickness( pke()/4 );
        GL2F.setFade(size[0]*2f);
        GL2F.setSFade(size[0]/2f);
        float[] fl1=new float[4];
        GL2F.setSCol( GL2F.COOP.Multiply( color, visibility.getVal()*parentVisibility, fl1 ) );
        GL2F.setCol( GL2F.COOP.Multiply( color, visibility.getVal()*0.3f*parentVisibility, fl1 ) );

        drawShape();
    }

    @Override
    public void remove() {
        visibility.animateTo( 0,()->disconnect() );
    }
}
