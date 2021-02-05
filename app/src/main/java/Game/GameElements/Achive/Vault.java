package Game.GameElements.Achive;

import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Engine.Element;
import Game.EngieParts.UiEngine.ListLayout.UIList;
import Game.EngieParts.UiEngine.UiElements.BackgroundAwardPanel;
import Game.EngieParts.UiEngine.UiElements.NodeAwardPanel;
import tams.libbuild.R;

public class Vault  {
    private static ArrayList<Award> awards=new ArrayList<>();
    public static void init(){
        // Level 1
        awards.add( new Award( 1,new NodeOptionPack( 2 ,110,210,310) ) );
        awards.add( new Award( 1,new BackgroundOptionPack( R.drawable.hex_gr,0, Element.pke()*128 ) ));
        awards.add( new Award( 1,new BackgroundOptionPack( R.drawable.brick_gr,0, Element.pke()*128 ) ));
        awards.add( new Award( 1,new BackgroundOptionPack( R.drawable.camu_gr,0, Element.pke()*128 ) ));
        awards.add( new Award( 1,new BackgroundOptionPack( R.drawable.rock_gr,0, Element.pke()*128 ) ));
        awards.add( new Award( 1,new BackgroundOptionPack( R.drawable.fabric_gr,0, Element.pke()*64 ) ));
        // Level 2
        awards.add( new Award( 2,new NodeOptionPack( 2 ,33,71,183) ) );
        // Level 3
        awards.add( new Award( 3,new NodeOptionPack( 2 ,360,235,57) ) );
        // Level 4
        awards.add( new Award( 4,new NodeOptionPack( 2 ,100+360,200+360,300+360) ) );
        // Level 5
        awards.add( new Award( 5,new NodeOptionPack( 2 ,100+360*2,200+360*2,300+360*2) ) );
        // Level 6
        awards.add( new Award( 6,new NodeOptionPack( 2 ,100+360*3,200+360*3,300+360*3) ) );
        // Level 7
        awards.add( new Award( 7,new NodeOptionPack( 1 ,100+360*4,200+360*4,300+360*4) ) );
        // Level 8
        awards.add( new Award( 8,new NodeOptionPack( 1 ,100+360*5,200+360*6,300+360*7) ) );
        // Level 9
        awards.add( new Award( 9,new NodeOptionPack( 1 ,100+360*8,200+360*9,300+360*10) ) );
        // Level 10
        awards.add( new Award( 10,new NodeOptionPack( 1 ,100+360*11,200+360*12,300+360*13) ) );
        // Level 11
        awards.add( new Award( 11,new NodeOptionPack( 1 ,100+360*14,200+360*15,300+360*16) ) );
        // Level 12
        awards.add( new Award( 12,new NodeOptionPack( 1 ,100+360*16,200+360*16,300+360*16) ) );

        Collections.sort( awards,(s1,s2)->{return s1.Quota-s2.Quota;} );

    }
    public static ArrayList<Award> getAwards(){
        return (ArrayList<Award>)awards.clone();
    }

    public static ArrayList<Award> getAwardsAtQuota(int q){
        ArrayList<Award> ret=new ArrayList<Award>(  );
        for(Award a : awards){
            if(a.Quota==q){
                ret.add( a );
            }
        }
        return ret;
    }

    public static ArrayList<Award> getAwardsAtQuota(int qmin, int qmax){
        ArrayList<Award> ret=new ArrayList<Award>(  );
        for(Award a : awards){
            if(a.Quota>=qmin&&a.Quota<=qmax){
                ret.add( a );
            }
        }
        return ret;
    }

    public static void addToUi(List<Award> aws, UIList ui){
        for(Award aw : aws){
            switch (aw.getAwardType()){
                case 1:
                    ui.addElement( new NodeAwardPanel(aw) );
                    break;
                case 2:
                    ui.addElement( new BackgroundAwardPanel( aw ) );
                    break;
            }
        }
    }
}
