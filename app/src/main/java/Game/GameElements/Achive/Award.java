package Game.GameElements.Achive;

import Game.PlayerData;

public class Award {
    public final int Quota;
    public final BackgroundOptionPack bop;
    public final NodeOptionPack nop;

    public Award(int quota, BackgroundOptionPack bop) {
        Quota = quota;
        this.bop = bop;
        this.nop = null;
    }

    public Award(int quota, NodeOptionPack nop) {
        Quota = quota;
        this.nop = nop;
        this.bop = null;
    }

    public void Activate(){
        if(nop!=null)nop.Activate();
        if(bop!=null)bop.Activate();
    }

    public boolean isActive(){
        if(bop!=null)return bop.isActive();
        if(nop!=null)return nop.isActive();
        else return false;
    }

    public boolean isUnlocked(){
        return PlayerData.pointsCollected>=Quota;
    }

    public int getAwardType(){
        return nop!=null||bop!=null?nop!=null?1:2:0;
    }

}
