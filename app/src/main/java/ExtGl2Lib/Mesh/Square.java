package ExtGl2Lib.Mesh;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ExtGl2Lib.Helpers.BufferBindHelper;

public class Square {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private int VBO;
    private int IBO;

    static float squareCoords[] = {
            -1f, 1f, 0.0f,   // top left
            -1f, -1f, 0.0f,   // bottom left
            1f, -1f, 0.0f,   // bottom right
            1f,  1f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 3, 2 }; // order to draw vertices

    private final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private final int elementCount = 4;

    public Square() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int buffers[]=new int[]{0,0};

        GLES20.glGenBuffers( 2,buffers,0 );

        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER,buffers[0]);
        GLES20.glBufferData( GLES20.GL_ARRAY_BUFFER, squareCoords.length*4,vertexBuffer,GLES20.GL_STATIC_DRAW );

        GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER,buffers[1]);
        GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, drawOrder.length*2,drawListBuffer,GLES20.GL_STATIC_DRAW );

        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER,0);

        VBO=buffers[0];
        IBO=buffers[1];
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVertexStride() {
        return vertexStride;
    }

    public int getElementCount() {
        return elementCount;
    }

    public int getCoordsPerVertex(){
        return COORDS_PER_VERTEX;
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public ShortBuffer getDrawListBuffer() {
        return drawListBuffer;
    }

    public void bindBuffer(){
        BufferBindHelper.bindBuffers( VBO,IBO );
    }

    public void unbindBuffer(){
       BufferBindHelper.unbindBuffers();
    }

    public void del(){
        GLES20.glDeleteBuffers( 2,new int[]{VBO,IBO},0 );
        VBO=0;
        IBO=0;
    }


}