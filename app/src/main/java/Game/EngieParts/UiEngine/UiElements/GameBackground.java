package Game.EngieParts.UiEngine.UiElements;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;

public class GameBackground extends Image {
    public GameBackground(float width, float height, float txWidth, Texture tex) {
        super( width, height, txWidth, tex,"drawBL3" );
        addEventHandler( "drawLight",(e)->onDrawlight() );
    }
    private void onDrawlight(){
        if(visibility.getVal()*parentVisibility<=0)return;
        GL2F.setCol( GL2F.COOP.VGenStr( visibility.getVal()*parentVisibility , new float[4]) );
        GL2F.setSqf( true );
        GL2F.setFade( size[1] );
        GL2F.setFill( true );
        GL2F.setSfe( false );
        GL2F.drawLine( new float[]{pos[0]-size[0]/2f+size[1]/2f,pos[1]},new float[]{pos[0]+size[0]/2f-size[1]/2f,pos[1]} );
    }
}
