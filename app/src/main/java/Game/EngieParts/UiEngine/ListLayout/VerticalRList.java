package Game.EngieParts.UiEngine.ListLayout;

import java.util.LinkedList;
import java.util.ListIterator;

import Engine.Element;
import Game.EngieParts.UiEngine.Positionable;

public class VerticalRList extends Element implements Positionable,UIList {
    private LinkedList<Positionable> children=new LinkedList<>(  );
    private LinkedList<Float> weights=new LinkedList<>(  );
    private float AvHSpace;
    private float posY;
    private float AvWSpace;
    private float posX;
    private float parentVisibility=1f;

    public VerticalRList(float width, float height, float posx, float posy){
        setAvWSpace( width );
        setAvHSpace( height );
        setPosX( posx );
        setPosY( posy );
    }

    public VerticalRList(){}

    public void addElement(Positionable p){
        addElement( p,1 );
    }

    public void addElement(Positionable p, float weight){
        children.addLast( p );
        weights.addLast( weight );
        p.setVisibility( parentVisibility );
        recalculate();
    }
    public void removeElement(Positionable p){
        weights.remove(children.indexOf( p ));
        children.remove( p );
        recalculate();
    }

    public void removeLast(){
        weights.removeLast();
        children.removeLast().remove();
        recalculate();
    }

    public void removeFirst(){
        weights.removeLast();
        children.removeLast().remove();
        recalculate();
    }

    private void recalculate(){
        float wsum=0;
        for(float w:weights){
            wsum+=w;
        }

        float vUnit=AvWSpace/wsum;
        float pos=0;

        ListIterator wi=weights.listIterator();
        for(Positionable p:children){
            float w=(Float) wi.next();
            pos+=(vUnit*w)/2;
            p.setAvWSpace( vUnit*w );
            p.setPosX( pos+(posX-AvWSpace/2f) );
            pos+=(vUnit*w)/2;
            p.setAvHSpace( AvHSpace );
            p.setPosY( posY );
        }
    }

    @Override
    public void setAvHSpace(float avHSpace) {
        this.AvHSpace=avHSpace;
        recalculate();
    }

    @Override
    public void setAvWSpace(float avWSpace) {
        this.AvWSpace=avWSpace;
        recalculate();
    }

    @Override
    public void setPosX(float posX) {
        this.posX=posX;
        recalculate();
    }

    @Override
    public void setPosY(float posY) {
        this.posY=posY;
        recalculate();
    }

    @Override
    public void setVisibility(float v) {
        parentVisibility=v;
        for(Positionable p:children){
            p.setVisibility( v );
        }
    }

    @Override
    public void remove() {
        for(Positionable p:children){
            p.remove();
        }
        disconnect();
    }

    public float getAvHSpace() {
        return AvHSpace;
    }

    public float getPosY() {
        return posY;
    }

    public float getAvWSpace() {
        return AvWSpace;
    }

    public float getPosX() {
        return posX;
    }

    public float getParentVisibility() {
        return parentVisibility;
    }
}
