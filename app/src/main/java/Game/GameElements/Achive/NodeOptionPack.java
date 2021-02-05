package Game.GameElements.Achive;

import java.util.Arrays;

import Game.Options;

public class NodeOptionPack {
    public final float[] nodeColors;
    public final int nodeShape;

    public NodeOptionPack(int nodeShape,float ... colors) {
        this.nodeShape = nodeShape;
        nodeColors =colors;
    }

    public void Activate(){
        Options.nodeColors = nodeColors.clone();
        Options.nodeShape=nodeShape;
    }

    public boolean isActive(){
        return  Arrays.equals(Options.nodeColors,nodeColors)&&nodeShape==Options.nodeShape;
    }
}
