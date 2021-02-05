package Engine.Events;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class EventPool {
    private Hashtable<String,EventHandlerList> eventTable= new Hashtable<String,EventHandlerList>(100);
    private LinkedList<EventHandlerList> removeMarked=new LinkedList<>();

    public void removeMark(EventHandlerList evl){
        removeMarked.add(evl);
    }

    public EventNode addEventHandler(String eventName,EventHandler eh){
        EventHandlerList hl=eventTable.get( eventName );
        EventNode en;
        if(hl==null){
            hl=new EventHandlerList(this);
            eventTable.put( eventName, hl);;
        }
        return hl.addHandler(eh);
    }

    public boolean removeEventHandler(String eventName,EventNode en){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.removeHandler(en);
        }
        else{
            return false;
        }
    }

    public boolean removeAllHandler(String eventName){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            eventTable.remove( eventName );
            return true;
        }
        else{
            return false;
        }
    }

    public void removeAllEvent(){
        eventTable.clear();
    }

    public boolean processRemoves(){
        boolean removed=false;
        for(EventHandlerList ehl:removeMarked){
            removed|=ehl.processRemoves();
        }
        removeMarked.clear();
        return removed;
    }


    public boolean callEvent(String eventName,Event e){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            hl.call( e );
            return true;
        }
        else{
            return false;
        }
    }
    public boolean farCallEvent(String eventName,Event e){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.farCall( e );
        }
        else{
            return false;
        }
    }

    public boolean execFarCall(String eventName){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.execFarCall();
        }
        else{
            return false;
        }
    }
    @Deprecated
    public void execAllFarCall(){
        Enumeration<String> keys=eventTable.keys();
        while(keys.hasMoreElements()){
            eventTable.get( keys.nextElement() ).execFarCall();
        }
    }

    public boolean dropFarCall(String eventName){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.dropFarCall();
        }
        else{
            return false;
        }
    }
    @Deprecated
    public void dropAllFarCall(){
        Enumeration<String> keys=eventTable.keys();
        while(keys.hasMoreElements()){
            eventTable.get( keys.nextElement() ).dropFarCall();
        }
    }

    public int getFarCallQSize(String eventName){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.getFarCallQSize();
        }
        else{
            return -1;
        }
    }

    public int getHandlerCount(String eventName){
        EventHandlerList hl=eventTable.get( eventName );
        if(hl!=null){
            return hl.getHandlerCount();
        }
        else{
            return -1;
        }
    }

    public Set<String> getEventNames(){
        return eventTable.keySet();
    }

}
