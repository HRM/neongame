package Game.GameElements.LineGameParts;

        import Engine.Animation.AnimFloat;
        import Engine.Animation.AnimFloatMD;
        import Engine.Element;
        import Engine.Timing.TimingNode;
        import ExtGl2Lib.GL2F;
        import Game.Options;

public class LineGameNode extends Element {
    private static float sq2=(float)Math.sqrt( 2 );
    private int val;
    private AnimFloatMD pos;
    private AnimFloatMD col;
    private AnimFloat size;
    private AnimFloat sizeMod;
    private AnimFloat colLightness;
    private AnimFloat visibility;
    private AnimFloat glowMod=getFloatAnimator( 1,0.2f );
    private int extra=0;

    private float[] fadePos=null;

    private TimingNode flashing=null;

    private void drawShape(){
        switch (Options.nodeShape) {
            case 1:   GL2F.drawRect( new float[]{size.getVal() * sizeMod.getVal(), size.getVal() * sizeMod.getVal()}, pos.getVal(), 0 ); break;
            case 2:   GL2F.drawCircle( pos.getVal(),size.getVal() * sizeMod.getVal() );break;
            case 3:   GL2F.drawRect( new float[]{size.getVal() * sizeMod.getVal()/2*sq2, size.getVal() * sizeMod.getVal()/2*sq2}, pos.getVal(), (float)Math.PI/4 ); break;
        }
    }

    public LineGameNode(int val, float[] pos, float size) {
        this.val = val;
        this.pos = getFloatAnimatorMD( pos,0.2f );
        this.col = getFloatAnimatorMD( GL2F.COOP.GetColorByHue( Options.nodeColors[val],1f,new float[4] ),0.2f );
        this.size = getFloatAnimator( size,0.2f );
        this.sizeMod=getFloatAnimator( 1,0.1f );
        this.colLightness=getFloatAnimator( 0,0.1f );
        this.visibility=getFloatAnimator( 0,0.2f );
        visibility.animateTo( 1 );

        addEventHandler( "drawL2",(e)->onDraw() );
        addEventHandler( "drawLight",(e)->onDrawLight() );
    }

    public int getVal(){
        return val;
    }

    public AnimFloatMD getPos() {
        return pos;
    }

    public AnimFloatMD getCol() {
        return col;
    }

    public AnimFloat getSize() {
        return size;
    }
    public void setVal(int val) {
        this.val = val;
    }

    public AnimFloat getSizeMod() {
        return sizeMod;
    }

    public int getExtra(){
        return extra;
    }

    public void setExtra(int in){
        extra=in;
    }

    public void fade(){
        if(flashing!=null) {
            removeTimer( flashing );
            flashing=null;
            colLightness.animateTo( 0 );
        }
        this.visibility.animateTo( 0,0.2f,()->disconnect() );
        this.sizeMod.animateTo( 0,0.2f );
    }

    public void flash(float rate){
        flashing=setInterval( ()->{colLightness.animateTo( 1,rate/2f ,()->colLightness.animateTo( 0));},rate*2f);
    }

    public void fadeTo(float[] pos){
        this.col.animateTo( GL2F.COOP.V4ONES,0.1f );
        this.visibility.enableBez( 0,0 );
        this.visibility.animateTo( 0,0.2f ,()->disconnect());
        this.sizeMod.animateTo( 0.0f,0.2f );
        this.pos.animateTo( pos );
        this.glowMod.animateTo( 3f );
    }

    public void bubbleUp(){
        sizeMod.enableBez( 0,1 );
        sizeMod.animateTo( 1.1f,()->{sizeMod.disableBez();} );
    }

    public void bubbleDown(){
        sizeMod.enableBez( 0,1 );
        sizeMod.animateTo( 1,()->{sizeMod.disableBez();} );
    }

    public void popToCol(int val){
        this.val=val;
        this.col.animateTo( GL2F.COOP.V4ONES,()->this.col.animateTo( GL2F.COOP.GetColorByHue( (float)(360/6*Math.log(val)),1f,new float[4] ) ) );
        this.sizeMod.animateTo( 1.2f,()->sizeMod.animateTo( 1 ) );
    }

