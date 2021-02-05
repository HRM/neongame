package Engine.Android;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import Engine.Events.EventType.PointerEvent;
import Engine.InitScript;
import ExtGl2Lib.GL2F;

public class EngineSurfaceView extends GLSurfaceView{

    EngineRender mRend=null;
    private float downsampling=0;

    public EngineSurfaceView(Context context, InitScript iS) {
        super(context);
        Engine.Context.acContext =context;
        Engine.Context.viewContext=this;
        getHolder().setFormat(PixelFormat.RGB_888);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        downsampling=(float)Math.ceil((width/1080.0/0.5))*0.5f;
        Log.i("downsmapling","width: "+width+" rwidth: "+(width/downsampling+" down: "+downsampling));
        setEGLContextClientVersion(2);
        mRend= new EngineRender(iS,this);
        setRenderer(mRend);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public boolean onTouchEvent(MotionEvent e) {
        switch(e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mRend.SendPointerEvent(new PointerEvent(e.getPointerId(e.getActionIndex()), e.getX(e.getActionIndex())/downsampling-GL2F.getDefWidth()/2, GL2F.getDefHeight()/2-e.getY(e.getActionIndex())/downsampling, e.getPressure(e.getActionIndex()),
                        (short) 0
                ));
                break;
            case MotionEvent.ACTION_UP:
                mRend.SendPointerEvent(new PointerEvent(e.getPointerId(e.getActionIndex()), e.getX(e.getActionIndex())/downsampling-GL2F.getDefWidth()/2, GL2F.getDefHeight()/2-e.getY(e.getActionIndex())/downsampling, e.getPressure(e.getActionIndex()),
                        (short) 1
                ));
                break;
            case MotionEvent.ACTION_MOVE:
                mRend.SendPointerEvent(new PointerEvent(e.getPointerId(e.getActionIndex()), e.getX(e.getActionIndex())/downsampling-GL2F.getDefWidth()/2, GL2F.getDefHeight()/2-e.getY(e.getActionIndex())/downsampling, e.getPressure(e.getActionIndex()),
                        (short) 2
                ));
                break;
            case MotionEvent.ACTION_CANCEL:
                mRend.SendPointerEvent(new PointerEvent(e.getPointerId(e.getActionIndex()), e.getX(e.getActionIndex())/downsampling-GL2F.getDefWidth()/2, GL2F.getDefHeight()/2-e.getY(e.getActionIndex())/downsampling, e.getPressure(e.getActionIndex()),
                        (short) 1
                ));
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setSystemUiVisibility( SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_FULLSCREEN|SYSTEM_UI_FLAG_HIDE_NAVIGATION|SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION );
        getHolder().setFixedSize((int)(getWidth()/downsampling),(int)(getHeight()/downsampling));
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Engine.Context.eventTable!=null)
        Engine.Context.eventTable.callEvent("renderPause",null);
        Engine.Context.eventTable=null;
        Engine.Context.timeTable=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSystemUiVisibility( SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_FULLSCREEN|SYSTEM_UI_FLAG_HIDE_NAVIGATION|SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION );
    }



    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged( gainFocus, direction, previouslyFocusedRect );
        if(gainFocus){
            setSystemUiVisibility( SYSTEM_UI_FLAG_IMMERSIVE_STICKY|SYSTEM_UI_FLAG_FULLSCREEN|SYSTEM_UI_FLAG_HIDE_NAVIGATION|SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION );
        }
    }
}
