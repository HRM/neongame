package Game.EngieParts.FontEngine;

import android.graphics.Bitmap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ExtGl2Lib.GL2F;
import ExtGl2Lib.Resource.Interfaces.Texture;
import ExtGl2Lib.Resource.TextureRes;

public class Font {
    private int charCodeOffset=0;
    private Char[] chars=new Char[300];
    private Texture[] pages;
    private float fontSize;

    private float[] tempPos=new float[]{0,0};
    private float[] tempSize=new float[]{0,0};
    private float[] temptxPos=new float[]{0,0};
    private float[] temptxSize=new float[]{0,0};

    public Font(InputStream fontFile, Bitmap ... pages) {

        this.pages=new Texture[pages.length];

        for(int i=0;i<pages.length;i++){
            this.pages[i]=new TextureRes( pages[i],true,false );
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse( fontFile );
            doc.getDocumentElement().normalize();

            Node info=doc.getElementsByTagName( "info" ).item( 0 );
            if(info.getNodeType()==Node.ELEMENT_NODE){
                fontSize=Integer.parseInt(((Element)info).getAttribute( "size" ));
            } else{
                fontSize=100;
            }

            NodeList charsNodes=doc.getElementsByTagName( "char" );

            boolean offsetChecked=false;

            for(int i=0;i<charsNodes.getLength();i++){
                if(charsNodes.item( i ).getNodeType() == Node.ELEMENT_NODE){
                    Element character=(Element) charsNodes.item( i );
                    int id=Integer.parseInt(character.getAttribute( "id" ));
                    int x=Integer.parseInt(character.getAttribute( "x" ));
                    int y=Integer.parseInt(character.getAttribute( "y" ));
                    int width=Integer.parseInt(character.getAttribute( "width" ));
                    int height=Integer.parseInt(character.getAttribute( "height" ));
                    int xoffset=Integer.parseInt(character.getAttribute( "xoffset" ));
                    int yoffset=Integer.parseInt(character.getAttribute( "yoffset" ));
                    int xadvance=Integer.parseInt(character.getAttribute( "xadvance" ));
                    int page=Integer.parseInt(character.getAttribute( "page" ));

                    if(!offsetChecked){
                        charCodeOffset=id;
                        offsetChecked=true;
                    }

                    chars[id-charCodeOffset]=new Char( new int[]{x,y},new int[]{width,height},new int[]{xoffset,yoffset},xadvance,page);
                }
            }

            NodeList kernNodes=doc.getElementsByTagName( "kerning" );
            for(int i=0;i<kernNodes.getLength();i++){
                if(kernNodes.item( i ).getNodeType() == Node.ELEMENT_NODE) {
                    Element kerning = (Element) kernNodes.item( i );
                    int first=Integer.parseInt( kerning.getAttribute( "first" ) );
                    int second=Integer.parseInt( kerning.getAttribute( "second" ) );
                    int amount=Integer.parseInt( kerning.getAttribute( "amount" ) );
                    this.chars[first-charCodeOffset].AddKerning( new Kerning( second,amount ) );
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(){
        for(int i=0;i<pages.length;i++){
            pages[i].del();
        }
    }

    public void drawChar(char c,float[] pos,float size){
        if(c-charCodeOffset>=chars.length&&c-charCodeOffset<0)return;
        Char ch=chars[c-charCodeOffset];
        float ratio=(float)ch.getTxSize()[0]/(float)ch.getTxSize()[1];
        PartialTextureHelper.drawPartialTexture( pages[ch.getPage()],pos,new float[]{ratio*size/2f,size/2f},0, GL2F.getCol(),new float[]{ch.getTxPos()[0],ch.getTxPos()[1]},new float[]{ch.getTxSize()[0],ch.getTxSize()[1]} );
    }

    public void drawCharAbsVu(int c,float[] pos,float size){
        if(c>=chars.length||c<0)return;
        Char ch=chars[c];
        float ratio=(float)ch.getTxSize()[0]/(float)ch.getTxSize()[1];
        PartialTextureHelper.drawPartialTexture( pages[ch.getPage()],pos,new float[]{ratio*size/2f,size/2f},0, GL2F.getCol(),new float[]{ch.getTxPos()[0],ch.getTxPos()[1]},new float[]{ch.getTxSize()[0],ch.getTxSize()[1]} );
    }
    public float getAbsLength(int c,float size){
        if(c>=chars.length||c<0)return 1;
        float ratio=(float)chars[c].getTxSize()[0]/(float)chars[c].getTxSize()[1];
        return ratio*size;
    }

    public float getLiength(String s,float size){
        float relativeSize=size/fontSize;

        float totalLength=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt( i )-charCodeOffset<chars.length&&s.charAt( i )-charCodeOffset>=0)
            totalLength+=chars[s.charAt( i )-charCodeOffset].getTxSize()[1];
        }
        return totalLength*relativeSize;
    }

    public void drawString(String s,float[] pos,float size,int xOrigin,int yOrigin){
        float relativeSize=size/fontSize;

        float totalLength=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt( i )-charCodeOffset<chars.length&&s.charAt( i )-charCodeOffset>=0)
            totalLength+=chars[s.charAt( i )-charCodeOffset].getAdvance();
        }

        float advancement;

        switch(xOrigin) {
            case -1:
                advancement=-totalLength;
                break;
            case 1:
                advancement=0;
                break;
            default:
                advancement = -totalLength / 2f;
        }

        float yGlobalOffset;

        switch (yOrigin){
            case -1:
                yGlobalOffset=fontSize/2f;
                break;
            case 1:
                yGlobalOffset=-fontSize/2f;
                break;
            default:
                yGlobalOffset=0;
        }

        Char prevChar=null;


        for(int i=0;i<s.length();i++){
            if(s.charAt( i )-charCodeOffset>=chars.length||s.charAt( i )-charCodeOffset<0)continue;
            Char ch=chars[s.charAt( i )-charCodeOffset];

            int kerning=prevChar==null?0:prevChar.getKerning( s.charAt( i ) );

            advancement+=ch.getAdvance()/2f;

            tempPos[0]=pos[0]+(advancement+ch.getOffset()[0]+kerning)*relativeSize;
            tempPos[1]=pos[1]+(yGlobalOffset-ch.getOffset()[1]+(fontSize-ch.getTxSize()[1])/2f)*relativeSize;

            tempSize[0]=ch.getTxSize()[0]*relativeSize/2f;
            tempSize[1]=ch.getTxSize()[1]*relativeSize/2f;

            temptxPos[0]=ch.getTxPos()[0];
            temptxPos[1]=ch.getTxPos()[1];

            temptxSize[0]=ch.getTxSize()[0];
            temptxSize[1]=ch.getTxSize()[1];

            PartialTextureHelper.drawPartialTexture( pages[ch.getPage()],tempPos,tempSize,0, GL2F.getCol(),temptxPos,temptxSize );

            advancement+=ch.getAdvance()/2f;

            prevChar=ch;
        }
    }

