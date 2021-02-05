package ExtGl2Lib.Shader.SIngleDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class TextureDrawShader extends Shader {
    private static String mVertexShaderCode = "precision highp float;\n" +
            "attribute vec3 vPosition;\n" +
            "uniform float rotation;\n" +
            "uniform vec2 scale;\n" +
            "uniform vec2 position;\n" +
            "uniform vec2 scrComp;\n"+
            "uniform bool flipy;\n"+
            "uniform vec2 txPos;\n" +
            "uniform vec2 txScale;\n" +
            "varying vec2 txCord;\n" +
            "void main() {\n" +
            "  vec2 scaled = vPosition.xy*scale;\n" +
            "  vec2 rotated;\n" +
            "  rotated.x = scaled.x*cos(rotation) - scaled.y*sin(rotation);\n" +
            "  rotated.y = scaled.y*cos(rotation) + scaled.x*sin(rotation);\n" +
            "  gl_Position = vec4((rotated+position)/scrComp,0.0,1.0);\n" +
            "  txCord=(vPosition.xy+vec2(1.0,1.0))*0.5;\n" +
            "  if(flipy){txCord.y=1.0-txCord.y;}\n"+
            "  txCord=txCord*txScale+txPos;\n"+
            "}";

    private static String mFragmentShaderCode = "precision highp float;\n" +
            "uniform sampler2D image;\n" +
            "uniform vec4 vColor;\n"+
            "varying vec2 txCord;\n" +
            "void main(){\n" +
            "  gl_FragColor = texture2D(image,txCord)*vColor;\n" +
            "}";

    public TextureDrawShader() {
        super(mVertexShaderCode,mFragmentShaderCode);
        Log.i( "ShaderInit", "TextureDrawShader" );
    }
}
