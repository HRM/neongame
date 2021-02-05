package Engine.Util;

public class Interpolation {
    public static float[] LinearInterpolationMD(float[] a,float[] b,float f,float[] out){
        for(int i=0;i<a.length;++i) {
            out[i] = f * b[i] + (1f - f) * a[i];
        }
        return out;
    }

    public static float LinearInterpolation(float a,float b,float f){
        return f*b+(1f-f)*a;
    }

    public static float BezierInterpolation(float a, float b, float av, float bv, float f){
        return (float) (a*Math.pow(1f-f,3f)+3f*av*Math.pow((1f-f),2f)*f+3f*bv*(1f-f)*Math.pow(f,2f)+b*Math.pow(f,3f));
    }

    public static float[] BezierInterpolationMD(float[] a, float[] b, float av[], float bv[], float f,float[] out){

        for(int i=0;i<a.length;++i) {
            out[i] = (float) (a[i] * Math.pow( 1f - f, 3f ) + 3f * av[i] * Math.pow( (1f - f), 2f ) * f + 3f * bv[i] * (1f - f) * Math.pow( f, 2f ) + b[i] * Math.pow( f, 3f ));
        }

        return out;

    }
}
