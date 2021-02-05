package Game.EngieParts.UiEngine.UiElements;

import android.util.Log;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;
import Game.EngieParts.UiEngine.UiBlock;

public class Image extends UiBlock {
    protected Texture drawTex;
    protected float blockRat;
    protected float[] txSize;
    protected float txWidth;
    protected AnimFloat visibility=getFloatAnimator( 1,0.2f );

    public Image(float width, float height, float txWidth, Texture tex){
        blockRat=height/width;
        RealSize[0]=width;
        RealSize[1]=height;
        this.txWidth=txWidth;
        drawTex=tex;
        addEventHandler( "drawL3",(e)->onDraw() );
    }

    public Image(float width, float height, float txWidth, Texture tex, String layer){
        blockRat=height/width;
        RealSize[0]=width;
        RealSize[1]=height;
        this.txWidth=txWidth;
        drawTex=tex;
        addEventHandler( layer,(e)->onDraw() );
    }

    public Image(float width, float height, Texture tex){
        this(width,height,width,tex);
    }

    private void onDraw(){
        if(parentVisibility*visibility.getVal()<=0)return;
        GL2F.setTexSca( txSize );
        GL2F.drawTex( new float[]{size[0]/2f,size[1]/2f},pos,0,GL2F.COOP.VGenOneOpa( parentVisibility*visibility.getVal(),new float[4] ), drawTex );
        GL2F.setTexSca( new float[]{1,1} );
    }

    @Override
    public void setAvHSpace(float avHSpace) {
        super.setAvHSpace( avHSpace );
        txSize=new float[]{size[0]/(txWidth*this.fitSizeMod),blockRat*drawTex.getWidth()/drawTex.getHeight()*size[0]/(txWidth*this.fitSizeMod)};
    }
    @Override
    public void setAvWSpace(float avHSpace) {
        super.setAvWSpace( avHSpace );
        txSize=new float[]{size[0]/(txWidth*this.fitSizeMod),blockRat*drawTex.getWidth()/drawTex.getHeight()*size[0]/(txWidth*this.fitSizeMod)};
    }

    @Override
    public void remove() {
        visibility.animateTo( 0 ,()->disconnect());
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        drawTex.del();
    }
}
