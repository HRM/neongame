package ExtGl2Lib.Resource;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;

public class TextureRes implements Texture {
    private int mGlTexture;
    private int mWidth;
    private int mHeight;
    public TextureRes(Bitmap img){
        this(img,true,false);
    }

    public TextureRes(Bitmap img, boolean linear, boolean repeat){
        int[] texpoint = new int[1];
        GL2F.genTextureFromBitmap(texpoint,img,linear,repeat);
        mWidth=img.getWidth();
        mHeight=img.getHeight();
        mGlTexture=texpoint[0];
    }

    @Override
    public int getGlTexture() {
        return mGlTexture;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    public void del(){
        GLES20.glDeleteTextures(1,new int[]{mGlTexture},0);
        mGlTexture=0;
        mWidth=0;
        mHeight=0;
    }
}
