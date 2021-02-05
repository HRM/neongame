package ExtGl2Lib.Shader.MultiDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class RectDrawShaderMD extends Shader{
    private static String mVertexShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "attribute vec4 vCompP;\n" +
            "attribute vec4 vCompF;\n" +
            "attribute vec4 vCol;\n" +
            "\n" +
            "uniform vec2 scrComp;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "varying vec2 scale;\n" +
            "varying vec4 color;\n" +
            "varying float filled;\n" +
            "varying float thickness;\n" +
            "varying float fade;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    gl_Position = vec4(vCompP.xy/scrComp,0.0,1.0);\n" +
            "    txCord= vCompP.zw;\n" +
            "    scale= vCompF.xy;\n" +
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

    private static String mFragmentShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "varying vec2 scale;\n" +
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
            "    float atcx=abs(txCord.x),atcy=abs(txCord.y);\n" +
            "    if(atcx<scale.x&&atcy<scale.y){\n" +
            "        if(filled==0.0)\n" +
            "        dist=min(scale.x-atcx,scale.y-atcy);\n" +
            "        else\n" +
            "        dist=0.0;\n" +
            "    }\n" +
            "    else{\n" +
            "        if(atcx>scale.x&&atcy>scale.y){\n" +
            "            dist=distance(txCord,vec2(sign(txCord.x)*scale.x,sign(txCord.y)*scale.y));\n" +
            "        }else{\n" +
            "            if(atcx>scale.x){\n" +
            "                dist=atcx-scale.x;\n" +
            "            }else{\n" +
            "                dist=atcy-scale.y;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    \n" +
            "    float factor = 1.0-min(max(dist-thickness,0.0)/fade,1.0);\n" +
            "    if(toColor==1){\n" +
            "        gl_FragColor = vec4(color.rgb*factor,1.0);\n" +
            "    }\n" +
            "    else {\n" +
            "         gl_FragColor = vec4(color.rgb,color.a*factor);\n" +
            "    }\n" +
            "}";


   public RectDrawShaderMD(){
       super(mVertexShaderCode,mFragmentShaderCode);
       Log.i("ShaderInit","RectDrawShader");

    }

}
