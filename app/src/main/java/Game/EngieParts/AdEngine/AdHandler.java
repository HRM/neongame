package Game.EngieParts.AdEngine;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AdHandler {
    private static int adShown=0;
    private static int failedRequest=0;
    private static InterstitialAd mAd=null;

    private static AdClosedCallback callAfterAd=null;
    private static boolean adClosed=false;


    public static void Init(InterstitialAd iAd){
        mAd=iAd;
        mAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adClosed=true;
                mAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    public static void showAd(AdClosedCallback ach){
        adClosed=false;
        callAfterAd=ach;
        if(mAd.isLoaded()) {
            mAd.show();
        }else{
            ach.call();
            callAfterAd=null;
            adClosed=true;
            if(!mAd.isLoading()){
                mAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }

    public static void processAfterAd(){
        if(adClosed&&callAfterAd!=null){
            callAfterAd.call();
            callAfterAd=null;
        }
    }

}
