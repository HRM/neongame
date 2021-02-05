package Engine.Events.EventType;

import Engine.Events.Event;

public class PointerEvent implements Event{
    private final int pid;
    private final float x;
    private final float y;
    private final float pressure;
    private final short aType;

    public PointerEvent(int pid, float x, float y, float pressure, short aType) {
        this.pid = pid;
        this.x = x;
        this.y = y;
        this.pressure=pressure;
        this.aType = aType;
    }

    public int getPid() {
        return pid;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getPressure() {
        return pressure;
    }

    public int getAType() {
        return aType;
    }

    @Override
    public String getEventName() {
        return "PointerEvent";
    }
}
