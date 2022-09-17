package com.example.abundanceudo.feature_bmi.domain.repository

import android.content.Context
import com.example.abundanceudo.feature_bmi.data.repository.AdDismissErrorHandler
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

interface AdsRepository {
    fun init(mContext: Context)
    fun showAd(callback: (InterstitialAd) -> Unit)
    fun onAdDismissOrFail(adDismissErrorHandler: AdDismissErrorHandler)
    fun onNativeAdLoaded(callback: (NativeAd, NativeTemplateStyle) -> Unit)
}
