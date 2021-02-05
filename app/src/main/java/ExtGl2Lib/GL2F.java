package ExtGl2Lib;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ExtGl2Lib.Buffers.CircleBuffer;
import ExtGl2Lib.Buffers.RectBuffer;
import ExtGl2Lib.Buffers.TexCompound;
import ExtGl2Lib.Helpers.BufferBindHelper;
import ExtGl2Lib.Helpers.TextureBinder;
import ExtGl2Lib.Mesh.Square;
import ExtGl2Lib.Resource.Interfaces.Texture;
import ExtGl2Lib.Resource.TextureResTar;
import ExtGl2Lib.Shader.Elder.Shader;
import ExtGl2Lib.Shader.MultiDraw.CircleDrawShaderMD;
import ExtGl2Lib.Shader.MultiDraw.RectDrawShaderMD;
import ExtGl2Lib.Shader.MultiDraw.TextureDrawShaderMD;
import ExtGl2Lib.Shader.SIngleDraw.BubbleDistShader;
import ExtGl2Lib.Shader.SIngleDraw.CircleDrawShader;
import ExtGl2Lib.Shader.SIngleDraw.RectDrawShader;
import ExtGl2Lib.Shader.SIngleDraw.TextureDrawShader;

public class GL2F {

    //Meshes
    private static Square squareMesh;

    //Shaders
    private static RectDrawShader rDraw;
    private static TextureDrawShader tDraw;
    private static CircleDrawShader cDraw;

    private static TextureDrawShaderMD tDrawMD;
    private static RectDrawShaderMD rDrawMD;
    private static CircleDrawShaderMD cDrawMD;

    private static BubbleDistShader bDist;

    //Buffers
    private static RectBuffer rBuffer=new RectBuffer();
    private static CircleBuffer cBuffer=new CircleBuffer();
    private static TexCompound tBuffer=new TexCompound();

    //DrawParams
    private static float thickness = 1f;
    private static float fade = 0.5f;
    private static float sfade = 0.5f;
    private static float[] col = {1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] scol = {1.0f, 1.0f, 1.0f, 1.0f};
    private static boolean toCol = true;
    private static boolean fill = true;
    private static float[] texPos = {0f, 0f};
    private static float[] texSca = {1f, 1f};
    private static boolean flipTexY = true;
    private static float[] defScrComp = {1f, 1f};
    private static float[] activeScrComp = {1f, 1f};
    private static int defWidth;
    private static int defHeight;
    private static boolean sfeEnabled;
    private static boolean sqf=true;

    //Modes
    private static boolean directMode=true;

    //OptimizationParams
    private static Shader prevShader=null;

    //Init
    public static void Init(){
        TextureBinder.reset();
        BufferBindHelper.unbindBuffers();

        rDraw = new RectDrawShader();
        GLERR("s2");
        tDraw = new TextureDrawShader();
        GLERR("s3");
        cDraw = new CircleDrawShader();
        GLERR("s4");
        bDist = new BubbleDistShader();
        GLERR("s5");
        tDrawMD= new TextureDrawShaderMD();
        GLERR("s6");
        rDrawMD= new RectDrawShaderMD();
        GLERR("s7");
        cDrawMD= new CircleDrawShaderMD();
        GLERR("s6");
        squareMesh=new Square();
        GLERR("m1");

        squareMesh.bindBuffer();
    }

    public static void Destroy(){
        rDraw.deleteShader();
        rDraw = null;
        cDraw.deleteShader();
        cDraw=null;
        tDraw.deleteShader();
        tDraw = null;
        bDist.deleteShader();
        bDist = null;
        tDrawMD.deleteShader();
        tDrawMD= null;
        rDrawMD.deleteShader();
        rDrawMD= null;
        cDrawMD.deleteShader();
        cDrawMD= null;
        squareMesh.del();
        squareMesh=null;
    }

    //Setters


    public static void setSqf(boolean sqf) {
        GL2F.sqf = sqf;
    }

    public static void setSfe(boolean en){sfeEnabled=en;}

