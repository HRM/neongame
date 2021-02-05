package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import Game.EngieParts.FontEngine.Font;
import Game.EngieParts.UiEngine.UiBlock;
import Game.GameElements.GUI.FontSet;

public class ProgressText extends UiBlock {

    private int progressMargin;
    private AnimFloat progress;
    private Font mFont= FontSet.neonFont;
    private AnimFloat visibility;
    private AnimFloat sizeMod =getFloatAnimator( 1,0.3f );

    float[] color=new float[]{1,1,1,1};

    public ProgressText( float fontSize,int progress,int progressMargin){
        this.progressMargin=progressMargin;
        this.progress=getFloatAnimator( progress, 0.2f );
        RealSize[0]=mFont.getLiength( ""+progressMargin+"/"+progressMargin,fontSize );
        RealSize[1]=fontSize;
        visibility=getFloatAnimator( 0,0.2f );
        visibility.animateTo( 1 );
        addEventHandler( "removeUi",(e)->remove() );
        addEventHandler( "drawL3",(e)->onDraw() );
    }
    public ProgressText( float fontSize,int progress,int progressMargin,float[] color){
        this(fontSize,progress,progressMargin);
        this.color=color.clone();
    }

    public ProgressText( float fontSize,int progress,float[] color){
        this(fontSize,progress,0);
        this.color=color.clone();
    }

    public ProgressText( float fontSize,int progress){
        this(fontSize,progress,0);
    }

    private void onDraw(){
        if(parentVisibility*visibility.getVal()>0) {
            GL2F.setCol( GL2F.COOP.ModOpa( color, visibility.getVal() * parentVisibility, new float[4] ) );
            if(progressMargin!=0) {
                mFont.drawString( "" + ((int) Math.ceil( progress.getVal() )) + "/" + progressMargin, pos, size[1] * sizeMod.getVal(), 0, 0 );
            }else{
                mFont.drawString( "" + ((int) Math.ceil( progress.getVal() )), pos, size[1] * sizeMod.getVal(), 0, 0 );
            }
        }
    }

    public void remove(){
        visibility.animateTo( 0,()->disconnect() );
    }

    public AnimFloat getSizeMod(){
        return sizeMod;
    }

    public void setProgressMargin(int progressMargin) {
        this.progressMargin = progressMargin;
    }

    public int getProgressMargin() {
        return progressMargin;
    }

    public AnimFloat getProgress() {
        return progress;
    }
}
