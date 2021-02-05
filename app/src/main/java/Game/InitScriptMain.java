package Game;


import android.content.SharedPreferences;

import Engine.Context;
import Engine.InitScript;
import Game.Directors.LineGameStarter;
import Game.Directors.Menu;
import Game.Directors.PauseMenu;
import Game.EngieParts.GraphEngine.GraphicalEngine;
import Game.GameElements.Achive.Vault;
import Game.GameElements.Backgrounds.Tier1Background;
import Game.GameElements.GUI.FontSet;
import Game.GameElements.Save.SaveData;
import Game.GameElements.Sound.MusicBox;
import Game.GameElements.util.LightBouncer;

import static android.content.Context.MODE_PRIVATE;


public class InitScriptMain implements InitScript{

    @Override
    public void init() {
        FontSet.init();

        new Tier1Background();
        new GraphicalEngine();
        new LightBouncer( 10 );
        new MusicBox();

        SharedPreferences sharedPref=Context.acContext.getSharedPreferences("gameData", MODE_PRIVATE);
        String gameState=sharedPref.getString("savedGameState","");
        if (gameState!=""){
            sharedPref.edit().putString("savedGameState","").apply();
            new PauseMenu(new SaveData(gameState));
        }
        else {
            //new Menu();
            new LineGameStarter();
        }

       // new DebugPanel("ver 1.4 for testing only",5);

    }

    @Override
    public void initOnce() {
        Vault.init();
    }
}
