package Engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import Engine.Animation.AnimFloat;
import Engine.Animation.AnimFloatMD;
import Engine.Animation.Animator;
import Engine.Events.Event;
import Engine.Events.EventHandler;
import Engine.Events.EventNode;
import Engine.Events.EventType.TickEvent;
import Engine.Exeptions.OutOfContextExeption;
import Engine.Timing.TimingHandler;
import Engine.Timing.TimingNode;

public class Element {

    private static float pke=0;

    public static float pke(){
        return pke;
    }

    public static void SetUnits(int width,int height){
        pke=Math.min(width,height)/128f;
    }

    private ConcurrentLinkedQueue<EventNode> eventSubs = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<Animator> animators = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<TimingNode> timings = new ConcurrentLinkedQueue();
    private EventNode AnimTickHander = null;

    protected Element() {
        if (Context.eventTable == null || Context.timeTable == null) {
            throw (new OutOfContextExeption());
        }
        addEventHandler("disconnectAll",(e)->{
            disconnect();
        });
    }

    protected final TimingNode setTimeout(TimingHandler th, float time) {
        TimingNode tn = Context.timeTable.SetTimeout(th, time);
        tn.addTimingHandler(() -> timings.remove(tn) );
        timings.add(tn);
        return tn;
    }

    protected final TimingNode setInterval(TimingHandler th, float time) {
        TimingNode tn = Context.timeTable.SetInterval(th, time);
        timings.add(tn);
        return tn;
    }

    protected final void stopAllTimer() {
        for (TimingNode tn : timings) {
            tn.stop();
        }
    }

    protected final void resumeAllTimer() {
        for (TimingNode tn : timings) {
            tn.resume();
        }
    }

    protected final void removeTimer(TimingNode tn) {
        tn.remove();
        timings.remove(tn);
    }

    private void removeAllTimer() {
        for (TimingNode tn : timings) {
            tn.remove();
        }
        timings.clear();
    }

    protected final void addEventHandler(String eventName, EventHandler handler) {

        EventNode en = Context.eventTable.addEventHandler(eventName, handler);

        this.eventSubs.add(en);
    }


    protected final void removeAllEventHandler() {
        for (EventNode en : eventSubs) {
            en.remove();
        }
        eventSubs.clear();
    }

    protected final boolean callEvent(String EventName, Event e) {
        return Context.eventTable.callEvent(EventName, e);
    }

    protected final AnimFloat getFloatAnimator(float val, float speed) {
        if (animators.isEmpty()) {
            AnimTickHander = Context.eventTable.addEventHandler("tick", (e) -> {
                TickEvent te = (TickEvent) e;
                for (Animator a : animators) {
                    a.refresh(te.getDelta());
                }
            });
        }
        AnimFloat ret = new AnimFloat(val, speed);
        animators.add(ret);
        return ret;
    }

    protected final AnimFloatMD getFloatAnimatorMD(float[] val, float speed) {
        if (animators.isEmpty()) {
            AnimTickHander = Context.eventTable.addEventHandler("tick", (e) -> {
                TickEvent te = (TickEvent) e;
                for (Animator a : animators) {
                    a.refresh(te.getDelta());
                }
            });
        }
        AnimFloatMD ret = new AnimFloatMD(val, speed);
        animators.add(ret);
        return ret;
    }

    protected final boolean removeAnimator(Animator a) {
        boolean ret = animators.remove(a);
        if (animators.isEmpty()) {
            AnimTickHander.remove();
            AnimTickHander = null;
        }
        return ret;
    }

    protected final void removeAllAnimator() {
        if (!animators.isEmpty()) {
            animators.clear();
            AnimTickHander.remove();
            AnimTickHander = null;
        }
    }

    protected void disconnect() {
        removeAllEventHandler();
        removeAllTimer();
        removeAllAnimator();
    }

    private LinkedList<Integer> classStaticSuffixerInt(Class place, String prefix, int suffixStart,int suffixEnd, int suffixLeap){
        LinkedList<Integer> ret=new LinkedList<>(  );
        for(int i=suffixStart;i<=suffixEnd;i+=suffixLeap){
            try {
                Field f = place.getField( prefix + i );
                if (f.getType() == int.class) {
                    ret.add( new Integer(  f.getInt( null ) ));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return ret;
    }


    public static Bitmap fB(int id){
        return BitmapFactory.decodeResource(Context.viewContext.getResources(), id);
    }
    protected Bitmap[] fB(Class place, String prefix, int suffixStart,int suffixEnd, int suffixLeap){
        LinkedList<Integer> ints=classStaticSuffixerInt(place,prefix,suffixStart,suffixEnd,suffixLeap);
        Bitmap[] ret=new Bitmap[ints.size()];
        int i=0;
        for(Integer n:ints){
            ret[i]=fB(n);
            i++;
        }
        return ret;
    }
    protected Bitmap[] fB(Class place, String prefix, int suffixStart,int suffixEnd){
        return fB(place,prefix,suffixStart,suffixEnd,1);
    }
    protected Bitmap[] fB(Class place, String prefix,int suffixEnd){
        return fB(place,prefix,0,suffixEnd,1);
    }


    public static InputStream fS(int id){
        return Context.viewContext.getResources().openRawResource( id );
    }
    protected InputStream[] fS(Class place, String prefix, int suffixStart,int suffixEnd, int suffixLeap){
        LinkedList<Integer> ints=classStaticSuffixerInt(place,prefix,suffixStart,suffixEnd,suffixLeap);
        InputStream[] ret=new InputStream[ints.size()];
        int i=0;
        for(Integer n:ints){
            ret[i]=fS(n);
            i++;
        }
        return ret;
    }
    protected InputStream[] fS(Class place, String prefix, int suffixStart,int suffixEnd){
        return fS(place,prefix,suffixStart,suffixEnd,1);
    }
    protected InputStream[] fS(Class place, String prefix,int suffixEnd){
        return fS(place,prefix,0,suffixEnd,1);
    }

}
