package ExtGl2Lib.Shader.Elder;

import android.opengl.GLES20;

import java.nio.Buffer;
import java.nio.ShortBuffer;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ExtGl2Lib.Helpers.ShaderHelper;

public class Shader{
    //HELPER FUNCTIONS
    private static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private final static LinkedList<VarData> getVars(String code) {
        LinkedList<VarData> ret=new LinkedList<>();
        Pattern a= Pattern.compile("(attribute|uniform)\\s+(\\w+)\\s+(\\w+)(?:\\[(\\d+)\\])?\\s*;");
        Matcher m= a.matcher(code);
        while(m.find()) {
            ret.add(new VarData(m.group(1),m.group(2),m.group(3),m.group(4)));
        }
        return ret;
    }

    private final static LinkedList<String> getIncludeList(String code){
        LinkedList<String> ret=new LinkedList<>();
        Pattern a= Pattern.compile("#\\s*include\\s*<\\s*(\\w+)\\s*>");
        Matcher m= a.matcher(code);
        while(m.find()) {
            ret.add(m.group(1));
        }
        return ret;
    }

    private final static String ProcessIncludes(String code,IncludeTable it){
        LinkedList<String> includes=getIncludeList(code);
        String ret=code;
        for(String i:includes){
            ret=ret.replaceAll( "#\\s*include\\s*<\\s*"+i+"\\s*>",it.getCode( i ) );
        }
        return ret;
    }
    //PRIVATE VARIABLES
    private int mProgram;

    private Hashtable<String,VarData> unifomData=new Hashtable();
    private LinkedList<VarData> uniformDataListed=new LinkedList();

    private Hashtable<String,VarData> attributeData=new Hashtable();
    private LinkedList<VarData> attributeDataListed=new LinkedList();

    //CONSTRUCTOR

    public Shader(String vShader,String fShader,IncludeTable includes){
        if(includes!=null) {
            vShader = ProcessIncludes( vShader, includes );
            fShader = ProcessIncludes( fShader, includes );
        }

        LinkedList<VarData> vsVars=getVars(vShader);
        LinkedList<VarData> psVars=getVars(fShader);

        LinkedHashSet<VarData> Vars=new LinkedHashSet<>(  );
        Vars.addAll( vsVars );
        Vars.addAll( psVars );

        int vertexShader = ShaderHelper.loadShader( GLES20.GL_VERTEX_SHADER,vShader);
        int fragmentShader = ShaderHelper.loadShader(GLES20.GL_FRAGMENT_SHADER,fShader);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        GLES20.glDetachShader(mProgram,vertexShader);
        GLES20.glDetachShader(mProgram,fragmentShader);
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        for(VarData vd:Vars){
            if(vd.isAttribute()){
                vd.setLocation(GLES20.glGetAttribLocation(mProgram, vd.getVarName()));
                vd.deepAnalyze();
                attributeData.put( vd.getVarName(),vd );
                attributeDataListed.add(vd);
            }else{
                vd.setLocation( GLES20.glGetUniformLocation( mProgram,vd.getVarName() ) );
                unifomData.put( vd.getVarName(),vd );
                uniformDataListed.add(vd);
            }
        }
    }

    public Shader(String vShader,String fShader){
        this(vShader,fShader,null);
    }
    //SHADER MANAGEMENT

    public void EnableShader(){
        GLES20.glUseProgram( mProgram );
        for(VarData vd:attributeDataListed){
            GLES20.glEnableVertexAttribArray( vd.getLocation() );
        }
    }
    public void DisableShader(){
        for(VarData vd:attributeDataListed){
            GLES20.glDisableVertexAttribArray( vd.getLocation() );
        }
    }

    //UNIFORM SETTERS

