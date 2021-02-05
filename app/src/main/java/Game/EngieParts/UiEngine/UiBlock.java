package Game.EngieParts.UiEngine;

import android.util.Log;

import Engine.Element;

public abstract class UiBlock extends Element implements Positionable{
    protected float[] AvSpace=new float[]{1,1};
    protected float[] RealSize=new float[]{1,1};
    protected float[] size=new float[]{1,1};
    protected float[] pos=new float[]{0,0};
    protected float fitSizeMod=1f;
    protected float parentVisibility =1f;

    private void calcFitSizeMod(){
        if(Math.min(AvSpace[0]-RealSize[0],AvSpace[1]-RealSize[1])<0){
            float ratio;
            if(AvSpace[0]-RealSize[0]<AvSpace[1]-RealSize[1]){
                ratio=AvSpace[0]/RealSize[0];
            }else{
                ratio=AvSpace[1]/RealSize[1];
            }
            fitSizeMod=ratio;
            size[0]=ratio*RealSize[0];
            size[1]=ratio*RealSize[1];
        }else{
            fitSizeMod=1f;
            size=RealSize.clone();
        }
    }

    @Override
    public void setAvHSpace(float avHSpace) {
        AvSpace[1]=avHSpace;
        calcFitSizeMod();
    }

    @Override
    public void setAvWSpace(float avWSpace) {
        AvSpace[0]=avWSpace;
        calcFitSizeMod();
    }

    @Override
    public void setPosX(float posX) {
        pos[0]=posX;
    }

    @Override
    public void setPosY(float posY) {
        pos[1]=posY;
    }

    @Override
    public void setVisibility(float v) {
        parentVisibility =v;
    }

    @Override
    public void remove() {
        disconnect();
    }
}
