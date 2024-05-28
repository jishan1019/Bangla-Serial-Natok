package com.bangalhindi.serialdramaepisod;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;

public class ApplovinActivity {

    public static  MaxInterstitialAd interstitialAd;

    public static void loadApplovinInterstitial(Context context){
        interstitialAd = new MaxInterstitialAd(Config.ApplovinMaxId, (Activity) context);
        MaxAdListener adListener = new MaxAdListener() {
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd) {
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) {
                loadApplovinInterstitial(context);
            }

            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {

            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {

            }
        };
        interstitialAd.setListener(adListener);
        interstitialAd.loadAd();
    }

    public static void showApplivinAds(){
        if (interstitialAd.isReady() && interstitialAd != null){
            interstitialAd.showAd();
        }
    }

}
