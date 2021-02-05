package Game.Directors;


import Engine.Animation.AnimFloat;
import Engine.Element;
import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.TextureRes;
import Game.EngieParts.UiEngine.ListLayout.HorizontalRList;
import Game.EngieParts.UiEngine.UiElements.Button;
import Game.EngieParts.UiEngine.UiElements.GameBackground;
import Game.EngieParts.UiEngine.UiElements.Image;
import Game.EngieParts.UiEngine.UiElements.Text;
import Game.GameElements.util.LightBouncer;
import Game.PlayerData;
import tams.libbuild.R;

public class Menu extends Element {
    private float[] dummy=new float[4];
    private HorizontalRList buttonList=new HorizontalRList(128*pke(),128*pke(),0,0);
    private Button PlayButton=new Button(15*pke(),50*pke(),"Play",()->{
        new LineGame();
        buttonList.remove();
        callEvent( "fadeBouncer",null );
        setTimeout(  ()->disconnect(),0.2f);} );
    private Button OptionsButton=new Button(15*pke(),50*pke(),"Options",()->{
        new Tester();
        buttonList.remove();
        callEvent( "fadeBouncer",null );
        setTimeout(  ()->disconnect(),0.2f);} );
    private Button EndScreenTester=new Button(15*pke(),50*pke(),"End Screen",()->{
        new AfterGameMenu( 5000 );
        buttonList.remove();
        setTimeout(  ()->disconnect(),0.2f);} );
    private Text title=new Text(50*pke(),"Neon");

    public Menu(){
        buttonList.addElement( title,1 );
        buttonList.addElement( new Text(10*pke(),"- Lvl:"+ PlayerData.pointsCollected+" -"),0.5f );
        buttonList.addElement( PlayButton,1 );
        buttonList.addElement( OptionsButton,1 );
        buttonList.addElement( EndScreenTester );


    }

}
