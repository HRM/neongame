package ExtGl2Lib.Shader.MultiDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class TextureDrawShaderMD extends Shader {
    private static String mVertexShaderCode = "precision highp float;\n" +
            "attribute vec4 vComp;\n" +
            "attribute vec4 vCol;\n" +
            "uniform vec2 scrComp;\n"+
            "varying vec4 color;\n"+
            "varying vec2 txCord;\n" +
            "void main() {\n" +
            "  gl_Position = vec4(vComp.xy/scrComp,0.0,1.0);\n" +
            "  txCord=vComp.zw;\n"+
            "  color=vCol;\n"+
            "}";

    private static String mFragmentShaderCode = "precision highp float;\n" +
            "uniform sampler2D image;\n" +
            "varying vec4 color;\n"+
            "varying vec2 txCord;\n" +
            "void main(){\n" +
            "  gl_FragColor = texture2D(image,txCord)*color;\n" +
            "}";

    public TextureDrawShaderMD() {
        super(mVertexShaderCode,mFragmentShaderCode);
        Log.i( "ShaderInit", "TextureDrawShader" );
    }
}
