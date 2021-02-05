package ExtGl2Lib.Helpers;

import android.opengl.GLES20;

public class BufferBindHelper {
    private static int IBO=0;
    private static int VBO=0;

    public static void bindBuffers(int vbo,int ibo){
        if(IBO!=ibo){
            GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER,ibo );
            IBO=ibo;
        }
        if(VBO!=vbo){
            GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER,vbo );
            VBO=vbo;
        }
    }
    public static void unbindBuffers(){
        if(IBO!=0){
            GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER,0 );
            IBO=0;
        }
        if(VBO!=0){
            GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER,0 );
            VBO=0;
        }
    }
}
