package Engine.Timing;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TimingNode {
    private float mTime;
    private boolean mRep;
    private LinkedList<TimingHandler> mTh=new LinkedList();
    private float mLocalTime=0.0f;
    private boolean stopped=false;
    private boolean removeable=false;
    TimingNode(TimingHandler th,float time,boolean rep){
        mTh.add(th);
        mTime=time;
        mRep=rep;
    }
    public void refresh(float delta){
        if(stopped)return;
        mLocalTime+=delta;
        if(mLocalTime>=mTime) {
            mLocalTime -= mTime;
            for (TimingHandler th : mTh) {
                th.handle();
            }
            if (!mRep) {
                removeable=true;
            }
        }
    }

    public void addTimingHandler(TimingHandler th){
        mTh.add(th);
    }

    public LinkedList<TimingHandler> getHandlerList(){
        return mTh;
    }

    public boolean isRemoveable(){
        return removeable;
    }

    public void stop(){
        stopped=true;
    }
    public void resume(){
        stopped=false;
    }
    public void remove(){
        removeable=true;
    }

    public float getTimeing() {
        return mTime;
    }
    public void setTiming(float t){
        mTime=t;
    }
}
