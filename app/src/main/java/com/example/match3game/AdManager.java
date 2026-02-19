package com.example.match3game;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

/**
 * 广告管理器
 * 处理所有广告相关操作
 */
public class AdManager {
    private static final String TAG = "AdManager";
    private static boolean isInitialized = false;
    private static boolean isLoading = false;

    // TODO: 替换为你的 AdMob 广告位 ID
    private static final String BANNER_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID";

    private Activity activity;
    private AdView bannerAd;

    public AdManager(Activity activity) {
        this.activity = activity;
        initialize();
    }

    /**
     * 初始化广告 SDK
     */
    public void initialize() {
        if (isInitialized) {
            return;
        }

        try {
            // 启用测试广告
            String testDeviceIds = "TEST_EMULATOR";
            RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                    .setTestDeviceIds(java.util.Arrays.asList(testDeviceIds))
                    .build();

            MobileAds.initialize(activity, initializationStatus -> {
                Log.d(TAG, "Ad SDK initialized successfully");
                isInitialized = true;
                // 初始化完成后，如果正在等待加载广告，则加载
                if (isLoading && bannerAd != null) {
                    loadBannerAd(bannerAd);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Ad SDK", e);
            isInitialized = true; // 即使失败也设为 true，避免重复初始化
        }
    }

    /**
     * 检查是否已初始化
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * 加载横幅广告
     */
    public void loadBannerAd(AdView adView) {
        this.bannerAd = adView;

        try {
            if (!isInitialized) {
                Log.d(TAG, "Ad SDK not initialized, waiting...");
                isLoading = true;
                // 如果未初始化，等待初始化完成
                return;
            }

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdUnitId(BANNER_AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);
            adView.loadAd(adRequest);
            Log.d(TAG, "Banner ad loaded");
        } catch (Exception e) {
            Log.e(TAG, "Failed to load banner ad", e);
        }
    }
}