    public static void setFlipTexY(boolean flipTexY) {
        GL2F.flipTexY = flipTexY;
    }

    public static void setThickness(float thickness) {
        GL2F.thickness = thickness;
    }

    public static void setFade(float fade) {
        GL2F.fade = fade;
    }

    public static void setSFade(float fade) {
        GL2F.sfade = fade;
    }

    public static void setCol(float[] col) {
        System.arraycopy( col,0,GL2F.col,0,4 );
    }

    public static void setSCol(float[] col) {
        System.arraycopy( col,0,GL2F.scol,0,4 );
    }

    public static void setToCol(boolean toCol) {
        GL2F.toCol = toCol;
    }

    public static void setTexPos(float[] texPos) {
        System.arraycopy( texPos,0,GL2F.texPos,0,2 );
    }

    public static void setTexSca(float[] texSca) {
        System.arraycopy( texSca,0,GL2F.texSca,0,2 );
    }

    public static void setFill(boolean fill) {
        GL2F.fill = fill;
    }

    public static void setDirectMode(boolean directMode) {
        GL2F.directMode = directMode;
    }

    //Getters

    public static boolean getToCol() {
        return toCol;
    }

    public static float[] getCol() {
        return col;
    }

    public static float getThickness() {
        return thickness;
    }

    public static float getFade() {
        return fade;
    }

    public static float[] getTexPos() {
        return texPos;
    }

    public static float[] getTexSca() {
        return texSca;
    }

    public static boolean getFill() {
        return fill;
    }

    public static int getDefWidth() {
        return defWidth;
    }

    public static int getDefHeight() {
        return defHeight;
    }

    public static boolean getFlipTexY() {
        return flipTexY;
    }

    public static boolean isDirectMode() {
        return directMode;
    }

    public static Square getSquareMesh() {
        return squareMesh;
    }

    public static float getSfade() {
        return sfade;
    }

    public static float[] getScol() {
        return scol;
    }

    public static boolean isSfeEnabled() {
        return sfeEnabled;
    }

    public static boolean isSqf() {
        return sqf;
    }

    //TargetSetFunctions

