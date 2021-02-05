package Game.EngieParts.UiEngine.UiElements;

import Engine.Animation.AnimFloat;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.GameElements.Achive.Award;

public class NodeAwardPanel extends VerticalRList {
    private static float nodeSize=10*pke();
    private static float textSize=10*pke();
    private static float buttonWidth=40*pke();
    private static float buttonHeight=10*pke();

    private float visibility=1f;
    private float parentVisibility=0f;

    private AnimFloat glow=getFloatAnimator( 0,0.2f );

    private Award aw;
    GameNode nodes[];
    Button mButton;
    Text mText;

    private void Init() {
        nodes = new GameNode[]{new GameNode( this.aw.nop, 0, nodeSize ), new GameNode( this.aw.nop, 1, nodeSize ), new GameNode( this.aw.nop, 2, nodeSize )};
        this.addElement( nodes[0], 1 );
        this.addElement( nodes[1], 1 );
        this.addElement( nodes[2], 1 );

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
                    this.aw.nop.Activate();
                    mText = new Text( textSize, "Active" );
                    this.addElement( mText, 2 );
                    callEvent( "checkActive", null );
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
            addEventHandler( "checkActive", (e) -> {
                if (!this.aw.isActive()) {
                    this.removeLast();
                    mText = null;
                    mButton = new Button( buttonHeight,buttonWidth, "Use", () -> {
                        this.removeLast();
                        mButton = null;
                        this.aw.nop.Activate();
                        mText = new Text( textSize, "Active" );
                        this.addElement( mText, 2 );
                        callEvent( "checkActive", null );
                        glow.animateTo(  0.3f);
                    } );
                    this.addElement( mButton, 2 );
                    glow.animateTo(  0.0f);
                }
            } );
        }
        addEventHandler( "drawLight",(e)->{
            if(glow.getVal()==0.0f) return;
            GL2F.setSfe( false );
            GL2F.setFade( 2*pke() );
            GL2F.setThickness( 0f );
            GL2F.setCol( GL2F.COOP.VGenStr( parentVisibility*glow.getVal(), new float[4] ) );
            GL2F.setFill( true );
            GL2F.drawRect( new float[]{getAvWSpace()/2f,getAvHSpace()/2f},new float[]{getPosX(),getPosY()},0 );
        } );
    }

    public NodeAwardPanel(Award aw, float width, float height, float posx, float posy) {
        super( width, height, posx, posy );
        this.aw = aw;
        Init();
    }

    public NodeAwardPanel(Award aw) {
        this.aw = aw;
        Init();
    }

    @Override
    public void setVisibility(float v) {
        super.setVisibility( v*visibility );
        parentVisibility=v*visibility;
    }
}
