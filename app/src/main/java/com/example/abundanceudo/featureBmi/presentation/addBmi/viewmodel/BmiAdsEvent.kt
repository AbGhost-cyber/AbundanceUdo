package com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel

import com.example.abundanceudo.featureBmi.data.repository.AdDismissErrorHandler
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

sealed class BmiAdsEvent {
    data class AddFailedOrDismissed(
        val adDismissErrorHandler: AdDismissErrorHandler
    ) : BmiAdsEvent()

    data class ShowAd(val callback: (InterstitialAd) -> Unit) : BmiAdsEvent()
    data class OnNativeAdLoaded(val callback: (NativeAd, NativeTemplateStyle) -> Unit) : BmiAdsEvent()
}
