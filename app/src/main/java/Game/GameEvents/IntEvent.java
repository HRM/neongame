package Game.GameEvents;

import Engine.Events.Event;

public class IntEvent implements Event {
    private final int val;

    public IntEvent(int val) {
        this.val=val;
    }

    public int getVal() {
        return val;
    }

    @Override
    public String getEventName() {
        return "IntEvent";
    }
}
