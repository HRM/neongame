package activityes;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import Engine.Android.EngineSurfaceView;
import Game.EngieParts.AdEngine.AdHandler;
import Game.InitScriptMain;
import tams.libbuild.R;

public class MainActivity extends AppCompatActivity {
    private EngineSurfaceView mGLView;
    private InterstitialAd mInterstAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView=new EngineSurfaceView(this,new InitScriptMain());
        setContentView(mGLView);

        MobileAds.initialize(this, getResources().getString(R.string.ad_app_id));
        mInterstAd = new InterstitialAd(this);
        mInterstAd.setAdUnitId(getResources().getString(R.string.ad_interst_id));
        AdHandler.Init(mInterstAd);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        super.onResume();
        mGLView.onResume();
    }
}
