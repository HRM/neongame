package Engine.Animation;

import Engine.Util.Interpolation;

public class AnimFloat implements Animator{
    private float v1;
    private float v2;

    private float bT1;
    private float bT2;

    private float state;
    private float cur;

    private boolean bezEn=false;

    private float speed;

    AnimCallbackHandle mHand=null;

    public AnimFloat(float val,float speed){
        v2=val;
        state=1.0f;
        cur=v2;
        this.speed=speed;
    }


    public void animateTo(float val){
        v1=cur;
        v2=val;
        state=0.0f;
    }

    public void animateTo(float val, AnimCallbackHandle hand){
        animateTo( val );
        mHand=hand;
    }

    public void animateTo(float val, float speed){
        v1=cur;
        v2=val;
        state=0.0f;
        setSpeed( speed );
    }

    public void animateTo(float val, float speed, AnimCallbackHandle hand){
        animateTo( val,speed );
        mHand=hand;
    }

    public void enableBez(float b1,float b2){
        bT1=b1;
        bT2=b2;
        bezEn=true;
    }

    public void disableBez(){
        bezEn=false;
    }

    public void setSpeed(float s){
        if(s>0.1f) {
            this.speed = s;
        }else{
            this.speed=0.1f;
        }
    }

    public void refresh(long delta){
        if(state<1.0f) {
            state += (float) delta / 1000f / speed;
            if (state > 1.0f) {
                state = 1.0f;
            }
            if (bezEn) {
                float fac = Interpolation.BezierInterpolation( 0f, 1f, bT1, bT2, state );
                cur = Interpolation.LinearInterpolation( v1, v2, fac );
            } else {
                cur = Interpolation.LinearInterpolation( v1, v2, state );
            }
            if (state == 1.0f && mHand != null) {
                mHand.handle();
                mHand = null;
            }
        }
    }

    public float getVal(){
        return cur;
    }

    public void setVal(float val) {
        v2 = val;
        state = 1.0f;
        cur = v2;
    }

    public void addVal(float v){
        v1+=v;v2+=v;
        if(state<1.0f) refresh(0);
        else cur+=v;
    }
}
