package Engine.Timing;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimingPool {
    private ConcurrentLinkedQueue<TimingNode> timingList=new ConcurrentLinkedQueue<>();

    public TimingNode SetTimeout(TimingHandler th, float time){
        TimingNode tn=new TimingNode( th,time,false );
        timingList.add( tn );
        return tn;
    }
    public TimingNode SetInterval(TimingHandler th, float time){
        TimingNode tn= new TimingNode( th,time,true );
        timingList.add( tn );
        return tn;
    }

    public boolean RemoveTimer(TimingNode tn){
        return timingList.remove(tn);
    }

    public void RemoveAllTimer(){
        timingList.clear();
    }

    public boolean processRemoves(){
        boolean removed =false;
        for(Iterator<TimingNode> iter = timingList.iterator(); iter.hasNext();){
            TimingNode tn=iter.next();
            if(tn.isRemoveable()){
                iter.remove();
                removed=true;
            }
        }
        return removed;
    }

    public void refresh(long delta){
        float d=(float)delta/1000.0f;
        for(TimingNode tn:timingList){
            tn.refresh(d);
        }
    }

    public int getTimingCount(){
        return timingList.size();
    }
}
