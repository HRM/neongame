package Game.Directors;

import java.util.ArrayList;
import java.util.LinkedList;

import Engine.Animation.AnimCallbackHandle;
import Engine.Animation.AnimFloat;
import Engine.Element;
import Engine.Timing.TimingHandler;
import Engine.Timing.TimingNode;
import ExtGl2Lib.GL2F;
import Game.EngieParts.AdEngine.AdHandler;
import Game.EngieParts.UiEngine.ListLayout.HorizontalERList;
import Game.EngieParts.UiEngine.ListLayout.HorizontalRList;
import Game.EngieParts.UiEngine.ListLayout.VerticalRList;
import Game.EngieParts.UiEngine.UiElements.Bar;
import Game.EngieParts.UiEngine.UiElements.Button;
import Game.EngieParts.UiEngine.UiElements.Dummy;
import Game.EngieParts.UiEngine.UiElements.IconButton;
import Game.EngieParts.UiEngine.UiElements.ProgressText;
import Game.EngieParts.UiEngine.UiElements.SpaceDefiner;
import Game.EngieParts.UiEngine.UiElements.Text;
import Game.GameElements.Achive.Award;
import Game.GameElements.Achive.Vault;
import Game.GameElements.util.UpWave;
import Game.PlayerData;

public class AfterGameMenu extends Element {
    public static int levelProgressMultipler=100;

    int score;
    int scoreQuota=PlayerData.pointsCollected*levelProgressMultipler;
    int playerLevel=PlayerData.pointsCollected;

    private HorizontalRList mainList=new HorizontalRList(GL2F.getDefWidth(),GL2F.getDefHeight(),0,0);
    private ProgressText mainScoreText=new ProgressText( 15*pke(),0 );
    private ProgressText barProgressText=new ProgressText( 10*pke(),PlayerData.currentProgress,PlayerData.pointsCollected*levelProgressMultipler );
    private Bar progressBar=new Bar(4*pke(),levelProgressMultipler*pke(),((float)PlayerData.currentProgress)/(PlayerData.pointsCollected*levelProgressMultipler));
    private HorizontalERList unlocks=new HorizontalERList( 25*pke(),10*pke() );
    private Text levelUpText=new Text(8*pke(),"nothing");
    private Text unlocked=new Text(8*pke(),"Unlocked:");
    private TimingNode owerflow;
    public TimingHandler loadLevels=()->{
        if(score>=scoreQuota){
            callEvent( "playUpSound",null );
            new UpWave();
            score-=scoreQuota;
            scoreQuota+=levelProgressMultipler;
            playerLevel+=1;
            ArrayList<Award> un=Vault.getAwardsAtQuota( playerLevel );
            levelUpText.getVisibility().animateTo( 0,0.25f,()->{
                levelUpText.getSizeMod().animateTo( 1,0.25f );
                levelUpText.getVisibility().animateTo( 1,0.25f);
                levelUpText.setText( "Reached level "+playerLevel );
            } );
            levelUpText.getSizeMod().animateTo( 0,0.25f );
            progressBar.getFullness().setVal( 0 );
            progressBar.getFullness().animateTo( Math.min(1f,((float)score)/(scoreQuota)),2f );
            barProgressText.getProgress().setVal( 0 );
            barProgressText.getProgress().animateTo( Math.min(score,scoreQuota),2f  );
            barProgressText.setProgressMargin( scoreQuota );
            if(un.size()>0&&unlocked.getVisibility().getVal()==0){
                unlocked.getxPosMod().setVal( 10*pke() );
                unlocked.getxPosMod().animateTo( 0,0.5f );
                unlocked.getVisibility().animateTo( 1,0.5f );
            }
            Vault.addToUi( un,unlocks );
        }else{
            removeTimer( owerflow );
        }
    };


    public AfterGameMenu(int score) {

        this.score=score+PlayerData.currentProgress;
        mainList.addElement( mainScoreText );

        SpaceDefiner pBarText=new SpaceDefiner( GL2F.getDefWidth(),10*pke()+6*pke() );
        HorizontalRList pBarTextSeparator=new HorizontalRList(  );
        pBarText.setBlock( pBarTextSeparator );

        pBarTextSeparator.addElement( progressBar );
        pBarTextSeparator.addElement( barProgressText );

        mainList.addElement( pBarText );

        VerticalRList buttonholder=new VerticalRList(  );
        buttonholder.addElement( new IconButton( 25*pke(),25*pke(),0x1,()->{AdHandler.showAd(()->{mainList.remove();disconnect();new Menu();callEvent( "mooveBouncer",null );});} ) );
        buttonholder.addElement( new IconButton( 25*pke(),25*pke(),0x076,()->{AdHandler.showAd(()->{mainList.remove();disconnect();new LineGame();callEvent( "fadeBouncer",null );});} ) );

        mainList.addElement( buttonholder );

        mainScoreText.getProgress().animateTo( score,2f );

        progressBar.getFullness().animateTo( Math.min(1f,((float)(this.score))/(PlayerData.pointsCollected*levelProgressMultipler)),2f );
        barProgressText.getProgress().animateTo( Math.min(this.score,scoreQuota),2f  );

        owerflow=setInterval( loadLevels,2f );

        int addtoPoints=score+PlayerData.currentProgress;

        boolean addAchivepanel=false;
        boolean levelAdded=addtoPoints>PlayerData.pointsCollected*levelProgressMultipler;

        while(addtoPoints>PlayerData.pointsCollected*levelProgressMultipler){
            addtoPoints-=PlayerData.pointsCollected*levelProgressMultipler;
            PlayerData.pointsCollected+=1;
            if(Vault.getAwardsAtQuota( PlayerData.pointsCollected ).size()>0)addAchivepanel=true;
        }
        PlayerData.currentProgress=addtoPoints;

        if(levelAdded){
            mainList.addElement( new Dummy(),0.25f );
            mainList.addElement( levelUpText,0.5f );
            levelUpText.getSizeMod().enableBez( 0,1 );
            levelUpText.getVisibility().enableBez( 0,1 );
            levelUpText.getVisibility().setVal( 0 );
            levelUpText.getSizeMod().setVal( 0 );
        }
        if(addAchivepanel){
            mainList.addElement( unlocked );
            unlocked.getVisibility().setVal( 0 );
            mainList.addElement( unlocks,3 );
            unlocks.setFlashing( true );
        }else{
            unlocks.remove();
            mainList.addElement( new Dummy(),2 );
            mainList.addElementLast( new Dummy(),2 );
            unlocked.remove();
        }

    }
}
