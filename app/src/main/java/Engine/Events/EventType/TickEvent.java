package Engine.Events.EventType;

import Engine.Events.Event;

public class TickEvent implements Event{
    private final long delta;

    public TickEvent(long delta) {
        this.delta = delta;
    }

    public long getDelta() {
        return delta;
    }

    @Override
    public String getEventName() {
        return "RefreshEvent";
    }
}
