package ExtGl2Lib.Resource;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;

public class TextureResTar implements Texture {
    private int mGlTexture;
    private int mWidth;
    private int mHeight;
    private int mFrameBuffer;

    public TextureResTar(Bitmap img) {
        this(img,true,false);
    }

    public TextureResTar(Bitmap img, boolean linear, boolean repeat) {
        int[] texpoint = new int[1];
        int[] fbpoint = new int[1];
        GL2F.genTextureFromBitmap(texpoint, img, linear, repeat);
        mWidth = img.getWidth();
        mHeight = img.getHeight();
        mGlTexture = texpoint[0];

        GL2F.genFrameBufferFromTexture(texpoint, fbpoint);
        mFrameBuffer = fbpoint[0];
    }

    public TextureResTar(int width, int height) {
        this(width,height,true,false);
    }

    public TextureResTar(int width, int height,boolean linear,boolean repeat) {
        int[] texpoint = new int[1];
        int[] fbpoint = new int[1];
        mWidth = width;
        mHeight = height;
        GL2F.genTexture(texpoint, width, height, null, linear, repeat);
        mGlTexture = texpoint[0];

        GL2F.genFrameBufferFromTexture(texpoint, fbpoint);
        mFrameBuffer = fbpoint[0];
    }

    public boolean SwapRes(TextureResTar tex) {
        if (tex.mWidth == this.mWidth && tex.mHeight == this.mHeight) {
            int st, sf;
            st = tex.mGlTexture;
            sf = tex.mFrameBuffer;
            tex.mGlTexture = this.mGlTexture;
            tex.mFrameBuffer = this.mFrameBuffer;
            this.mFrameBuffer = sf;
            this.mGlTexture = st;
            return true;
        } else return false;
    }


    @Override
    public int getGlTexture() {
        return mGlTexture;
    }

    public int getFrameBuffer(){return mFrameBuffer;}

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    public void del() {
        GLES20.glDeleteFramebuffers(1, new int[]{mFrameBuffer}, 0);
        GLES20.glDeleteTextures(1, new int[]{mGlTexture}, 0);
        mGlTexture = 0;
        mFrameBuffer = 0;
        mWidth = 0;
        mHeight = 0;
    }
}