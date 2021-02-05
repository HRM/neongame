package Game.Directors;

import Engine.Context;
import Engine.Element;
import Engine.Timing.TimingNode;
import ExtGl2Lib.GL2F;
import Game.EngieParts.UiEngine.ListLayout.HorizontalRList;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.EngieParts.UiEngine.UiElements.Dummy;
import Game.EngieParts.UiEngine.UiElements.IconButton;
import Game.EngieParts.UiEngine.UiElements.Text;
import Game.GameElements.Save.SaveData;

import static android.content.Context.MODE_PRIVATE;

public class PauseMenu extends Element {
    private static float countDownTime=0.6f;
    private HorizontalRList mainList=new HorizontalRList( GL2F.getDefWidth(),GL2F.getDefHeight(),0,0 );
    private Text mainText=new Text(15*pke(),"Game Paused");
    private IconButton endButton,returnButton;
    private SaveData data;
    private int countDown=3;
    private TimingNode countDownTiming;

    public PauseMenu(SaveData sd){

        addEventHandler("renderPause",(e)->{
            Context.acContext.getSharedPreferences("gameData", MODE_PRIVATE).edit()
                    .putString("savedGameState",data.toString()).apply();
        });

        data=sd;
        mainList.addElement( new Dummy() );
        mainList.addElement( mainText );
        VerticalRList bh= new VerticalRList(  );
        mainList.addElement( bh );
        bh.addElement( endButton=new IconButton( 25*pke(),25*pke(),0x1,()->{
            mainList.remove();
            disconnect();
            callEvent( "mooveBouncer",null );
            new Menu();
        } ) );
        bh.addElement( returnButton=new IconButton( 40*pke(),40*pke(),0x03c,()->{
            endButton.remove();
            returnButton.remove();
            mainText.setText( ""+countDown );
            countDown--;
            mainText.getSizeMod().animateTo( 1.5f,countDownTime/4f,()->mainText.getSizeMod().animateTo( 1 ) );
            setTimeout( ()-> {
                mainList.remove();
                disconnect();
                callEvent( "fadeBouncer", null );
                new LineGame( data );
            },countDownTime*3);
            countDownTiming=setInterval( ()->{
                if(countDown==1)removeTimer( countDownTiming );
                mainText.setText( ""+countDown );
                countDown--;
                mainText.getSizeMod().animateTo( 1.5f,countDownTime/4f,()->mainText.getSizeMod().animateTo( 1 ) );
            },countDownTime );
        } ) );
        bh.addElement( new Dummy() );
        mainList.addElement( new Dummy() );
    }
}
