package Game.GameElements.util;

import Engine.Animation.AnimFloat;
import Engine.Element;
import ExtGl2Lib.GL2F;

public class HWave extends Element {
    private AnimFloat fadeFac=getFloatAnimator( 1,0.5f );
    private static float fade=2*pke();
    private static  float fadeTo=5*pke();
    private float[] size=new float[]{0,0};
    private float[] pos;

    public HWave(float[] p,float[] s){
        size[0]=s[0]/2f;
        size[1]=s[1]/2f;

        fadeFac.enableBez( 0,1 );

        pos=p.clone();

        fadeFac.animateTo( 0,()->disconnect() );

        addEventHandler( "drawLight",(e)->onDrawLight() );
    }

    private void onDrawLight(){
        float fadeClip=fade +fadeTo-fadeTo*fadeFac.getVal();
        GL2F.setSfe( false );
        GL2F.setFill( true );
        GL2F.setFade(fadeClip );
        GL2F.setThickness( 0.1f );
        GL2F.setCol( GL2F.COOP.VGenStr( fadeFac.getVal()*0.5f,new float[4] ) );
        GL2F.setSqf( false );
        GL2F.drawRect( new float[]{Math.max( 0,size[0]-fadeClip/2f ),Math.max( 0,size[1]-fadeClip/2f )},pos,0 );
    }
}
