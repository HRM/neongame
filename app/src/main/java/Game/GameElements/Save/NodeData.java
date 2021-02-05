package Game.GameElements.Save;

public class NodeData {
    public int si,sj;
    public int val;
    public int extra;

    public NodeData(int si, int sj, int val, int extra) {
        this.si = si;
        this.sj = sj;
        this.val = val;
        this.extra = extra;
    }

    public NodeData(String data){
        String sep[]=data.split( "," );
        si=Integer.parseInt( sep[0] );
        sj=Integer.parseInt( sep[1] );
        val=Integer.parseInt( sep[2] );
        extra=Integer.parseInt( sep[3] );
    }

    @Override
    public String toString() {
        return ""+si+","+sj+","+val+","+extra;
    }
}
