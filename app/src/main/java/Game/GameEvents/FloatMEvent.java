package Game.GameEvents;

import Engine.Events.Event;

public class FloatMEvent implements Event{
    private final float val;

    public FloatMEvent(float val) {
        this.val = val;
    }

    public float getVal() {
        return val;
    }

    @Override
    public String getEventName() {
        return "FloatMEvent";
    }
}