    private void onDraw(){
        GL2F.setSqf( true );
        GL2F.setSfe( false );
        GL2F.setFade( 0.1f );
        GL2F.setFill( true );
        GL2F.setCol( GL2F.COOP.ModOpa( col.getVal(),visibility.getVal()*0.1f,new float[4]) );

        drawShape();

        GL2F.setSfe( false );
        GL2F.setFill( false );

        GL2F.setThickness(pke()/4 );
        GL2F.setFade( 0.1f );

        if(visibility.getVal()!=1.0f)
            GL2F.setCol( GL2F.COOP.ModOpa(col.getVal(),visibility.getVal(),new float[4]) );
        else
            GL2F.setCol(col.getVal());

        drawShape();

        GL2F.setThickness(0.1f );
        GL2F.setFade( pke() );

        if(visibility.getVal()!=1.0f)
            GL2F.setCol( GL2F.COOP.VGenOneOpa(visibility.getVal(),new float[4]  ) );
        else
            GL2F.setCol( GL2F.COOP.V4ONES);

        drawShape();


        switch(extra){
            case 1:
                GL2F.setThickness(pke()/4 );
                GL2F.setFade( 0.1f );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.ModOpa(col.getVal(),visibility.getVal(),new float[4]) );
                else
                    GL2F.setCol(col.getVal());
                GL2F.drawLine( new float[]{pos.getVal()[0],pos.getVal()[1]-size.getVal()*sizeMod.getVal()},new float[]{pos.getVal()[0],pos.getVal()[1]+size.getVal()*sizeMod.getVal()} );
                GL2F.setThickness(0.1f );
                GL2F.setFade( pke() );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.VGenOneOpa(visibility.getVal(),new float[4]  ) );
                else
                    GL2F.setCol( GL2F.COOP.V4ONES);
                GL2F.drawLine( new float[]{pos.getVal()[0],pos.getVal()[1]-size.getVal()*sizeMod.getVal()},new float[]{pos.getVal()[0],pos.getVal()[1]+size.getVal()*sizeMod.getVal()} );
            break;
            case 2:
                GL2F.setThickness(pke()/4 );
                GL2F.setFade( 0.1f );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.ModOpa(col.getVal(),visibility.getVal(),new float[4]) );
                else
                    GL2F.setCol(col.getVal());
                GL2F.drawLine( new float[]{pos.getVal()[0]-size.getVal()*sizeMod.getVal(),pos.getVal()[1]},new float[]{pos.getVal()[0]+size.getVal()*sizeMod.getVal(),pos.getVal()[1]} );
                GL2F.setThickness(0.1f );
                GL2F.setFade( pke() );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.VGenOneOpa(visibility.getVal(),new float[4]  ) );
                else
                    GL2F.setCol( GL2F.COOP.V4ONES);
                GL2F.drawLine( new float[]{pos.getVal()[0]-size.getVal()*sizeMod.getVal(),pos.getVal()[1]},new float[]{pos.getVal()[0]+size.getVal()*sizeMod.getVal(),pos.getVal()[1]} );
                break;
            case 3:
                GL2F.setThickness(pke()/4 );
                GL2F.setFade( 0.1f );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.ModOpa(col.getVal(),visibility.getVal(),new float[4]) );
                else
                    GL2F.setCol(col.getVal());
                GL2F.drawRect(new float[]{size.getVal()*sizeMod.getVal()/2f,size.getVal()*sizeMod.getVal()/2f},pos.getVal(),0);
                GL2F.setThickness(0.1f );
                GL2F.setFade( pke() );
                if(visibility.getVal()!=1.0f)
                    GL2F.setCol( GL2F.COOP.VGenOneOpa(visibility.getVal(),new float[4]  ) );
                else
                    GL2F.setCol( GL2F.COOP.V4ONES);
                GL2F.drawRect(new float[]{size.getVal()*sizeMod.getVal()/2f,size.getVal()*sizeMod.getVal()/2f},pos.getVal(),0);
                break;
            default:
        }

        GL2F.setFade( size.getVal()/2f );
        GL2F.setFill( true );

        if(colLightness.getVal()!=0) {
            GL2F.setCol( GL2F.COOP.VGenOneOpa( colLightness.getVal() * visibility.getVal(), new float[4] ) );
            drawShape();
        }

        /*String temp=""+val;
        float tsm=1f/temp.length();
        if(temp.length()>1)tsm*=1.8f;

        FontSet.neonFont.drawString( ""+val,new float[]{pos.getVal()[0]-size.getVal()/8*tsm,pos.getVal()[1]},size.getVal()*2f*sizeMod.getVal()*tsm,0,0 );*/
    }
    private void onDrawLight(){

        GL2F.setSqf( true );
        GL2F.setFill( false );
        GL2F.setSfe( true );
        GL2F.setThickness( pke()/4 );
        float pcalc=size.getVal()*glowMod.getVal()*2f;
        GL2F.setFade(pcalc);
        GL2F.setSFade(pcalc/4f);
        float[] fl1=new float[4];
        GL2F.setSCol( GL2F.COOP.Multiply( col.getVal(), visibility.getVal(), fl1 ) );
        GL2F.setCol( GL2F.COOP.Multiply( col.getVal(), visibility.getVal()*0.3f, fl1 ) );

        drawShape();


        /*GL2F.setSfe( false );
        GL2F.setFade( 0.1f );
        GL2F.setFill( true );
        GL2F.setCol( GL2F.COOP.Multiply( col.getVal(),visibility.getVal()*0.1f,fl1) );
        GL2F.drawRect(new float[]{size.getVal()*sizeMod.getVal(),size.getVal()*sizeMod.getVal()},pos.getVal(),0);*/
    }
    public void remove(){
        disconnect();
    }
}
