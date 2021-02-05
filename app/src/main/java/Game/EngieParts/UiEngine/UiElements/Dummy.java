package Game.EngieParts.UiEngine.UiElements;

import Game.EngieParts.UiEngine.Positionable;
import Game.EngieParts.UiEngine.UiBlock;

public class Dummy implements Positionable {
    private float avHSpace=0;
    private float avWSpace=0;
    private float parentVisibility=1f;
    private float posX=0;
    private float posY=0;
    private Positionable block=null;

    public void setBlock(Positionable p){
        block=p;
        block.setPosX( posX );
        block.setPosY( posY );
        block.setAvWSpace( avWSpace );
        block.setAvHSpace( avHSpace );
        block.setVisibility( parentVisibility );
    }

    public void removeBlock(){
        block.remove();
        block=null;
    }

    public void detachBlock(){
        block=null;
    }

    @Override
    public void setAvHSpace(float avHSpace) {
        this.avHSpace=avHSpace;
        if(block!=null)block.setAvHSpace( avHSpace );
    }

    @Override
    public void setAvWSpace(float avWSpace) {
        this.avHSpace=avWSpace;
        if(block!=null)block.setAvWSpace( avWSpace );
    }

    @Override
    public void setPosX(float posX) {
        this.posX=posX;
        if(block!=null)block.setPosX( posX );
    }

    @Override
    public void setPosY(float posY) {
        this.posY=posY;
        if(block!=null)block.setPosY( posY );
    }

    @Override
    public void setVisibility(float v) {
        this.parentVisibility=v;
        if(block!=null)block.setVisibility( v );
    }

    @Override
    public void remove() {
        if(block!=null)block.remove();
    }
}
