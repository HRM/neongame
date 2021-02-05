package Game.GameElements.util;

import java.util.ArrayList;

import Engine.Animation.AnimFloat;
import Engine.Element;
import Engine.Events.EventType.TickEvent;
import Engine.Timing.TimingNode;
import ExtGl2Lib.GL2F;

public class LightBouncer extends Element {
    private static float minSpeed=pke()*4;
    private static float speedDif=pke()*4;
    private static float refChance=0.2f;
    private static float refSpeed=0.1f;
    private AnimFloat visibility=getFloatAnimator(1f,1f);
    private AnimFloat speedMod=getFloatAnimator( 1f,1f );

    private ArrayList<float[]> positions=new ArrayList<>(  );
    private ArrayList<float[]> impulses= new ArrayList<>(  );
    private ArrayList<float[]> colors= new ArrayList<>(  );
    private ArrayList<AnimFloat> visibilities=new ArrayList<>(  );
    TimingNode iHateVariableNameing;

    private float[] impGenerator(int dir,float length){
        float[] ret=new float[]{0,0};
        float angle=(float)(Math.random()*Math.PI);
        float pi2=(float)Math.PI/2f;
        switch (dir){
            case 1:
                ret[0]=(float)Math.cos(angle);
                ret[1]=(float)Math.sin(angle);
                break;
            case 2:
                ret[0]=(float)Math.cos(angle+pi2);
                ret[1]=(float)Math.sin(angle+pi2);
                break;
            case 3:
                ret[0]=(float)Math.cos(angle+pi2*2);
                ret[1]=(float)Math.sin(angle+pi2*2);
                break;
            case 4:
                ret[0]=(float)Math.cos(angle+pi2*3);
                ret[1]=(float)Math.sin(angle+pi2*3);
                break;
        }
        ret[0]*=length;
        ret[1]*=length;
        return ret;
    }
    private void refreshPos(float timeFac){
        if(visibility.getVal()>0)
        for(int i=0;i<positions.size();i++){
            positions.get( i )[0]+=impulses.get( i )[0]*timeFac*speedMod.getVal();
            positions.get( i )[1]+=impulses.get( i )[1]*timeFac*speedMod.getVal();
            if(Math.abs(positions.get( i )[0])>=GL2F.getDefWidth()/2f){
                float ran=(float)Math.random()*minSpeed+speedDif;
                if(positions.get( i )[0]<0){
                    float[] newpend= impGenerator( 4,(float)Math.random()*minSpeed+speedDif );
                    impulses.set( i,newpend);
                    positions.get( i )[0]=-GL2F.getDefWidth()/2f;
                }else{
                    float[] newpend= impGenerator( 2,(float)Math.random()*minSpeed+speedDif );
                    impulses.set( i,newpend);
                    positions.get( i )[0]=GL2F.getDefWidth()/2f;
                }
            }
            if(Math.abs(positions.get( i )[1])>=GL2F.getDefHeight()/2f){
                if(positions.get( i )[1]<0){
                    float[] newpend= impGenerator( 1,(float)Math.random()*minSpeed+speedDif );
                    impulses.set( i,newpend);
                    positions.get( i )[1]=-GL2F.getDefHeight()/2f;
                }else{
                    float[] newpend= impGenerator( 3,(float)Math.random()*minSpeed+speedDif );
                    impulses.set( i,newpend);
                    positions.get( i )[1]=GL2F.getDefHeight()/2f;
                }
            }
        }
    }
    private void refreshVisibility(){
        if(visibility.getVal()>0)
        for(int i=0;i<visibilities.size();i++){
            if(Math.random()<refChance)
            visibilities.get( i ).animateTo( (float)Math.random() );
        }
    }
    private void hideAll(){
        for(int i=0;i<visibilities.size();i++){
            visibilities.get( i ).animateTo( 0,0.2f );
        }
    }
    private void init(int count){
        for(int i=0;i<count;i++){
            positions.add( new float[]{(float)Math.random()*GL2F.getDefWidth()-GL2F.getDefWidth()/2f,(float)Math.random()*GL2F.getDefHeight()-GL2F.getDefHeight()/2f} );
            impulses.add( impGenerator(i%4+1,(float)Math.random()*minSpeed+speedDif));
            AnimFloat t=getFloatAnimator( 0,1f );
            t.enableBez( 0,1 );
            visibilities.add( t );
            colors.add( GL2F.COOP.GetColorByHue( (float)Math.random()*360 ,1,new float[4]) );
        }
    }
    private void draw(){
        if(visibility.getVal()>0)
        for(int i=0;i<visibilities.size();i++){
            GL2F.setSqf( true );
            GL2F.setFill( false );
            GL2F.setSfe( false );
            GL2F.setThickness( pke()/4 );
            GL2F.setFade(50*pke()*2f);
            float[] fl1=new float[4];
            GL2F.setCol( GL2F.COOP.Multiply( colors.get( i ), visibilities.get( i ).getVal()*0.6f*visibility.getVal(), fl1 ) );
            GL2F.drawCircle( positions.get( i ),0 );
    }
    }
    public LightBouncer(int lc){
        init(lc);
        refreshVisibility();
        addEventHandler( "tick",(e)->{
            TickEvent te=(TickEvent)e;
            refreshPos( te.getDelta()/1000f );
        } );
        iHateVariableNameing=setInterval( ()->refreshVisibility(),refSpeed );
        addEventHandler( "drawLight",(e)->draw() );

        addEventHandler( "hideBouncer",(e)->{
            speedMod.animateTo( 10f,1f,()->{speedMod.setVal( 1 );} );
            visibility.animateTo( 0,1f );
        } );
        addEventHandler( "showBouncer",(e)->{
            speedMod.animateTo( 10f,0.5f,()->{speedMod.animateTo( 1f );} );
            visibility.animateTo( 1,1f );
        } );
        addEventHandler( "fadeBouncer",(e)->{
            speedMod.animateTo( 10f,0.5f,()->{speedMod.animateTo( 1f );} );
            visibility.animateTo( 0.3f,1f );
        } );
        addEventHandler( "mooveBouncer",(e)->{
            speedMod.animateTo( 10f,0.5f,()->{speedMod.animateTo( 1f );} );
        } );

    }
    public void remove(){
        removeTimer( iHateVariableNameing );
        hideAll();
        setTimeout( ()->disconnect(),0.2f );
    }


}
