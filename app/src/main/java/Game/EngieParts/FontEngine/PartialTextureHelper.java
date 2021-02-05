package Game.EngieParts.FontEngine;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;

public class PartialTextureHelper {
    public static void drawPartialTexture(Texture tex, float[] pos, float[] scale, float rot, float[] color, float[] tpos, float[] tscale){
        float[] realTPos=new float[]{tpos[0]/tex.getWidth(),tpos[1]/tex.getHeight()};
        float[] realTSca=new float[]{tscale[0]/tex.getWidth(),tscale[1]/tex.getHeight()};
        GL2F.setTexSca( realTSca );
        GL2F.setTexPos( realTPos );
        GL2F.drawTex(scale,pos,rot,color,tex);
        GL2F.setTexPos( new float[]{0,0} );
        GL2F.setTexSca( new float[]{1,1} );
    }
}
