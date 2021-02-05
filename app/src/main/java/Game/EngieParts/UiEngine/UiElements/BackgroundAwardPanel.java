package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.TextureRes;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.GameElements.Achive.Award;
import tams.libbuild.R;

public class BackgroundAwardPanel extends VerticalRList {
    private static float textSize=10*pke();
    private static float buttonWidth=40*pke();
    private static float buttonHeight=10*pke();

    private float visibility=1f;
    private float parentVisibility=0f;

    private AnimFloat glow=getFloatAnimator( 0,0.2f );

    private Award aw;
    private Button mButton;
    private Text mText;

    private void Init() {
        if(aw.bop.thumbnailImage!=0) {
            this.addElement( new GameBackground( 70 * pke(), 25 * pke(), 128 * pke(), new TextureRes( fB( aw.bop.thumbnailImage ), true, true ) ), 3 );
        }else{
            this.addElement( new GameBackground( 70 * pke(), 25 * pke(), aw.bop.backgroundImageWidth, new TextureRes( fB( aw.bop.image ), true, true ) ), 3 );
        }


        if (aw.isUnlocked()) {
            if (aw.isActive()) {
                mText = new Text( textSize, "Active" );
                this.addElement( mText, 2 );
                mButton = null;
                glow.animateTo(  0.3f);
            } else {
                mButton = new Button( buttonHeight,buttonWidth, "Use", () -> {
                    this.removeLast();
                    mButton = null;
                    this.aw.bop.Activate();
                    mText = new Text( textSize, "Active" );
                    this.addElement( mText, 2 );
                    callEvent( "checkActiveBack", null );
                    glow.animateTo(  0.3f);
                } );
                this.addElement( mButton, 2 );
                mText = null;
            }
        } else {
            mText = new Text( textSize, "Unlocks at: " + aw.Quota );
            this.addElement( mText, 2 );
            mButton = null;
            visibility=0.3f;
        }
        if (aw.isUnlocked()) {
            addEventHandler( "checkActiveBack", (e) -> {
                if (!this.aw.isActive()) {
                    this.removeLast();
                    mText = null;
                    mButton = new Button( buttonHeight,buttonWidth, "Use", () -> {
                        this.removeLast();
                        mButton = null;
                        this.aw.bop.Activate();
                        mText = new Text( textSize, "Active" );
                        this.addElement( mText, 2 );
                        callEvent( "checkActiveBack", null );
                        glow.animateTo(  0.3f);
                    } );
                    this.addElement( mButton, 2 );
                    glow.animateTo(  0.0f);
                }
            } );
        }
        addEventHandler( "drawLight",(e)->{
            if(parentVisibility*glow.getVal()==0.0f) return;
            GL2F.setSfe( false );
            GL2F.setFade( 2*pke() );
            GL2F.setThickness( 0f );
            GL2F.setCol( GL2F.COOP.VGenStr( parentVisibility*glow.getVal(), new float[4] ) );
            GL2F.setFill( true );
            GL2F.drawRect( new float[]{getAvWSpace()/2f,getAvHSpace()/2f},new float[]{getPosX(),getPosY()},0 );
        } );
    }

    public BackgroundAwardPanel(Award aw, float width, float height, float posx, float posy) {
        super( width, height, posx, posy );
        this.aw = aw;
        Init();
    }

    public BackgroundAwardPanel(Award aw) {
        this.aw = aw;
        Init();
    }

    @Override
    public void setVisibility(float v) {
        super.setVisibility( v*visibility );
        parentVisibility=v*visibility;
    }
}
