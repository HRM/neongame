package ExtGl2Lib.Shader.SIngleDraw;


import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class BubbleDistShader extends Shader {
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
            "varying vec2 txCordReal;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    vec2 scaled = vPosition.xy*(vec2(rad,rad)+vec2(fade)+vec2(thickness));\n" +
            "\n" +
            "    gl_Position = vec4((scaled+position)/scrComp,0.0,1.0);\n" +
            "    txCord= scaled;\n" +
            "    txCordReal=(gl_Position.xy+vec2(1.0,1.0))*0.5;\n"+
            "}";

    private static String mFragmentShaderCode = "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "uniform vec2 position;\n" +
            "uniform vec2 scrComp;\n" +
            "\n" +
            "uniform sampler2D image;\n" +
            "uniform float distStr;\n" +
            "uniform float vecStr;\n" +
            "uniform float rad;\n" +
            "uniform bool filled;\n" +
            "uniform float thickness;\n" +
            "uniform float fade;\n" +
            "uniform int toColor;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "varying vec2 txCordReal;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    float dist;\n" +
            "    \n" +
            "    dist=abs(length(txCord)-rad);\n" +
            "    \n" +
            "    float factor = 1.0-min(max(dist-thickness,0.0)/fade,1.0);\n" +
            "    if(filled&&length(txCord)<rad)factor=1.0;\n" +
            "    vec4 color;\n" +
            "    vec2 posTrans=(txCordReal-(position/scrComp+vec2(1.0,1.0))*0.5)*vecStr;\n" +
            "    color=texture2D(image,txCordReal-sign(distStr)*(1.0-pow(length(txCord)/(rad+fade+thickness),abs(distStr)))*posTrans*factor);\n"+
            "    if(toColor==1){\n" +
            "        gl_FragColor = vec4(color.rgb*ceil(factor),1.0);\n" +
            "    }\n" +
            "    else {\n" +
            "         gl_FragColor = vec4(color.rgb,color.a*ceil(factor));\n" +
            "    }\n" +
            "}";

    public BubbleDistShader(){
        super(mVertexShaderCode,mFragmentShaderCode);
        Log.i("ShaderInit","BubbleDistShader");

    }

}

