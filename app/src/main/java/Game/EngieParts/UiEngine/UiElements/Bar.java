package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.UiBlock;

public class Bar extends UiBlock {
    private AnimFloat fullness;
    private AnimFloat visibility=getFloatAnimator( 1,0.2f );

    public Bar(float thickness, float length, float fullness){
        this.RealSize[0]=length;
        this.RealSize[1]=thickness;
        this.fullness=getFloatAnimator( fullness,0.2f );

        addEventHandler( "drawL2",(e)->{
            GL2F.setFill( false );
            GL2F.setThickness( 0.25f*pke() );
            GL2F.setFade( 0.1f );
            GL2F.setSfe( false );
            GL2F.setCol( GL2F.COOP.VGenOneOpa( parentVisibility*visibility.getVal(),new float[4] ) );
            GL2F.drawRect( new float[]{size[0]/2f,size[1]/2f},pos,0 );

            GL2F.setFill( true );
            GL2F.setThickness( 0.1f );

            GL2F.drawRect( new float[]{size[0]/2f*this.fullness.getVal(),size[1]/2f},new float[]{pos[0]+size[0]/2f*(-1f+this.fullness.getVal()),pos[1]},0 );
        } );
    }

    public AnimFloat getFullness() {
        return fullness;
    }

    @Override
    public void remove() {
        visibility.animateTo( 0,()->disconnect() );
    }
}
