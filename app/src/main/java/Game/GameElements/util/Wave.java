package Game.GameElements.util;

import Engine.Animation.AnimFloat;
import Engine.Animation.AnimFloatMD;
import Engine.Element;
import ExtGl2Lib.GL2F;
import Game.GameEvents.FloatMEvent;

public class Wave extends Element {
    private AnimFloatMD size;
    private AnimFloat opacity;
    private float[] pos;
    private float[] color;
    float speed=0.2f;

    private float[] dummy=new float[4];

    public Wave(float[] pos,float[] size,float[] color){
        addEventHandler("drawLight",(e)->onDraw());
        this.pos=pos.clone();
        this.color=color.clone();
        this.size=getFloatAnimatorMD(new float[]{0,0},speed);
        opacity=getFloatAnimator(0.0f,speed/2);
        this.size.enableBez( 0,0 );
        this.opacity.enableBez( 0,0 );
        this.size.animateTo(new float[]{size[0]/2,size[1]/2},()->disconnect());
        opacity.animateTo(0.2f,()->opacity.animateTo( 0 ));
    }
    public void onDraw(){
        GL2F.setSfe( false );
        GL2F.setFill(true);
        GL2F.setFade(pke()*10);
        GL2F.setThickness(0.1f);
        GL2F.setCol(GL2F.COOP.Multiply(color,opacity.getVal(),dummy));
        GL2F.drawRect( size.getVal(),pos,0 );
        /*
        GL2F.drawLine( new float[]{pos[0]+size.getVal()[0],pos[1]+size.getVal()[1]},new float[]{pos[0]+size.getVal()[0],pos[1]-size.getVal()[1]} );
        GL2F.drawLine( new float[]{pos[0]+size.getVal()[0],pos[1]-size.getVal()[1]},new float[]{pos[0]-size.getVal()[0],pos[1]-size.getVal()[1]} );
        GL2F.drawLine( new float[]{pos[0]-size.getVal()[0],pos[1]-size.getVal()[1]},new float[]{pos[0]-size.getVal()[0],pos[1]+size.getVal()[1]} );
        GL2F.drawLine( new float[]{pos[0]-size.getVal()[0],pos[1]+size.getVal()[1]},new float[]{pos[0]+size.getVal()[0],pos[1]+size.getVal()[1]} );
        */
    }
}
