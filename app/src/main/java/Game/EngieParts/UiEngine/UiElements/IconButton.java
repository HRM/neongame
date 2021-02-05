package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import Engine.Element;
import Engine.Events.EventType.PointerEvent;
import ExtGl2Lib.GL2F;
import Game.EngieParts.FontEngine.Font;
import Game.EngieParts.UiEngine.ButtonCallback;
import Game.EngieParts.UiEngine.Positionable;
import Game.EngieParts.UiEngine.UiBlock;
import Game.GameElements.GUI.FontSet;

public class IconButton extends UiBlock {
    int iconCode;
    float fontSize;
    Font mFont= FontSet.icons;
    ButtonCallback eventOnPress;
    AnimFloat visibility=getFloatAnimator( 0,0.2f );;
    AnimFloat pressColorMod = getFloatAnimator( 1,0.1f );
    AnimFloat pressSizeMod = getFloatAnimator( 1,0.1f );
    AnimFloat pressGlowMod = getFloatAnimator(0,0.1f);

    float[] color=GL2F.COOP.GetColorByHue( (float) (Math.random()*360),1,new float[4] );

    public IconButton(float fontSize, int icon, ButtonCallback event){
        iconCode =icon;
        this.fontSize=fontSize;
        RealSize[0]=mFont.getAbsLength(icon,fontSize )/0.8f;
        RealSize[1]=fontSize/0.8f;
        eventOnPress=event;

        visibility.animateTo( 1 );

        addEventHandler( "removeUi",(e)->visibility.animateTo( 0,()->disconnect() ) );

        addEventHandler( "pointerEvent",(e)->onPointerEvent( (PointerEvent)e ) );
        addEventHandler( "drawL3",(e)->onDraw() );
        addEventHandler( "drawLight",(e)->onDrawLight() );
    }

    public IconButton(float height, float width, int icon, ButtonCallback event){
        iconCode =icon;

        RealSize[0]=width;
        RealSize[1]=height;

        float partL=mFont.getAbsLength(icon,RealSize[1] );
        if(partL>RealSize[0]){
            fontSize=RealSize[0]/partL*RealSize[1]*0.8f;
        }
        else{
            fontSize=RealSize[1]*0.8f;
        }

        eventOnPress=event;
        visibility.animateTo( 1 );

        addEventHandler( "removeUi",(e)->visibility.animateTo( 0,()->disconnect() ) );

        addEventHandler( "pointerEvent",(e)->onPointerEvent( (PointerEvent)e ) );
        addEventHandler( "drawL3",(e)->onDraw() );
        addEventHandler( "drawLight",(e)->onDrawLight() );
    }
    public IconButton(float height, float width, int icon, float[] color, ButtonCallback event){
        this(height,width,icon,event);
        this.color=color.clone();
    }

    public void setFont(Font f){
        mFont=f;
    }

    private void onPointerEvent(PointerEvent pe){
        if(pe.getAType()==0){
            if(pe.getY()<pos[1]+size[1]/2&&pe.getY()>pos[1]-size[1]/2){
                if(pe.getX()<pos[0]+size[0]/2&&pe.getX()>pos[0]-size[0]/2){
                    pressColorMod.animateTo( 0.0f, ()-> pressColorMod.animateTo( 1 ) );
                    pressSizeMod.animateTo( 0.9f, ()-> pressSizeMod.animateTo( 1 ) );
                    pressGlowMod.animateTo( 1,()->pressGlowMod.animateTo( 0 ) );
                    if(eventOnPress!=null&&pressSizeMod.getVal()==1f) {
                        setTimeout( ()->{eventOnPress.call();},0.2f );
                        callEvent( "playTapSound",null );
                    }
                }
            }
        }
    }

    private void onDraw(){
        if(parentVisibility*visibility.getVal()>0) {
            GL2F.setSfe( false );
            GL2F.setFill( false );

            GL2F.setThickness( pke() / 4 );
            GL2F.setFade( 0.1f );
            GL2F.setCol( GL2F.COOP.ModOpa( GL2F.COOP.LightenColor( color, pressColorMod.getVal(), new float[4] ), visibility.getVal() * parentVisibility, new float[4] ) );
            GL2F.drawRect( new float[]{size[0] / 2  * pressSizeMod.getVal() , size[1] / 2  * pressSizeMod.getVal() }, pos, 0 );

            GL2F.setThickness( 0.1f );
            GL2F.setFade( pke() );
            GL2F.setCol( GL2F.COOP.VGenOneOpa( visibility.getVal() * parentVisibility, new float[4] ) );
            GL2F.drawRect( new float[]{size[0] / 2  * pressSizeMod.getVal() , size[1] / 2  * pressSizeMod.getVal() }, pos, 0 );

            GL2F.setCol( GL2F.COOP.ModOpa( GL2F.COOP.LightenColor( color, pressColorMod.getVal(), new float[4] ), visibility.getVal() * parentVisibility, new float[4] ) );
            mFont.drawCharAbsVu( this.iconCode, pos, fontSize  * pressSizeMod.getVal()*fitSizeMod );
        }
    }

    private void onDrawLight(){
        if(parentVisibility*visibility.getVal()*pressGlowMod.getVal()>0) {
            GL2F.setFill( false );
            GL2F.setSfe( true );
            GL2F.setThickness( pke() / 4 );
            GL2F.setFade( pke() * 4f );
            GL2F.setSFade( pke() );
            GL2F.setSCol( GL2F.COOP.Multiply( GL2F.COOP.LightenColor( color, pressColorMod.getVal(), new float[4] ), visibility.getVal() * parentVisibility *pressGlowMod.getVal(), new float[4] ) );
            GL2F.setCol( GL2F.COOP.Multiply( GL2F.COOP.LightenColor( color, pressColorMod.getVal(), new float[4] ), visibility.getVal() * parentVisibility *pressGlowMod.getVal(), new float[4] ) );
            GL2F.drawRect( new float[]{size[0] / 2 * pressSizeMod.getVal(), size[1] / 2 * pressSizeMod.getVal()}, pos, 0 );
        }
    }

    @Override
    public void remove(){
        visibility.animateTo( 0,()->disconnect() );
    }
}
