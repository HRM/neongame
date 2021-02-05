package ExtGl2Lib.Shader.MultiDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class CircleDrawShaderMD extends Shader {
    private static String mVertexShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "attribute vec4 vCompP;\n" +
            "attribute vec4 vCompF;\n" +
            "attribute vec4 vCol;\n" +
            "\n" +
            "uniform vec2 scrComp;\n" +
            "\n"+
            "varying vec2 txCord;\n" +
            "varying float rad;\n" +
            "varying vec4 color;\n" +
            "varying float filled;\n" +
            "varying float thickness;\n" +
            "varying float fade;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = vec4(vCompP.xy/scrComp,0.0,1.0);\n" +
            "    txCord= vCompP.zw;\n" +
            "    rad= vCompF.x;\n" +
            "    fade= vCompF.z;\n" +
            "    if(vCompF.w<0.0){\n" +
            "       filled=1.0;\n" +
            "       thickness=-vCompF.w;\n"+
            "    }else{\n" +
            "      filled=0.0;" +
            "      thickness=vCompF.w;\n"+
            "    }\n"+
            "    color=vCol;\n"+
            "}";

    private static String mFragmentShaderCode = "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "varying float rad;\n" +
            "varying vec4 color;\n" +
            "varying float filled;\n" +
            "varying float thickness;\n" +
            "varying float fade;\n" +
            "uniform int toColor;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    float dist;\n" +
            "    \n" +
            "    dist=abs(length(txCord)-rad);\n" +
            "    if(filled>0.0&&length(txCord)<rad)dist=0.0;\n" +
            "    \n" +
            "    float factor = 1.0-min(max(dist-thickness,0.0)/fade,1.0);\n" +
            "    \n" +
            "    if(toColor==1){\n" +
            "        gl_FragColor = vec4(color.rgb*factor,1.0);\n" +
            "    }\n" +
            "    else {\n" +
            "         gl_FragColor = vec4(color.rgb,color.a*factor);\n" +
            "    }\n" +
            "}";

    public CircleDrawShaderMD(){
        super(mVertexShaderCode,mFragmentShaderCode);
        Log.i("ShaderInit","CircleDrawShader");
    }


}
