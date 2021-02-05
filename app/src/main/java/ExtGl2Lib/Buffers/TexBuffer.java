package ExtGl2Lib.Buffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;

public class TexBuffer {
    private int comSize=1000;
    private int indSize=1000;

    private int currPos=0;


    private FloatBuffer comp1= ByteBuffer.allocateDirect( comSize*4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer();
    private FloatBuffer compCol = ByteBuffer.allocateDirect( comSize*4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer();
    private ShortBuffer ind= ByteBuffer.allocateDirect( indSize*2 ).order( ByteOrder.nativeOrder() ).asShortBuffer();

    float col[]=new float[4*4];
    float w1[]=new float[4*4];
    short w3[]=new short[6];

    private void resetWs(){
        w1[0]=-1f;w1[1]=1f;w1[2]=0f;w1[3]=0f;
        w1[4]=-1f;w1[5]=-1f;w1[6]=0f;w1[7]=1f;
        w1[8]=1f;w1[9]=-1f;w1[10]=1f;w1[11]=1f;
        w1[12]=1f;w1[13]=1f;w1[14]=1f;w1[15]=0f;
    }
    private void setCol(float color[]){
        this.col[0]=color[0];
        this.col[1]=color[1];
        this.col[2]=color[2];
        this.col[3]=color[3];
        this.col[4]=color[0];
        this.col[5]=color[1];
        this.col[6]=color[2];
        this.col[7]=color[3];
        this.col[8]=color[0];
        this.col[9]=color[1];
        this.col[10]=color[2];
        this.col[11]=color[3];
        this.col[12]=color[0];
        this.col[13]=color[1];
        this.col[14]=color[2];
        this.col[15]=color[3];
    }
    private void setInd(int el){
        w3[0]=(short)(el*4);
        w3[1]=(short)(el*4+2);
        w3[2]=(short)(el*4+1);
        w3[3]=(short)(el*4);
        w3[4]=(short)(el*4+3);
        w3[5]=(short)(el*4+2);
    }

    private void resizeVBuff(int size){
        comSize=size;
        FloatBuffer tcomp1=ByteBuffer.allocateDirect( comSize*4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer();
        FloatBuffer tcolor= ByteBuffer.allocateDirect( comSize*4 ).order( ByteOrder.nativeOrder() ).asFloatBuffer();
        comp1.position(0);
        compCol.position(0);
        comp1.limit( Math.min(comp1.limit(),size) );
        compCol.limit( Math.min( compCol.limit(),size) );
        tcomp1.put( comp1);
        tcolor.put( compCol );
        comp1=tcomp1;
        compCol =tcolor;
    }
    private void resizeIBuff(int size){
        comSize=size;
        ShortBuffer tind=ByteBuffer.allocateDirect( indSize*2 ).order( ByteOrder.nativeOrder() ).asShortBuffer();
        ind.position(0);
        ind.limit( Math.min(ind.limit(),size) );
        tind.put(ind);
        ind=tind;
    }

    public void addTex(float[] scale, float[] pos, float rot,float[] color){
        if(currPos*4*4>comSize)resizeVBuff( comSize*2 );
        if(currPos*6>indSize)resizeIBuff( indSize*2 );
        resetWs();
        setInd( currPos );
        setCol( color );
        for(int i=0;i<4;i++){
            w1[i*4]*=scale[0];
            w1[i*4+1]*=scale[1];
            if(rot!=0) {
                float tx = (float) (w1[i * 4] * Math.cos( rot ) - w1[i * 4 + 1] * Math.sin( rot ));
                float ty = (float) (w1[i * 4 + 1] * Math.cos( rot ) + w1[i * 4] * Math.sin( rot ));
                w1[i * 4] = tx + pos[0];
                w1[i * 4 + 1] = ty + pos[1];
            }else{
                w1[i * 4] += pos[0];
                w1[i * 4 + 1] += pos[1];
            }
            if(!GL2F.getFlipTexY()){
                w1[i*4+3]=1-w1[i*4+3];
            }
            w1[i*4+2]=w1[i*4+2]*GL2F.getTexSca()[0]+GL2F.getTexPos()[0];
            w1[i*4+3]=w1[i*4+3]*GL2F.getTexSca()[1]+GL2F.getTexPos()[1];
        }
        comp1.put(w1);
        ind.put(w3);
        compCol.put( col );
        currPos++;
    }

    public void drawOut(int tex){
        comp1.position( 0 );
        compCol.position(0);
        ind.position(0);
        GL2F.drawTexMD( comp1,compCol,ind,currPos*6,tex);
        reset();
    }

    public void reset(){
        comp1.position( 0 );
        compCol.position(0);
        ind.position(0);
        currPos=0;
    }


}
