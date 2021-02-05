package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import Game.EngieParts.FontEngine.Font;
import Game.EngieParts.UiEngine.UiBlock;
import Game.GameElements.GUI.FontSet;

public class Text extends UiBlock {
    
    private String text;
    private float fontSize;
    private Font mFont= FontSet.neonFont;
    private AnimFloat visibility;
    private AnimFloat xPosMod=getFloatAnimator( 0,0.3f );
    private AnimFloat yPosMod=getFloatAnimator( 0,0.3f );
    private AnimFloat sizeMod =getFloatAnimator( 1,0.3f );

    float[] color=new float[]{1,1,1,1};

    public Text( float fontSize,String text){
        this.text =text;
        
        this.fontSize=fontSize;
        RealSize[0]=mFont.getLiength( text,fontSize );
        RealSize[1]=fontSize;
        visibility=getFloatAnimator( 0,0.2f );
        visibility.animateTo( 1 );

        addEventHandler( "removeUi",(e)->remove() );

        addEventHandler( "drawL3",(e)->onDraw() );
    }
    public Text( float fontSize,String text,float[] color){
        this(fontSize,text);
        this.color=color.clone();
    }

    private void onDraw(){
        if(parentVisibility*visibility.getVal()>0) {
            GL2F.setCol( GL2F.COOP.ModOpa( color, visibility.getVal() * parentVisibility, new float[4] ) );
            mFont.drawString( this.text, new float[]{pos[0]+xPosMod.getVal(),pos[1]+yPosMod.getVal()}, size[1]* sizeMod.getVal() , 0, 0 );
        }
    }

    public void remove(){
        visibility.animateTo( 0,()->disconnect() );
    }

    public AnimFloat getSizeMod(){
        return sizeMod;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AnimFloat getVisibility() {
        return visibility;
    }

    public AnimFloat getxPosMod() {
        return xPosMod;
    }

    public AnimFloat getyPosMod() {
        return yPosMod;
    }
}
