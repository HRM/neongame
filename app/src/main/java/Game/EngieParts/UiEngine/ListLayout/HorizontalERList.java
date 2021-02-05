package Game.EngieParts.UiEngine.ListLayout;

import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.SynchronousQueue;

import Engine.Animation.AnimFloat;
import Engine.Element;
import Engine.Events.EventType.PointerEvent;
import Engine.Events.EventType.TickEvent;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.Positionable;

public class HorizontalERList extends Element implements Positionable,UIList {
    private LinkedList<Positionable> children=new LinkedList<>(  );
    private float nodeSize =1f;
    private float nodeGap=1f;
    private float offset=0f;
    private float parentVisibility=1f;
    private float friction=4f;
    private float fadingDist=0.2f;
    private boolean flashing=false;

    private float prevPY=0;
    private float ssp=0;
    private boolean sliding=false;
    private long pTime=0;

    private float AvHSpace;
    private float AvWSpace;
    private float posX;
    private float posY;

    private float upperBoundry;
    private float lowerBoundry;

    private AnimFloat flasher=getFloatAnimator( 0,0.4f );

    private void updateBound(){
        upperBoundry=nodeSize*fadingDist-nodeGap;
        lowerBoundry=-nodeSize+2f+AvHSpace-children.size()*(nodeGap+ nodeSize);
    }

    public HorizontalERList(float nodeSize, float nodeGap) {
        this.nodeSize = nodeSize;
        this.nodeGap = nodeGap;

        updateBound();
        offset=upperBoundry;

        addEventHandler( "pointerEvent",(e)->{
            PointerEvent pe=(PointerEvent)e;
            if(Math.abs( pe.getX()-posX )<AvWSpace/2f&&Math.abs( pe.getY()-posY )<AvHSpace/2f) {
                if(pe.getAType()==2||pe.getAType()==0){
                    if(pTime!=0) {
                        sliding=false;
                        float sT = (float) (System.currentTimeMillis() - pTime) / 1000f;
                        if(sT>0) {
                            changeOffset( offset + prevPY - pe.getY() );
                            ssp = (prevPY - pe.getY()) / sT;
                            prevPY = pe.getY();
                            pTime = System.currentTimeMillis();
                        }
                    }else{
                        pTime=System.currentTimeMillis();
                        prevPY=pe.getY();
                }
                }else{
                    pTime=0;
                    sliding=true;
                }
            }else{
                pTime=0;
                sliding=true;
            }
        } );

        addEventHandler( "tick",(e)->{
            if(sliding) {
                TickEvent te = (TickEvent) e;
                float tf = (float) te.getDelta() / 1000f;
                ssp = ssp/(float)Math.pow(friction,tf);
                changeOffset( offset + ssp*tf );
                sliding = sliding && Math.abs( ssp )>0.01;
            }
        } );

        addEventHandler( "drawL1", (e)->{
            if(flasher.getVal()==0f)return;
            GL2F.setSqf( false );
            GL2F.setSfe( false );
            GL2F.setFill( true );
            GL2F.setThickness(pke() );
            GL2F.setFade( 0.1f );

            GL2F.setCol( GL2F.COOP.VGenOneOpa(flasher.getVal(),new float[4]));
            GL2F.drawLine( new float[]{posX-AvWSpace/4f,posY-AvHSpace/2f+3*pke()},new float[]{posX+AvWSpace/4f,posY-AvHSpace/2f+3*pke()} );
        } );
        addEventHandler( "drawLight", (e)->{
            if(flasher.getVal()==0f)return;
            GL2F.setSqf( false );
            GL2F.setSfe( false );
            GL2F.setFill( true );
            GL2F.setThickness(pke() );
            GL2F.setFade( 2*pke() );

            GL2F.setCol( GL2F.COOP.VGenStr(flasher.getVal(),new float[4]));
            GL2F.drawLine( new float[]{posX-AvWSpace/4f,posY-AvHSpace/2f+3*pke()},new float[]{posX+AvWSpace/4f,posY-AvHSpace/2f+3*pke()} );
        } );
    }

    public HorizontalERList(float nodeSize, float nodeGap, float width,float height,float posx,float posy) {
        this(nodeSize,nodeGap);
        setAvWSpace( width );
        setAvHSpace( height );
        setPosX( posx );
        setPosY( posy );
        updateBound();
    }

    public void addElement(Positionable p){
        p.setAvWSpace( AvWSpace );
        p.setAvHSpace( nodeSize );
        p.setPosX( posX );
        float sp=-(children.size()*(nodeGap+ nodeSize) +offset+nodeGap+ nodeSize /2f)+posY +AvHSpace/2f;
        p.setPosY( sp );
        float d=AvHSpace/2f-Math.abs(sp-posY);
        float  vis=Math.max(0f,Math.min(1f,(d- nodeSize /2f)/ (nodeSize*fadingDist) ));
        p.setVisibility( vis*parentVisibility );
        children.add( p );
        updateBound();

        if(posY-AvHSpace/2f>sp&&flashing){
            flasher.animateTo( 1,()->flasher.animateTo( 0 ) );
        }
    }
    public void removeElement(Positionable p){
        children.remove( p );
        changeOffset( offset );
        lowerBoundry+=nodeGap+ nodeSize;
    }

    public void removeFirst(){
        children.removeFirst();
        changeOffset( offset );
        lowerBoundry+=nodeGap+ nodeSize;
    }

    public void removeLast(){
        children.removeFirst();
        changeOffset( offset );
        lowerBoundry+=nodeGap+ nodeSize;
    }

    public void changeOffset(float o){
        if(upperBoundry<lowerBoundry)offset=upperBoundry;
        else if(o>upperBoundry){offset=upperBoundry;}
        else if(o<lowerBoundry){offset=lowerBoundry;}
        else
        offset=o;
        int i=0;
        for(Positionable p:children){
            float sp=-(i*(nodeGap+ nodeSize) +offset+nodeGap+ nodeSize /2f)+posY +AvHSpace/2f;
            p.setPosY( sp );
            float d=AvHSpace/2f-Math.abs(sp-posY);
            float  vis=Math.max(0f,Math.min(1f,(d- nodeSize /2f)/ (nodeSize*fadingDist) ));
            p.setVisibility( vis*parentVisibility );
            i++;
        }
    }

    public float getOffset(){
        return offset;
    }

    public void setFlashing(boolean flashing) {
        this.flashing = flashing;
    }

    @Override
    public void setAvWSpace(float avWSpace) {
        AvWSpace=avWSpace;
        for(Positionable p:children){
            p.setAvWSpace( avWSpace );
        }
    }

    @Override
    public void setPosY(float posY) {
        this.posY=posY;
        for(Positionable p:children){
            p.setPosY( posY );
        }
    }

    @Override
    public void setAvHSpace(float avHSpace) {
        AvHSpace=avHSpace;
        updateBound();
        changeOffset( offset );
    }

    @Override
    public void setPosX(float posX) {
        this.posX=posX;
        changeOffset( offset );
    }

    @Override
    public void setVisibility(float v) {
        parentVisibility=v;
    }

    @Override
    public void remove() {
        for(Positionable p:children){
            p.remove();
        }
    }
}