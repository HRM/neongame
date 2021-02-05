package Engine;

import android.view.SurfaceView;

import Engine.Events.EventPool;
import Engine.Timing.TimingPool;

public class Context {
    public static EventPool eventTable = null;
    public static TimingPool timeTable = null;
    public static android.content.Context acContext = null;
    public static SurfaceView viewContext=null;
}
