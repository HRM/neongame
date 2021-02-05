package Game.EngieParts.UiEngine.UiElements;

import Game.EngieParts.UiEngine.Positionable;
import Game.EngieParts.UiEngine.UiBlock;

public class SpaceDefiner extends UiBlock {
    private Positionable block=null;

    public SpaceDefiner(float width, float height){
        RealSize[0]=width;
        RealSize[1]=height;
    }

    public void setBlock(Positionable p){
        block=p;
        block.setPosX( pos[0] );
        block.setPosY( pos[1] );
        block.setAvWSpace( size[0] );
        block.setAvHSpace( size[1] );
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
        super.setAvHSpace( avHSpace );
        if(block!=null)block.setAvHSpace(size[1] );
    }

    @Override
    public void setAvWSpace(float avWSpace) {
        super.setAvWSpace( avWSpace );
        if(block!=null)block.setAvWSpace( size[0] );
    }

    @Override
    public void setPosX(float posX) {
        super.setPosX( posX );
        if(block!=null)block.setPosX( pos[0] );
    }

    @Override
    public void setPosY(float posY) {
        super.setPosY( posY );
        if(block!=null)block.setPosY( pos[1] );
    }

    @Override
    public void setVisibility(float v) {
        super.setVisibility( v );
        if(block!=null)block.setVisibility( parentVisibility );
    }

    @Override
    public void remove() {
        super.remove();
        if(block!=null)block.remove();
    }
}
