package ExtGl2Lib.Shader.SIngleDraw;

import android.util.Log;

import ExtGl2Lib.Shader.Elder.Shader;

public class RectDrawShader extends Shader{
    private static String mVertexShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "attribute vec3 vPosition;\n" +
            "\n" +
            "uniform vec2 scrComp;\n" +
            "uniform float rotation;\n" +
            "uniform vec2 scale;\n" +
            "uniform vec2 position;\n" +
            "\n" +
            "uniform float thickness;\n" +
            "uniform float fade;\n" +
            "\n" +
            "varying vec2 txCord;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    vec2 scaled = vPosition.xy*(scale+vec2(fade)+vec2(thickness));\n" +
            "    vec2 rotated;\n" +
            "    rotated.x = scaled.x*cos(rotation) - scaled.y*sin(rotation);\n" +
            "    rotated.y = scaled.y*cos(rotation) + scaled.x*sin(rotation);\n" +
            "    gl_Position = vec4((rotated+position)/scrComp,0.0,1.0);\n" +
            "    txCord= scaled;\n" +
            "}";

    private static String mFragmentShaderCode = "\n" +
            "precision highp float;\n" +
            "precision highp int;\n" +
            "\n" +
            "uniform bool sfe;\n"+
            "uniform bool sqf;\n"+
            "uniform vec2 scale;\n" +
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
            "    float atcx=abs(txCord.x),atcy=abs(txCord.y);\n" +
            "    if(atcx<scale.x&&atcy<scale.y){\n" +
            "        if(!filled)\n" +
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
            "    float partfac =max(dist-thickness,0.0);\n" +
            "    float factor = 1.0-min(partfac/fade,1.0);\n" +
            "    float factor2=0.0;\n"+
            "    if(sfe)factor2 = 1.0-min(partfac/fadeS,1.0);\n"+
            "    if(sqf){factor*=factor;factor2*=factor2;}\n"+
            "    if(toColor==1){\n" +
            "        gl_FragColor = vec4(vColor.rgb*factor*factor,1.0);\n" +
            "        if(sfe)gl_FragColor += vec4(vColorS.rgb*factor2*factor2,1.0);\n"+
            "    }\n" +
            "    else {\n" +
            "         gl_FragColor = vec4(vColor.rgb,vColor.a*factor*factor);\n" +
            "         if(sfe)gl_FragColor += vec4(vColorS.rgb,vColorS.a*factor2*factor2);\n"+
            "    }\n" +
            "}";


   public RectDrawShader(){
       super(mVertexShaderCode,mFragmentShaderCode);
       Log.i("ShaderInit","RectDrawShader");

    }

}
