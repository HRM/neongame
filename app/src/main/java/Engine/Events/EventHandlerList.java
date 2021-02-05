package Engine.Events;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import Engine.Events.EventType.PointerEvent;
import Engine.Timing.TimingNode;

public class EventHandlerList {
    private ConcurrentLinkedQueue<EventNode> mHanlerList= new ConcurrentLinkedQueue<EventNode>();
    private ConcurrentLinkedQueue<Event> mEvents = new ConcurrentLinkedQueue<Event>();
    private EventPool mContainer;
    public EventHandlerList(EventPool cont){
        mContainer=cont;
    }

    public EventNode addHandler(EventHandler eh){
        EventNode en=new EventNode( eh, this  );
            mHanlerList.add( en );
            return en;
    }

    public void markForRemove(){
        mContainer.removeMark(this);
    }

    public boolean removeHandler(EventNode en){
        return mHanlerList.remove(en);
    }
    public void removeAllHandler(){
        mHanlerList.clear();
    }
    public boolean processRemoves(){
        boolean removed =false;
        for(Iterator<EventNode> iter = mHanlerList.iterator(); iter.hasNext();){
            EventNode en=iter.next();
            if(en.isRemovable()){
                iter.remove();
                removed=true;
            }
        }
        return removed;
    }
    public int getHandlerCount(){
        return mHanlerList.size();
    }
    public void call(Event e){
        for (EventNode h : mHanlerList) {
            h.getEventHandler().handle(e);
        }
    }
    public boolean farCall(Event e){
        return mEvents.add( e );
    }

    public boolean execFarCall(){
        if(mEvents.size()>0) {
            LinkedList<Event> tList=new LinkedList<>(  );
            while(mEvents.size()>0){
                tList.addLast( mEvents.poll() );
            }
            for (EventNode h : mHanlerList) {
                for(Event e:tList) {
                    h.getEventHandler().handle( e );
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    public boolean dropFarCall(){
        boolean any=mEvents.size()>0;
        mEvents.clear();
        return any;
    }

    public int getFarCallQSize(){
        return mEvents.size();
    }
}