    public void drawStringSuperCentral(String s,float[] pos,float size,int xOrigin,int yOrigin){
        float relativeSize=size/fontSize;

        float totalLength=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt( i )-charCodeOffset<chars.length&&s.charAt( i )-charCodeOffset>=0)
            totalLength+=chars[s.charAt( i )-charCodeOffset].getTxSize()[0];
        }

        float advancement;

        switch(xOrigin) {
            case -1:
                advancement=-totalLength;
                break;
            case 1:
                advancement=0;
                break;
            default:
                advancement = -totalLength / 2f;
        }

        float yGlobalOffset;

        switch (yOrigin){
            case -1:
                yGlobalOffset=fontSize/2f;
                break;
            case 1:
                yGlobalOffset=-fontSize/2f;
                break;
            default:
                yGlobalOffset=0;
        }

        Char prevChar=null;


        for(int i=0;i<s.length();i++){
            if(s.charAt( i )-charCodeOffset>=chars.length||s.charAt( i )-charCodeOffset<0)continue;
            Char ch=chars[s.charAt( i )-charCodeOffset];

            int kerning=0;

            advancement+=ch.getTxSize()[0]/2f;

            tempPos[0]=pos[0]+(advancement+ch.getOffset()[0]+kerning)*relativeSize;
            tempPos[1]=pos[1]+(yGlobalOffset-ch.getOffset()[1]+(fontSize-ch.getTxSize()[1])/2f)*relativeSize;

            tempSize[0]=ch.getTxSize()[0]*relativeSize/2f;
            tempSize[1]=ch.getTxSize()[1]*relativeSize/2f;

            temptxPos[0]=ch.getTxPos()[0];
            temptxPos[1]=ch.getTxPos()[1];

            temptxSize[0]=ch.getTxSize()[0];
            temptxSize[1]=ch.getTxSize()[1];

            PartialTextureHelper.drawPartialTexture( pages[ch.getPage()],tempPos,tempSize,0, GL2F.getCol(),temptxPos,temptxSize );

            advancement+=ch.getTxSize()[0]/2f;

            prevChar=ch;
        }
    }
}

class Char{
    private final int[] txPos;
    private final int[] txSize;
    private final int[] offset;
    private final int advance;
    private final int page;
    private final LinkedList<Kerning> kernings=new LinkedList<>( );

    public Char(int[] txPos, int[] txSize, int[] offset, int advance, int page) {
        this.txPos = txPos;
        this.txSize = txSize;
        this.offset = offset;
        this.advance = advance;
        this.page = page;
    }

    public int[] getTxPos() {
        return txPos;
    }

    public int[] getTxSize() {
        return txSize;
    }

    public int[] getOffset() {
        return offset;
    }

    public int getAdvance() {
        return advance;
    }

    public int getPage() {
        return page;
    }

    public void AddKerning(Kerning k){
        kernings.add( k );
    }
    public int getKerning(char c){
        Kerning ret=null;
        for(Iterator<Kerning> it=kernings.listIterator(  );it.hasNext()&&ret==null;){
            Kerning t=it.next();
            if(t.getCharId()==c){
                ret=t;
            }
        }
        return ret==null?0:ret.getOffset();
    }
}

class Kerning{
    private final int CharId;
    private final int Offset;

    public Kerning(int charId, int offset) {
        CharId = charId;
        Offset = offset;
    }

    public int getCharId() {
        return CharId;
    }

    public int getOffset() {
        return Offset;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Kerning){
            Kerning inst=(Kerning) obj;
            return inst.CharId==CharId&&inst.Offset==inst.Offset;
        }
        else return super.equals( obj );
    }

    @Override
    public int hashCode() {
        return CharId^Offset;
    }
}