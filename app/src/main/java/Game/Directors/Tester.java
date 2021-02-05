package Game.Directors;

import Engine.Animation.AnimFloat;
import Engine.Element;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.ListLayout.HorizontalERList;
import Game.EngieParts.UiEngine.ListLayout.HorizontalRList;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.EngieParts.UiEngine.UiElements.Button;
import Game.EngieParts.UiEngine.UiElements.Dummy;
import Game.EngieParts.UiEngine.UiElements.IconButton;
import Game.EngieParts.UiEngine.UiElements.NodeAwardPanel;
import Game.EngieParts.UiEngine.UiElements.Text;
import Game.GameElements.Achive.Award;
import Game.GameElements.Achive.Vault;

public class Tester extends Element{

    private HorizontalERList HERL=new HorizontalERList( 25*pke(),10*pke());
    private HorizontalRList VRL=new HorizontalRList( GL2F.getDefWidth(),GL2F.getDefHeight(),0,0 );
    public Tester(){
        VRL.addElement( new Dummy(),0.1f );
        VerticalRList hrl=new VerticalRList(  );
        hrl.addElement( new Dummy(),0.5f );
        hrl.addElement(new Text( 15*pke(),"The good stuff" ),11  );
        hrl.addElement( new Dummy(),0.5f );
        hrl.addElement( new IconButton( 25*pke(),25*pke(),0x1,()->{VRL.remove();callEvent( "showBouncer",null );disconnect();new Menu();} ),2 );
        hrl.addElement( new Dummy(),0.5f );
        VRL.addElement( hrl );
        VRL.addElement( HERL,10 );
        Vault.addToUi( Vault.getAwards(),HERL );

    }

}