    public static void SetRenderTarget(TextureResTar target) {
        if (target != null) {
            GLES20.glViewport(0, 0, target.getWidth(), target.getHeight());
            activeScrComp[0] = (float) target.getWidth() / 2.0f;
            activeScrComp[1] = (float) target.getHeight() / 2.0f;
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, target.getFrameBuffer());
        }
    }

    public static void SetRenderTarget(TextureResTar target,float mul) {
        if (target != null) {
            GLES20.glViewport(0, 0, target.getWidth(), target.getHeight());
            activeScrComp[0] = (float) target.getWidth() / 2.0f * mul;
            activeScrComp[1] = (float) target.getHeight() / 2.0f * mul;
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, target.getFrameBuffer());
        }
    }

    public static void SetDefaultTarget() {
        activeScrComp[0] = defScrComp[0];
        activeScrComp[1] = defScrComp[1];
        GLES20.glViewport(0, 0, defWidth, defHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public static void SetDefaultScreenSize(int width, int height) {
        defScrComp[0] = (float) width / 2.0f;
        defScrComp[1] = (float) height / 2.0f;
        defWidth = width;
        defHeight = height;
    }

    //Blending functions

    public static void EnableAlphaBlending() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        toCol = false;
    }

    public static void EnableAdditiveBlending() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        toCol = true;
    }

    public static void EnableMultiplicativeBlending() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_DST_COLOR, GLES20.GL_ZERO);
        toCol = true;
    }

    public static void DisableBlending() {
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    //Target related functions

    public static void Clear(float[] color) {
        GLES20.glClearColor(color[0], color[1], color[2], color[3]);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public static void Clear() {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    //Drawing functions

    public static void drawRect(float[] scale, float[] pos, float rot) {
        if(directMode) {
            if (prevShader != rDraw) {
                if (prevShader != null) prevShader.DisableShader();
                rDraw.EnableShader();
                squareMesh.bindBuffer();
                rDraw.setVertexAttribute( "vPosition", null );
                prevShader = rDraw;
            }
            rDraw.setUniform4f( "vColor", col[0], col[1], col[2], col[3] );
            rDraw.setUniform4f( "vColorS", scol[0], scol[1], scol[2], scol[3] );

            rDraw.setUniform1b( "sqf",sqf );
            rDraw.setUniform1b( "sfe",sfeEnabled );
            rDraw.setUniform1f( "fade", fade );
            rDraw.setUniform1f( "fadeS", sfade );
            rDraw.setUniform1f( "thickness", thickness );
            rDraw.setUniform2f( "scrComp", activeScrComp[0], activeScrComp[1] );
            rDraw.setUniform1i( "toColor", toCol ? 1 : 0 );
            rDraw.setUniform1b( "filled", fill );

            rDraw.setUniform2f( "position", pos[0], pos[1] );
            rDraw.setUniform2f( "scale", scale[0], scale[1] );
            rDraw.setUniform1f( "rotation", rot );

            rDraw.drawTriangleStripElemenents( squareMesh.getElementCount(), null );
        }else{
            rBuffer.addRect( scale,pos,rot );
        }
    }

    public static void drawOutRects(){
        rBuffer.drawOut();
    }

    public static void drawRectMD(FloatBuffer comp1,FloatBuffer comp2,FloatBuffer compCol,ShortBuffer ind,int count) {
        if(prevShader!=rDrawMD){
            if(prevShader!=null)prevShader.DisableShader();
            rDrawMD.EnableShader();
            prevShader=rDrawMD;
            BufferBindHelper.unbindBuffers();
        }

        rDrawMD.setUniform2f( "scrComp",activeScrComp[0],activeScrComp[1] );
        rDrawMD.setUniform1i( "toColor",toCol ? 1 : 0);

        rDrawMD.setVertexAttribute("vCompP", comp1  );
        rDrawMD.setVertexAttribute("vCompF", comp2  );
        rDrawMD.setVertexAttribute("vCol", compCol  );

        rDrawMD.drawTriangleElemenents( count,ind );
    }

    public static void drawLine(float[] p1,float[] p2){
        float diffx=p1[0]-p2[0],diffy=p1[1]-p2[1];
        float[] centp=new float[]{(p1[0]+p2[0])/2f,(p1[1]+p2[1])/2f};
        float[] size=new float[]{(float)Math.sqrt( diffx*diffx+diffy*diffy )/2f,0};
        float rot=(float)Math.atan( diffy/diffx );
        drawRect( size,centp,rot );
    }

    public static void drawCircle(float[] pos, float rad) {
        if(directMode) {
            if (prevShader != cDraw) {
                if (prevShader != null) prevShader.DisableShader();
                cDraw.EnableShader();
                squareMesh.bindBuffer();
                cDraw.setVertexAttribute( "vPosition", null );
                prevShader=cDraw;
            }
            cDraw.setUniform1b( "sqf",sqf );
            cDraw.setUniform1b( "sfe",sfeEnabled );
            cDraw.setUniform4f( "vColor", col[0], col[1], col[2], col[3] );
            cDraw.setUniform1f( "fade", fade );
            cDraw.setUniform4f( "vColorS", scol[0], scol[1], scol[2], scol[3] );
            cDraw.setUniform1f( "fadeS", sfade );
            cDraw.setUniform1f( "thickness", thickness );
            cDraw.setUniform2f( "scrComp", activeScrComp[0], activeScrComp[1] );
            cDraw.setUniform1i( "toColor", toCol ? 1 : 0 );
            cDraw.setUniform1b( "filled", fill );

            cDraw.setUniform2f( "position", pos[0], pos[1] );
            cDraw.setUniform1f( "rad", rad );

            cDraw.drawTriangleStripElemenents( squareMesh.getElementCount(), null );
        }else{
            cBuffer.addCircle( pos,rad );
        }
    }

    public static void drawOutCircs(){
        cBuffer.drawOut();
    }

    public static void drawCircleMD(FloatBuffer comp1,FloatBuffer comp2,FloatBuffer compCol,ShortBuffer ind,int count) {
        if(prevShader!=cDrawMD){
            if(prevShader!=null)prevShader.DisableShader();
            BufferBindHelper.unbindBuffers();
            cDrawMD.EnableShader();
            prevShader=cDrawMD;
        }

        cDrawMD.setUniform2f( "scrComp",activeScrComp[0],activeScrComp[1] );
        cDrawMD.setUniform1i( "toColor",toCol ? 1 : 0);
        cDrawMD.setVertexAttribute("vCompP", comp1 );
        cDrawMD.setVertexAttribute("vCompF", comp2  );
        cDrawMD.setVertexAttribute("vCol", compCol  );


        cDrawMD.drawTriangleElemenents( count,ind );
        squareMesh.bindBuffer();
    }

    public static void drawTex(float[] scale, float[] pos, float rot, float[] color, Texture tex) {
        if(directMode) {
            if (prevShader != tDraw) {
                if (prevShader != null) prevShader.DisableShader();
                tDraw.EnableShader();
                squareMesh.bindBuffer();
                tDraw.setVertexAttribute( "vPosition", null );
                prevShader = tDraw;
            }

            tDraw.setUniform2f( "position", pos[0], pos[1] );
            tDraw.setUniform2f( "scale", scale[0], scale[1] );
            tDraw.setUniform1f( "rotation", rot );

            tDraw.setUniform2f( "scrComp", activeScrComp[0], activeScrComp[1] );

            tDraw.setUniform2f( "txPos", texPos[0], texPos[1] );
            tDraw.setUniform2f( "txScale", texSca[0], texSca[1] );
            tDraw.setUniform4f( "vColor", color[0], color[1], color[2], color[3] );
            tDraw.setUniform1b( "flipy", flipTexY );

            TextureBinder.bindTexture( 0, tex.getGlTexture() );
            tDraw.setUniformSampler( "image", 0 );
            tDraw.drawTriangleStripElemenents( squareMesh.getElementCount(), null );
        }else{
            tBuffer.addTex( scale,pos,rot,color,tex );
        }
    }

    public static void drawOutTexs(){
        tBuffer.drawOut();
    }

    public static void drawOut(){
        drawOutRects();
        drawOutTexs();
        drawOutCircs();
    }

    public static void drawTexMD(FloatBuffer comp, FloatBuffer compCol,ShortBuffer ind,int count, int tex) {
        if(prevShader!=tDrawMD){
            if(prevShader!=null)prevShader.DisableShader();
            tDrawMD.EnableShader();
            BufferBindHelper.unbindBuffers();
            prevShader=tDrawMD;
        }

        tDrawMD.setVertexAttribute("vComp", comp  );
        tDrawMD.setVertexAttribute("vCol", compCol  );

        tDrawMD.setUniform2f( "scrComp",activeScrComp[0],activeScrComp[1] );

        TextureBinder.bindTexture( 0,tex );
        tDrawMD.setUniformSampler( "image",0 );
        tDrawMD.drawTriangleElemenents( count,ind );
    }


    //Distortion functions

    public static void BubbleDist(Texture tex, float str, float vecStr, float[] pos, float rad) {
        if(prevShader!=bDist){
            if(prevShader!=null)prevShader.DisableShader();
            bDist.EnableShader();
            squareMesh.bindBuffer();
            bDist.setVertexAttribute( "vPosition",null );
            prevShader=bDist;
        }

        TextureBinder.bindTexture( 0,tex.getGlTexture() );
        bDist.setUniformSampler( "image",0 );
        bDist.setUniform1f( "fade",fade );
        bDist.setUniform1f( "thickness",thickness );
        bDist.setUniform2f( "scrComp",activeScrComp[0],activeScrComp[1] );
        bDist.setUniform1i( "toColor",toCol ? 1 : 0);
        bDist.setUniform1b( "filled",fill );
        bDist.setUniform1f( "distStr",str );
        bDist.setUniform1f( "vecStr",vecStr );

        bDist.setUniform2f( "position",pos[0],pos[1] );
        bDist.setUniform1f( "rad",rad );

        bDist.drawTriangleStripElemenents( squareMesh.getElementCount(),null );
    }

    //Texture related functions

    public static void genFrameBufferFromTexture(int[] tex, int[] fbo) {
        GLES20.glGenFramebuffers(1, fbo, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, tex[0], 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public static void genTexture(int[] tex, int width, int height, Buffer data) {
        genTexture( tex,width,height,data,true,false );
    }

    public static void genTexture(int[] tex, int width, int height, Buffer data, boolean linearFiltering, boolean repeat) {
        GLES20.glGenTextures(1, tex, 0);
        TextureBinder.bindTexture( 0,tex[0] );

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, data);


        if(linearFiltering){
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        }else {
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );
        }

        if(repeat){
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT );
        }else {
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
        }

    }

    public static void genTextureFromBitmap(int[] tex, Bitmap img) {
        genTextureFromBitmap( tex,img, true,false );
    }

    public static void genTextureFromBitmap(int[] tex, Bitmap img,boolean linearFiltering,boolean repeat) {
        GLES20.glGenTextures(1, tex, 0);
        TextureBinder.bindTexture( 0,tex[0] );
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0,img, 0);
        GLUtils.getEGLErrorString(0);

        if(linearFiltering){
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        }else {
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );
        }

        if(repeat){
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT );
        }else {
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
            GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
        }

    }

    public static class COOP {
        public final static float[] V4ONES = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

        //Color modifier

        public static float[] LightenColor(float[] color, float ammount,float[] out) {
            out[0]=color[0] * (1.0f - ammount) + ammount;
            out[1]=color[1] * (1.0f - ammount) + ammount;
            out[2]=color[2] * (1.0f - ammount) + ammount;
            out[3]=color[3];

            return out;
        }

        public static float[] DarkenColor(float[] color, float ammount,float[] out) {
            out[0]=color[0] * (1.0f - ammount);
            out[1]= color[1] * (1.0f - ammount);
            out[2]=color[2] * (1.0f - ammount);
            out[3]= color[3];

            return out;
        }

        public static float[] GetColorByHue(float hue, float alpha,float[] out) {
            float lightness=Math.min(1f, (float)Math.floor(hue/360)/16f);
            hue %= 360f;
            int section = (int) Math.floor(hue / 60f);
            float w = (hue % 60f) / 60f;


            int ind1 = section / 2, ind2 = (ind1 + 1) % 3;
            out[0]=0;out[1]=0;out[2]=0;

            out[ind1] = section % 2 == 0 ? 1f : 1f - w;
            out[ind2] = section % 2 == 0 ? w : 1f;
            out[3]=alpha;

            LightenColor( out,lightness,out );
            return out;
        }

        //Vector modifier

        public static float[] ModOpa(float[] color, float opa,float[] out) {
            out[0]=color[0];
            out[1]=color[1];
            out[2]=color[2];
            out[3]=opa;

            return out;
        }

        public static float[] Multiply(float[] vec, float val,float[] out) {
            for (int i = 0; i < vec.length; ++i) {
                out[i] = vec[i] * val;
            }
            return out;
        }

        public final static float[] VGenOneOpa(float opacity,float[] out) {
            out[0]=1.0f; out[1]=1.0f; out[2]=1.0f; out[3]=opacity;
            return out;
        }

        public final static float[] VGenStr(float strength,float[] out) {
            out[0]=strength; out[1]=strength; out[2]=strength; out[3]=strength;
            return out;
        }

    }

    //Debug

    public static void GLERR(String when){
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("glerror", "("+when+") :" + GLUtils.getEGLErrorString(error));
        }
    }
    public static String SIL(int sh){
        return GLES20.glGetShaderInfoLog(sh);
    }
}
