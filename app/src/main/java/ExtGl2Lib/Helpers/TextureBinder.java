package ExtGl2Lib.Helpers;

import android.opengl.GLES20;

public class TextureBinder {
    private static int[] activeTextures=new int[]{0,0,0,0,0,0,0,0};
    private static int activeUnit=0;

    public static void bindTexture(int unit,int texture){
        if(activeTextures[unit]!=texture){
            GLES20.glActiveTexture( GLES20.GL_TEXTURE0 + unit );
            if(unit!=activeUnit){
                GLES20.glActiveTexture( GLES20.GL_TEXTURE0 + unit );
                activeUnit=unit;
            }

            GLES20.glBindTexture( GLES20.GL_TEXTURE_2D,texture );
            activeTextures[unit]=texture;
        }
    }

    public static void reset(){
        activeTextures=new int[]{0,0,0,0,0,0,0,0};
        activeUnit=0;
    }
}
