package Game.GameElements.GUI;

import Engine.Element;
import Game.EngieParts.FontEngine.Font;
import tams.libbuild.R;

public class FontSet {
    public static Font defFont=null;
    public static Font neonFont=null;
    public static Font icons=null;
    public static void init() {
        defFont = new Font( Element.fS( R.raw.arial_sm ), Element.fB( R.drawable.arial_sm_0 ) );
        neonFont = new Font( Element.fS( R.raw.neon ), Element.fB( R.drawable.neon_0 ) );
        icons= new Font( Element.fS( R.raw.icons ), Element.fB( R.drawable.icons_0 ), Element.fB( R.drawable.icons_1 ), Element.fB( R.drawable.icons_2 ) );
    }
    public void delete(){
        defFont.delete();
        neonFont.delete();
        icons.delete();
    }

}
