package Game.GameElements.Sound;

import java.util.HashMap;

public class MarkToNoteConverter {
    private HashMap<String,Integer> conv=new HashMap<String, Integer>(  );

    public MarkToNoteConverter(){

        conv.put("C1",1 );
        conv.put("C#1",2 );
        conv.put("Db1",2 );
        conv.put("D1",3 );
        conv.put("D#1",4 );
        conv.put("Eb1",4 );
        conv.put("E1",5 );
        conv.put("F1",6 );
        conv.put("F#1",7 );
        conv.put("Gb1",7 );
        conv.put("G1",8 );
        conv.put("G#1",9 );
        conv.put("Ab1",9 );
        conv.put("A1",10 );
        conv.put("A#1",11 );
        conv.put("Bb1",11 );
        conv.put("B1",12 );

        conv.put("C2",13 );
        conv.put("C#2",14 );
        conv.put("Db2",14 );
        conv.put("D2",15 );
        conv.put("D#2",16 );
        conv.put("Eb2",16 );
        conv.put("E2",17 );
        conv.put("F2",18);
        conv.put("F#2",19 );
        conv.put("Gb2",19 );
        conv.put("G2",20 );
        conv.put("G#2",21 );
        conv.put("Ab2",21 );
        conv.put("A2",22 );
        conv.put("A#2",23 );
        conv.put("Bb2",23 );
        conv.put("B2",24 );

    }

    public int convert(String in){
        if(conv.containsKey( in )){
            return conv.get(in);
        }else{
            return 0;
        }
    }
    public int[] convertChord(String in){
        int[] ret;
        in=in.replaceAll("(^\\s+)|(\\s+$)","");
        String[] chords=in.split( "\\s+" );
        ret=new int[chords.length];
        for(int i=0;i<chords.length;i++){
            ret[i]=convert( chords[i] );
        }
        return ret;
    }
}
