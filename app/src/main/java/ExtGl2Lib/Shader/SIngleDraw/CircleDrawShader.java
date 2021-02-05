package ExtGl2Lib.Shader.SIngleDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class CircleDrawShader extends Shader {
    private static String mVertexShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "attribute vec3 vPosition;\n" +
            "\n" +
            "uniform vec2 scrComp;\n" +
            "uniform float rad;\n" +
            "uniform vec2 position;\n" +
            "\n" +
            "uniform float thickness;\n" +
            "uniform float fade;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    vec2 scaled = vPosition.xy*(vec2(rad,rad)+vec2(fade)+vec2(thickness));\n" +
            "\n" +
            "    gl_Position = vec4((scaled+position)/scrComp,0.0,1.0);\n" +
            "    txCord= scaled;\n" +
            "}";

    private static String mFragmentShaderCode = "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "uniform bool sqf;\n" +
            "uniform bool sfe;\n" +
            "uniform float rad;\n" +
            "uniform vec4 vColor;\n" +
            "uniform vec4 vColorS;\n" +
            "uniform bool filled;\n" +
            "uniform float thickness;\n" +
            "uniform float fade;\n" +
            "uniform float fadeS;\n" +
            "uniform int toColor;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    float dist;\n" +
            "    \n" +
            "    dist=abs(length(txCord)-rad);\n" +
            "    if(filled&&length(txCord)<rad)dist=0.0;\n" +
            "    \n" +
            "    float partfac =max(dist-thickness,0.0);\n" +
            "    float factor = 1.0-min(partfac/fade,1.0);\n" +
            "    float factor2=0.0;\n"+
            "    if(sfe)factor2 = 1.0-min(partfac/fadeS,1.0);\n"+
            "    if(sqf){factor*=factor;factor2*=factor2;}\n"+
            "    if(toColor==1){\n" +
            "        gl_FragColor = vec4(vColor.rgb*factor,1.0);\n" +
            "        if(sfe)gl_FragColor += vec4(vColorS.rgb*factor2,1.0);\n"+
            "    }\n" +
            "    else {\n" +
            "         gl_FragColor = vec4(vColor.rgb,vColor.a*factor);\n" +
            "         if(sfe)gl_FragColor += vec4(vColorS.rgb,vColorS.a*factor2);\n"+
            "    }\n" +
            "}";

    public CircleDrawShader(){
        super(mVertexShaderCode,mFragmentShaderCode);
        Log.i("ShaderInit","CircleDrawShader");
    }


}
