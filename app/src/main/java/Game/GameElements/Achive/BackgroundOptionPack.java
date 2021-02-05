package Game.GameElements.Achive;

import Engine.Context;
import Game.Options;

public class BackgroundOptionPack {
    public final int image;
    public final int thumbnailImage;
    public final float backgroundImageWidth;

    public BackgroundOptionPack(int image, int thumbnailImage, float backgroundImageWidth) {
        this.image = image;
        this.thumbnailImage = thumbnailImage;
        this.backgroundImageWidth = backgroundImageWidth;
    }

    public void Activate(){
        Options.backgroundImage=image;
        Options.texWidth= backgroundImageWidth;
        Context.eventTable.callEvent( "refreshBackground",null );
    }

    public boolean isActive(){
        return Options.backgroundImage==image&&Options.texWidth== backgroundImageWidth;
    }
}
