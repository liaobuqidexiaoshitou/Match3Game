package com.example.match3game

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.InterstitialAdLoadCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

/**
 * 广告管理器
 * 处理所有广告相关操作
 */
class AdManager(private val activity: Activity) {

    // TODO: 替换为你的 AdMob 广告位 ID
    private val BANNER_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID"
    private val INTERSTITIAL_AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID"

    private var interstitialAd: InterstitialAd? = null
    private var isAdLoaded = false

    /**
     * 初始化广告 SDK
     */
    fun initialize() {
        // 启用测试广告
        val testDeviceIds = listOf("TEST_EMULATOR")
        RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()

        MobileAds.initialize(activity) {
            Log.d("AdManager", "Ad SDK initialized")
        }
    }

    /**
     * 加载横幅广告
     */
    fun loadBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.adUnitId = BANNER_AD_UNIT_ID
        adView.loadAd(adRequest)
    }

    /**
     * 加载插屏广告
     */
    fun loadInterstitialAd() {
        InterstitialAd.load(
            activity,
            INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build()
        ) { ad, loadAdError ->
            if (loadAdError == null) {
                interstitialAd = ad
                isAdLoaded = true
                Log.d("AdManager", "Interstitial ad loaded")
            } else {
                Log.e("AdManager", "Interstitial ad failed to load: ${loadAdError.message}")
                isAdLoaded = false
            }
        }
    }

    /**
     * 显示插屏广告
     */
    fun showInterstitialAd() {
        interstitialAd?.let { ad ->
            if (ad.isLoaded) {
                ad.show(activity)
            } else {
                Log.d("AdManager", "Interstitial ad not ready")
            }
        } ?: run {
            Log.d("AdManager", "Interstitial ad is null")
        }
    }

    /**
     * 在游戏结束时显示插屏广告
     */
    fun showAdOnGameOver() {
        if (isAdLoaded) {
            interstitialAd?.show(activity)
        }
    }

    /**
     * 每隔一定步数显示插屏广告
     * @param currentMoves 当前步数
     * @param adFrequency 显示频率（每 N 步显示一次）
     */
    fun showAdOnInterval(currentMoves: Int, adFrequency: Int = 10) {
        if (currentMoves > 0 && currentMoves % adFrequency == 0) {
            showInterstitialAd()
        }
    }

    /**
     * 在关卡完成时显示插屏广告
     */
    fun showAdOnLevelComplete() {
        showInterstitialAd()
    }

    /**
     * 在游戏开始前显示插屏广告（可选）
     */
    fun showAdOnGameStart() {
        loadInterstitialAd()
    }

    /**
     * 请求激励视频广告（可选功能）
     */
    fun loadRewardedVideoAd() {
        // TODO: 实现激励视频广告
        Log.d("AdManager", "Rewarded video ad not implemented yet")
    }
}
