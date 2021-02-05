package Game.EngieParts.GraphEngine;

import Engine.Element;
import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.TextureResTar;

public class GraphicalEngine extends Element{
    int lightDownsampling;
    TextureResTar lightTarget;
    public GraphicalEngine(){
        if(GL2F.getDefWidth()>=960)lightDownsampling=2;
        else lightDownsampling=1;

        lightTarget=new TextureResTar((int) GL2F.getDefWidth()/lightDownsampling,(int) GL2F.getDefHeight()/lightDownsampling);
        //target=new TextureResTar((int) GL2F.getDefWidth(),(int) GL2F.getDefHeight());
        addEventHandler("draw",(e)->onDraw());
    }
    private void onDraw(){

        callEvent("drawBL1",null);

        callEvent("drawBL2",null);

        callEvent("drawBL3",null);

        GL2F.EnableAdditiveBlending();

        GL2F.SetRenderTarget(lightTarget,lightDownsampling);
        GL2F.Clear(new float[]{0.1f,0.1f,0.1f,1.0f});
        callEvent("drawLight",null);
        GL2F.SetDefaultTarget();

        GL2F.EnableMultiplicativeBlending();
        GL2F.setFlipTexY(false);
        GL2F.drawTex(new float[]{GL2F.getDefWidth()/2f,GL2F.getDefHeight()/2f},new float[]{0,0},0, GL2F.COOP.V4ONES,lightTarget);
        GL2F.setFlipTexY(true);

        GL2F.EnableAlphaBlending();
        callEvent("drawL1",null);

        callEvent("drawL2",null);

        callEvent("drawL3",null);

        callEvent("drawL4",null);

        callEvent("debugDraw",null);

    }

    @Override
    protected void disconnect() {
        super.disconnect();
        lightTarget.del();
    }
}
