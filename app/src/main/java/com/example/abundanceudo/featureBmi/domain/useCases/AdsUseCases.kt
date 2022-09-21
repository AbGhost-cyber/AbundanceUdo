package com.example.abundanceudo.featureBmi.domain.useCases

import android.content.Context
import com.example.abundanceudo.featureBmi.data.repository.AdDismissErrorHandler
import com.example.abundanceudo.featureBmi.domain.repository.AdsRepository
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import javax.inject.Inject

class AdsUseCases @Inject constructor(
    private val adsRepository: AdsRepository
) {

    fun initAd(mContext: Context) {
        return adsRepository.init(mContext)
    }

    fun onShowAd(callback: (InterstitialAd) -> Unit) = adsRepository.showAd(callback)

    fun onAddDismissOrError(adDismissErrorHandler: AdDismissErrorHandler) {
        return adsRepository.onAdDismissOrFail(adDismissErrorHandler)
    }

    fun onNativeAdLoaded(callback: (NativeAd, NativeTemplateStyle) -> Unit) {
        return adsRepository.onNativeAdLoaded(callback)
    }
}
