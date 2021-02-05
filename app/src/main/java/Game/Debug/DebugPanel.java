package Game.Debug;

import Engine.Context;
import Engine.Element;
import Engine.Events.EventType.TickEvent;
import ExtGl2Lib.GL2F;
import Game.EngieParts.FontEngine.Font;
import tams.libbuild.R;

public class DebugPanel extends Element {
    float ms=1;
    Font mFont;
    String build;
    float size=7;
    int refreshDump=60;

    public DebugPanel(String build, float size){
        this.size=size;
        this.build=build;
        mFont=new Font( fS( R.raw.arial_sm),fB(R.drawable.arial_sm_0) );
        addEventHandler( "tick",(e)->{
            ms=((refreshDump-1)*ms+((TickEvent)e).getDelta()/1000f)/refreshDump;
        } );
        addEventHandler( "debugDraw",(e)->{
            GL2F.setCol( new float[]{0,1,0,1} );
            GL2F.setDirectMode( false );
            mFont.drawString( this.build,new float[]{-GL2F.getDefWidth()/2f,GL2F.getDefHeight()/2f},size*pke(),1,1 );
            mFont.drawString( "FPS: "+String.format("%.2f", 1/ms),new float[]{-GL2F.getDefWidth()/2f,GL2F.getDefHeight()/2f-size*pke()},size*pke(),1,1 );
            mFont.drawString( "MS:"+String.format("%.2f", ms*1000),new float[]{-GL2F.getDefWidth()/2f,GL2F.getDefHeight()/2f-2*size*pke()},size*pke(),1,1 );
            GL2F.drawOutTexs();
            GL2F.setDirectMode( true );

        } );
    }
}
