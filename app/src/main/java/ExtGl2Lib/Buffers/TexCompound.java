package ExtGl2Lib.Buffers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;

import ExtGl2Lib.Resource.Interfaces.Texture;

public class TexCompound {
    private Hashtable<Integer,Integer> texParser=new Hashtable<Integer, Integer>(  );
    private int buffCount=15;
    private int[] textures=new int[buffCount];
    private TexBuffer[] texBuffers=new TexBuffer[buffCount];
    private int filled=0;

    Integer conv=0;

    public TexCompound(){
        for(int i=0;i<buffCount;i++){
            texBuffers[i]=new TexBuffer();
        }
    }

    public void addTex(float[] scale, float[] pos, float rot,float[] color,Texture tex){
        Integer t=texParser.get(tex.getGlTexture());
        if(t!=null){
            texBuffers[t].addTex( scale,pos,rot,color );
        }else{
            textures[filled]=tex.getGlTexture();
            texBuffers[filled].addTex( scale,pos,rot,color );
            texParser.put(tex.getGlTexture(),filled);
            filled++;
        }
    }
    public void drawOut(){
        for(int i=0;i<filled;i++){
            texBuffers[i].drawOut( textures[i] );
        }
        filled=0;
        texParser.clear();
    }


}
