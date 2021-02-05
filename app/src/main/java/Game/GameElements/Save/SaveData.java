package Game.GameElements.Save;

import java.util.LinkedList;

public class SaveData {
    public LinkedList<NodeData> nodes=new LinkedList<>();
    public int score=0;

    public SaveData(){

    }
    public SaveData(String data){
        String sp1[]=data.split( "S" );
        score=Integer.parseInt(sp1[0]);
        String nodeData[]=sp1[1].split( "N" );
        for(int i=0;i<nodeData.length;i++){
            nodes.addLast( new NodeData( nodeData[i] ));
        }
    }

    @Override
    public String toString() {
        String ret=""+score+"S";
        boolean first=true;
        for(NodeData nd :nodes){
            if(first){
                ret+=nd;
                first=false;
            }else{
                ret+="N"+nd;
            }
        }

        return ret;
    }
}
