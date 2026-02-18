package com.example.match3game;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.InterstitialAdLoadCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

/**
 * 广告管理器
 * 处理所有广告相关操作
 */
public class AdManager {
    private static final String TAG = "AdManager";

    // TODO: 替换为你的 AdMob 广告位 ID
    private static final String BANNER_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID";
    private static final String INTERSTITIAL_AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID";

    private Activity activity;
    private InterstitialAd interstitialAd;
    private boolean isAdLoaded;

    public AdManager(Activity activity) {
        this.activity = activity;
        this.isAdLoaded = false;
    }

    /**
     * 初始化广告 SDK
     */
    public void initialize() {
        // 启用测试广告
        String testDeviceIds = "TEST_EMULATOR";
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(java.util.Arrays.asList(testDeviceIds))
                .build();

        MobileAds.initialize(activity, initializationStatus -> {
            Log.d(TAG, "Ad SDK initialized");
        });
    }

    /**
     * 加载横幅广告
     */
    public void loadBannerAd(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdUnitId(BANNER_AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
    }

    /**
     * 加载插屏广告
     */
    public void loadInterstitialAd() {
        InterstitialAd.load(
                activity,
                INTERSTITIAL_AD_UNIT_ID,
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(InterstitialAd interstitialAd) {
                        AdManager.this.interstitialAd = interstitialAd;
                        isAdLoaded = true;
                        Log.d(TAG, "Interstitial ad loaded");
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e(TAG, "Interstitial ad failed to load: " + loadAdError.getMessage());
                        isAdLoaded = false;
                    }
                }
        );
    }

    /**
     * 显示插屏广告
     */
    public void showInterstitialAd() {
        if (interstitialAd != null && isAdLoaded) {
            interstitialAd.show(activity);
        } else {
            Log.d(TAG, "Interstitial ad not ready");
        }
    }

    /**
     * 在游戏结束时显示插屏广告
     */
    public void showAdOnGameOver() {
        if (isAdLoaded) {
            interstitialAd.show(activity);
        }
    }

    /**
     * 每隔一定步数显示插屏广告
     */
    public void showAdOnInterval(int currentMoves, int adFrequency) {
        if (currentMoves > 0 && currentMoves % adFrequency == 0) {
            showInterstitialAd();
        }
    }

    /**
     * 在游戏开始前显示插屏广告
     */
    public void showAdOnGameStart() {
        loadInterstitialAd();
    }
}
