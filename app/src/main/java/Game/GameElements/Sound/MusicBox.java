package Game.GameElements.Sound;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import Engine.Context;
import Engine.Element;
import Game.GameEvents.IntEvent;
import tams.libbuild.R;

public class MusicBox extends Element {

    private class SoundRunner implements Runnable{
        int soundID; float leftVolume; float rightVolume; int priority; int loop; float rate;

        public SoundRunner(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) {
            this.soundID = soundID;
            this.leftVolume = leftVolume;
            this.rightVolume = rightVolume;
            this.priority = priority;
            this.loop = loop;
            this.rate = rate;
        }

        @Override
        public void run() {
            soundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
        }
    }

    private ChordSet chords=new ChordSet();
    private Random rand=new Random(  );
    private int activeChord=rand.nextInt(chords.getChordSize());
    private ArrayList<Integer> sound=new ArrayList();
    private int gameLostSound,tapSound,fillSound,levelUpSound,powerUpBip;
    SoundPool soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);

    public MusicBox(){
        sound.add(soundPool.load( Context.acContext, R.raw.s1,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s2,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s3,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s4,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s5,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s6,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s7,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s8,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s9,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s10,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s11,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s12,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s13,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s14,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s15,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s16,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s17,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s18,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s19,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s20,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s21,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s22,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s23,1 ));
        sound.add(soundPool.load( Context.acContext, R.raw.s24,1 ));
        tapSound=soundPool.load( Context.acContext, R.raw.tap,1 );
        fillSound=soundPool.load( Context.acContext, R.raw.fillbip,1 );
        levelUpSound=soundPool.load( Context.acContext, R.raw.lubip,1 );
        gameLostSound=soundPool.load( Context.acContext, R.raw.lobip,1 );
        powerUpBip=soundPool.load( Context.acContext, R.raw.pubip,1 );

        addEventHandler( "playNoteFromChord",(e)->{
            IntEvent ie=(IntEvent)e;
            playNoteFromChord( activeChord,ie.getVal() );
        } );
        addEventHandler( "newChord",(e)->{activeChord=rand.nextInt(chords.getChordSize());} );
        addEventHandler( "playLostSound",(e)->{
            playOnThread(gameLostSound,0.7f,0.7f,1,0,1);
        } );
        addEventHandler( "playFillSound",(e)->{
            playOnThread(fillSound,0.7f,0.7f,1,0,1);
        } );
        addEventHandler( "playUpSound",(e)->{
            playOnThread(levelUpSound,0.3f,0.3f,1,0,1);
        } );
        addEventHandler( "playTapSound",(e)->{
            playOnThread(tapSound,1,1,1,0,1);
        } );
        addEventHandler( "playPupSound",(e)->{
            playOnThread(powerUpBip,0.2f,0.2f,1,0,1);
        } );
        addEventHandler("renderPause",(e)->{
            Log.i("AudioEFX","Audio Release");
            soundPool.release();
        });

    }

    public void playOnThread(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate){
        (new Thread(new SoundRunner( soundID, leftVolume, rightVolume, priority, loop, rate))).start();
    }

    public void playNoteFromChord(int ch,int nt){
        playOnThread(sound.get(chords.getNote( ch,nt )-1),0.1f,0.1f,1,0,1);
    }

    public void playNote(int n){
        playOnThread(sound.get(n),0.3f,0.3f,1,0,1);
    }

    @Override
    protected void disconnect() {
        soundPool.release();
        super.disconnect();
    }
}
