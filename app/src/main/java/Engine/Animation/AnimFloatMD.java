package Engine.Animation;

import Engine.Util.Interpolation;

public class AnimFloatMD implements Animator{
    private float[] v1;
    private float[] v2;

    private float bT1;
    private float bT2;

    private float state;
    private float[] cur;

    private boolean bezEn=false;

    private float speed;

    AnimCallbackHandle mHand=null;

    public AnimFloatMD(float[] val,float speed){
        v2=val.clone();
        state=1.0f;
        cur=v2.clone();
        v1=v2.clone();
        this.speed=speed;
    }


    public void animateTo(float[] val){
        System.arraycopy(cur,0,v1,0,cur.length);
        System.arraycopy(val,0,v2,0,val.length);
        state=0.0f;
        state=0.0f;
    }

    public void animateTo(float[] val, AnimCallbackHandle hand){
        animateTo( val );
        mHand=hand;
    }

    public void animateTo(float[] val, float speed){
        System.arraycopy(cur,0,v1,0,cur.length);
        System.arraycopy(val,0,v2,0,val.length);
        state=0.0f;
        setSpeed( speed );
    }

    public void animateTo(float[] val, float speed, AnimCallbackHandle hand){
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
                Interpolation.LinearInterpolationMD( v1, v2, fac,cur );
            } else {
                Interpolation.LinearInterpolationMD( v1, v2, state,cur );
            }
            if (state == 1.0f && mHand != null) {
                mHand.handle();
                mHand = null;
            }
        }
    }

    public void setVal(float[] val) {
        System.arraycopy(val,0,v2,0,v2.length);
        state = 1.0f;
        System.arraycopy(v2,0,cur,0,cur.length);
    }

    public float[] getVal(){
        return cur;
    }

    public void addVal(float[] v){
        for(int i=0;i<v2.length;++i){
            if(v1!=null)v1[i]+=v[i];
            v2[i]+=v[i];
            if(state==1.0f)cur[i]+=v[i];
        }
        if(state<1.0f)refresh(0);
    }
}