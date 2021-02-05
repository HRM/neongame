package Engine.Android;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Engine.Context;
import Engine.Element;
import Engine.Events.EventPool;
import Engine.Events.EventType.PointerEvent;
import Engine.Events.EventType.TickEvent;
import Engine.InitScript;
import Engine.Timing.TimingPool;
import ExtGl2Lib.GL2F;
import Game.EngieParts.AdEngine.AdHandler;

public class EngineRender implements GLSurfaceView.Renderer{
    private static boolean initOnce=true;
    private boolean reinit=false;

    private long prev=System.currentTimeMillis();
    private long now=System.currentTimeMillis();
    private EngineSurfaceView mContext;

    private InitScript mInit=null;

    public EngineRender(InitScript mInit,EngineSurfaceView context) {
        mContext=context;
        this.mInit = mInit;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        reinit=true;
    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GL2F.SetDefaultScreenSize( width, height );
        GL2F.SetDefaultTarget();
        if(!reinit)return;
        Context.eventTable = new EventPool();
        Context.timeTable = new TimingPool();
        GL2F.SetDefaultScreenSize( width, height );
        Element.SetUnits(width,height);
        GL2F.Init();

        mInit.init();

        if(initOnce){
            initOnce=false;
            mInit.initOnce();
        }

        Context.eventTable.callEvent("start",null);
        reinit=false;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        prev = now;
        now = System.currentTimeMillis();

        Context.timeTable.refresh( now - prev );
        Context.eventTable.execFarCall( "pointerEvent" );
        AdHandler.processAfterAd();
        Context.eventTable.callEvent( "tick", new TickEvent( now - prev ) );
        Context.eventTable.callEvent( "afterTick", null );
        Context.eventTable.callEvent( "draw", null );
        Context.eventTable.processRemoves();
        Context.timeTable.processRemoves();
    }

    public void SendPointerEvent(PointerEvent PEvent){
        Context.eventTable.farCallEvent("pointerEvent",PEvent);
    }


}