    public boolean setUniform1f(String vname,float v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "float" )){
            GLES20.glUniform1f( uniData.getLocation(),v );
            return true;
        }
        return false;
    }

    public boolean setUniform2f(String vname,float v1,float v2){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec2" )){
            GLES20.glUniform2f( uniData.getLocation(),v1,v2 );
            return true;
        }
        return false;
    }

    public boolean setUniform3f(String vname,float v1,float v2,float v3){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec3" )){
            GLES20.glUniform3f( uniData.getLocation(),v1,v2,v3 );
            return true;
        }
        return false;
    }

    public boolean setUniform4f(String vname,float v1,float v2,float v3,float v4){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec4" )){
            GLES20.glUniform4f( uniData.getLocation(),v1,v2,v3,v4 );
            return true;
        }
        return false;
    }

    public boolean setUniform1i(String vname,int v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "int" )){
            GLES20.glUniform1i( uniData.getLocation(),v );
            return true;
        }
        return false;
    }

    public boolean setUniform2i(String vname,int v1,int v2){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec2" )){
            GLES20.glUniform2i( uniData.getLocation(),v1,v2 );
            return true;
        }
        return false;
    }

    public boolean setUniform3i(String vname,int v1,int v2,int v3){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec3" )){
            GLES20.glUniform3i( uniData.getLocation(),v1,v2,v3 );
            return true;
        }
        return false;
    }

    public boolean setUniform4i(String vname,int v1,int v2,int v3,int v4){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec4" )){
            GLES20.glUniform4i( uniData.getLocation(),v1,v2,v3,v4 );
            return true;
        }
        return false;
    }

    public boolean setUniform2m(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat2" )){
            GLES20.glUniformMatrix2fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform3m(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat3" )){
            GLES20.glUniformMatrix3fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform4m(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat4" )){
            GLES20.glUniformMatrix4fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform1b(String vname,boolean v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bool" )){
            GLES20.glUniform1i( uniData.getLocation(),v?1:0);
            return true;
        }
        return false;
    }

    public boolean setUniform2b(String vname,boolean v1,boolean v2){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec2" )){
            GLES20.glUniform2i( uniData.getLocation(),v1?1:0,v2?1:0 );
            return true;
        }
        return false;
    }

    public boolean setUniform3b(String vname,boolean v1,boolean v2,boolean v3){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec3" )){
            GLES20.glUniform3i( uniData.getLocation(),v1?1:0,v2?1:0,v3?1:0 );
            return true;
        }
        return false;
    }

    public boolean setUniform4b(String vname,boolean v1,boolean v2,boolean v3,boolean v4){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec4" )){
            GLES20.glUniform4i( uniData.getLocation(),v1?1:0,v2?1:0,v3?1:0,v4?1:0 );
            return true;
        }
        return false;
    }

    public boolean setUniformSampler(String vname,int tu){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "sampler2D" )||uniData.getVarType().equals( "samplerCube" )){
            GLES20.glUniform1i( uniData.getLocation(),tu );
            return true;
        }
        return false;
    }
    
    //UNIFORM ARRAY SETTERS

    public boolean setUniform1fArr(String vname,float[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "float" )&&uniData.isArray()){
            GLES20.glUniform1fv( uniData.getLocation(),v.length,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform2fArr(String vname,float[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec2" )&&uniData.isArray()){
            GLES20.glUniform2fv( uniData.getLocation(),v.length/2,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform3fArr(String vname,float[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec3" )&&uniData.isArray()){
            GLES20.glUniform3fv( uniData.getLocation(),v.length/3,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform4fArr(String vname,float[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "vec4" )&&uniData.isArray()){
            GLES20.glUniform4fv( uniData.getLocation(),v.length/4,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform1iArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "int" )&&uniData.isArray()){
            GLES20.glUniform1iv( uniData.getLocation(),v.length,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform2iArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec2" )&&uniData.isArray()){
            GLES20.glUniform2iv( uniData.getLocation(),v.length/2,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform3iArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec3" )&&uniData.isArray()){
            GLES20.glUniform3iv( uniData.getLocation(),v.length/3,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform4iArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "ivec4" )&&uniData.isArray()){
            GLES20.glUniform4iv( uniData.getLocation(),v.length/4,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform2mArr(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat2" )&&uniData.isArray()){
            GLES20.glUniformMatrix2fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform3mArr(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat3" )&&uniData.isArray()){
            GLES20.glUniformMatrix3fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform4mArr(String vname,float[] mat){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "mat4" )&&uniData.isArray()){
            GLES20.glUniformMatrix4fv( uniData.getLocation(),1,false,mat,0 );
            return true;
        }
        return false;
    }

    public boolean setUniform1bArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bool" )&&uniData.isArray()){
            GLES20.glUniform1iv( uniData.getLocation(),v.length,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform2bArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec2" )&&uniData.isArray()){
            GLES20.glUniform2iv( uniData.getLocation(),v.length/2,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform3bArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec3" )&&uniData.isArray()){
            GLES20.glUniform3iv( uniData.getLocation(),v.length/3,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniform4bArr(String vname,int[] v){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if(uniData.getVarType().equals( "bvec4" )&&uniData.isArray()){
            GLES20.glUniform4iv( uniData.getLocation(),v.length/4,v,0);
            return true;
        }
        return false;
    }

    public boolean setUniformSamplerArr(String vname,int[] tu){
        VarData uniData=unifomData.get( vname );
        if(uniData!=null)if((uniData.getVarType().equals( "sampler2D" )||uniData.getVarType().equals( "samplerCube" ))&&uniData.isArray()){
            GLES20.glUniform1iv( uniData.getLocation(),tu.length,tu,0);
            return true;
        }
        return false;
    }

    //ATTRIBUTE SETTERS

    public boolean setVertexAttribute(String vname, Buffer buffer){
        VarData atrData=attributeData.get( vname );
        if(atrData==null)return false;
        if(buffer==null){
            GLES20.glVertexAttribPointer(atrData.getLocation(), atrData.getCompCount(),
                    atrData.getAttribType(), false,
                    0, 0);
        }else{
            GLES20.glVertexAttribPointer(atrData.getLocation(), atrData.getCompCount(),
                    atrData.getAttribType(), false,
                    0, buffer);
        }
        return true;
    }
    public boolean setVertexAttribute(String vname,int vertexType, int coordPerVert, int vertexStride, Buffer buffer){
        VarData atrData=attributeData.get( vname );
        if(atrData==null)return false;
        if(buffer==null){
            GLES20.glVertexAttribPointer(atrData.getLocation(), coordPerVert,
                    vertexType, false,
                    vertexStride, 0);
        }else{
            GLES20.glVertexAttribPointer(atrData.getLocation(), coordPerVert,
                    vertexType, false,
                    vertexStride, buffer);
        }
        return true;
    }

    public boolean setVertexAttributeNormalized(String vname, Buffer buffer){
        VarData atrData=attributeData.get( vname );
        if(atrData==null)return false;
        if(buffer==null){
            GLES20.glVertexAttribPointer(atrData.getLocation(), atrData.getCompCount(),
                    atrData.getAttribType(), true,
                    0, 0);
        }else{
            GLES20.glVertexAttribPointer(atrData.getLocation(), atrData.getCompCount(),
                    atrData.getAttribType(), true,
                    0, buffer);
        }
        return true;
    }
    public boolean setVertexAttributeNormalized(String vname,int vertexType, int coordPerVert, int vertexStride, Buffer buffer){
        VarData atrData=attributeData.get( vname );
        if(atrData==null)return false;
        if(buffer==null){
            GLES20.glVertexAttribPointer(atrData.getLocation(), coordPerVert,
                    vertexType, true,
                    vertexStride, 0);
        }else{
            GLES20.glVertexAttribPointer(atrData.getLocation(), coordPerVert,
                    vertexType, true,
                    vertexStride, buffer);
        }
        return true;
    }

    //DRAW CALLS
        //draw arrays

    public void drawPointArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_POINTS, first, count);
    }

    public void drawLineArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_LINES, first, count);
    }

    public void drawLineStripArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, first, count);
    }

    public void drawLineLoopArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, first, count);
    }

    public void drawTriangleArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, first, count);
    }

    public void drawTriangleStripArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, first, count);
    }

    public void drawTriangleFanArrays(int first, int count){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, first, count);
    }
        //draw elements

    public void drawPointElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_POINTS, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_POINTS, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawLineElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_LINES, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_LINES, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawLineStripElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_LINE_STRIP, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_LINE_STRIP, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawLineLoopElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_LINE_LOOP, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_LINE_LOOP, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawTriangleElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawTriangleStripElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_TRIANGLE_STRIP, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    public void drawTriangleFanElemenents(int count, ShortBuffer indices){
        if(indices!=null) {
            GLES20.glDrawElements( GLES20.GL_TRIANGLE_FAN, count, GLES20.GL_UNSIGNED_SHORT, indices );
        } else{
            GLES20.glDrawElements( GLES20.GL_TRIANGLE_FAN, count, GLES20.GL_UNSIGNED_SHORT, 0 );
        }
    }

    //DELETE SHADER
    public void deleteShader(){
        GLES20.glDeleteProgram( mProgram );
        mProgram=0;
    }
}

class VarData{
    private final boolean attribute;
    private final boolean array;
    private final String varType;
    private final String varName;
    private final int arraySize;
    private int location=0;
    private int attribType=0;
    private int compCount=0;

    public VarData(String atr,String type,String name,String arr){
        varType=type;
        varName=name;
        if(arr!=null){
            arraySize=Integer.parseInt( arr );
            array=true;
        } else {
            array=false;
            arraySize=0;
        }
        attribute=atr.equals( "attribute" );
    }

    public void deepAnalyze(){
        if(varType.charAt( varType.length()-1 )<='9'&&varType.charAt( varType.length()-1 )>='0'){
            compCount=varType.charAt( varType.length()-1 )-'0';
        }else compCount=1;
        switch (varType.charAt( 0 ) ){
            case 'b':attribType=GLES20.GL_BYTE;break;
            case 'i':attribType=GLES20.GL_SHORT;break;
            case 'f':attribType=GLES20.GL_FLOAT;break;
            case 'v':attribType=GLES20.GL_FLOAT;break;
        }
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

    public boolean isAttribute() {
        return attribute;
    }

    public boolean isArray() {
        return array;
    }

    public String getVarType() {
        return varType;
    }

    public String getVarName() {
        return varName;
    }

    public int getArraySize() {
        return arraySize;
    }

    public int getAttribType() {
        return attribType;
    }

    public int getCompCount() {
        return compCount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VarData){
            return this.varName==((VarData)obj).getVarName();
        }
        else return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return varName.hashCode();
    }
}
