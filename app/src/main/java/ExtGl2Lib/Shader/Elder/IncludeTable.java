package ExtGl2Lib.Shader.Elder;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncludeTable {
    Hashtable<String,String> includes=new Hashtable<>(  );
    public void addCode(String tag,String code){
        includes.put( tag,code );
    }
    public boolean addCode(String code){
        Pattern a= Pattern.compile("#\\s*export\\s*<\\s*(\\w+)\\s*>");
        Matcher m= a.matcher(code);
        String tag;
        if(m.find()) {
            tag = m.group( 1 );
        } else return false;
        String cCode=code.replaceAll( "#export\\s*<\\s*\\w+\\s*>\\s*\\n*" ,"");
        addCode( tag,cCode );
        return true;
    }
    public String getCode(String tag){
        return  includes.get( tag );
    }
}
