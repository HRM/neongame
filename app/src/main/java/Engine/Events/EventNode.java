package Engine.Events;

public class EventNode {
    private EventHandler eh;
    private final EventHandlerList mCotainer;
    private boolean removable=false;

    EventNode(EventHandler eh, EventHandlerList container){
        this.eh=eh;
        mCotainer=container;
    }

    public boolean isRemovable() {
        return removable;
    }

    public EventHandler getEventHandler() {
        return eh;
    }
    public void remove(){
        removable=true; mCotainer.markForRemove();
    }

}
