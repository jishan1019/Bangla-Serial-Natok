package com.bangalhindi.banglaserialnatok;

import android.content.Context;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FacebookActivity {

    public static InterstitialAd interstitialAd;
    public static AdView fbAdView;

    public static void loadFbIntAds(Context context){

        interstitialAd = new InterstitialAd(context, Config.FacebookIntId);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFbIntAds(context);
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public static void showFbIntAds(){
        if (interstitialAd != null && interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }
    }

    public static void loadFbBannerAds(Context context, LinearLayout adContainer){
        fbAdView = new AdView(context, Config.FacebookBanId, AdSize.BANNER_HEIGHT_50);
        adContainer.addView(fbAdView);

        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        fbAdView.loadAd(fbAdView.buildLoadAdConfig().withAdListener(adListener).build());

    }


}
