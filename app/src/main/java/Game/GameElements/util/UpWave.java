package Game.GameElements.util;

import Engine.Animation.AnimFloat;
import Engine.Element;
import ExtGl2Lib.GL2F;

public class UpWave extends Element {
    private AnimFloat prog=getFloatAnimator( 0,0.8f );
    private AnimFloat vis=getFloatAnimator( 0,0.5f );

    public UpWave(){
        prog.animateTo( 1,()->disconnect() );
        vis.animateTo( 1,()->vis.animateTo( 0 ) );
        addEventHandler( "drawLight",(e)->onDrawLight() );
    }

    private void onDrawLight(){
        GL2F.setSqf( true );
        GL2F.setThickness( 0.1f );
        GL2F.setCol( GL2F.COOP.VGenStr( vis.getVal(),new float[4] ) );
        GL2F.setFade( 50*pke() );
        GL2F.setSfe( false );
        GL2F.setFill( true );
        GL2F.drawLine( new float[]{-GL2F.getDefWidth()/2f,-GL2F.getDefHeight()/2f+GL2F.getDefHeight()*prog.getVal()},new float[]{GL2F.getDefWidth()/2f,-GL2F.getDefHeight()/2f+GL2F.getDefHeight()*prog.getVal()} );
    }
}
