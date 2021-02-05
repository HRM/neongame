package Game.GameElements.Backgrounds;

import Engine.Element;
import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.TextureRes;
import Game.Options;

public class Tier1Background extends Element{
    private static float scrRat=(float)GL2F.getDefHeight()/(float)GL2F.getDefWidth();
    private static float[] scrSize=new float[]{GL2F.getDefWidth()/2f,GL2F.getDefHeight()/2f};
    private static float[] pos=new float[]{0,0};

    TextureRes mTex;
    float[] size;

    public Tier1Background() {

        addEventHandler( "refreshBackground",(e)->onRefreshBackground() );

        float txWidth=Options.texWidth;
        this.mTex = new TextureRes( fB( Options.backgroundImage ),true,true );
        size=new float[]{1f*GL2F.getDefWidth()/txWidth,scrRat*mTex.getWidth()/mTex.getHeight()*GL2F.getDefWidth()/txWidth};

        addEventHandler("drawBL1",(e)->onDraw());
    }

    private void onDraw(){
        GL2F.setTexSca( size );
        GL2F.drawTex( scrSize,pos,0,GL2F.COOP.V4ONES,mTex );
        GL2F.setTexSca( new float[]{1,1} );
    }

    private void onRefreshBackground(){
        float txWidth=Options.texWidth;
        mTex.del();
        this.mTex = new TextureRes( fB( Options.backgroundImage ),true,true );
        size=new float[]{1f*GL2F.getDefWidth()/txWidth,scrRat*mTex.getWidth()/mTex.getHeight()*GL2F.getDefWidth()/txWidth};
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        mTex.del();
    }
}
