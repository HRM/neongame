package Game.GameElements.Sound;

import java.util.ArrayList;

public class ChordSet {
    private ArrayList<int[]> chords = new ArrayList();

    public ChordSet(){
        MarkToNoteConverter mtn=new MarkToNoteConverter();
        chords.add( mtn.convertChord( "Db1 F1 Ab1" ) );
        chords.add( mtn.convertChord( "Bb1 Db1 F1" ) );
        chords.add( mtn.convertChord( "Gb1 Db2 Bb2" ) );
        chords.add( mtn.convertChord( "Ab1 C2 Eb2 Gb2" ) );

        chords.add( mtn.convertChord( "C1 Eb1 G1" ) );
        chords.add( mtn.convertChord( "Eb1 F1 Bb1 D2" ) );
        chords.add( mtn.convertChord( "Ab1 C2 Eb2" ) );
        chords.add( mtn.convertChord( "C1 Eb1 G1" ) );
    }
    public int getNote(int ch, int n){
        int[] chord=chords.get( ch%chords.size() );
        int n1=n%(chord.length*2-2);
        int n2=n1>=chord.length?chord.length-2-(n1%chord.length):n1;
        return chord[n2];
    }
    public int getChordSize(){
        return chords.size();
    }
}
