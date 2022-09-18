package com.example.abundanceudo.feature_bmi.data.repository

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.abundanceudo.R
import com.example.abundanceudo.feature_bmi.domain.repository.AdsRepository
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd

class AdsRepositoryImpl : AdsRepository {
    private var mInterstitialAd: InterstitialAd? = null
    private var adRequest = AdRequest.Builder().build()
    private var adDismissErrorHandler: AdDismissErrorHandler? = null
    private var adLoaderBuilder: AdLoader.Builder? = null
    private lateinit var nativePriTextTypeface: Typeface
    private lateinit var nativeActionColor: ColorDrawable

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    override fun onAdDismissOrFail(adDismissErrorHandler: AdDismissErrorHandler) {
        setAdDismissErrorHandler(adDismissErrorHandler)
    }

    private fun setAdDismissErrorHandler(adDismissErrorHandler: AdDismissErrorHandler) {
        this.adDismissErrorHandler = adDismissErrorHandler
    }

    override fun init(mContext: Context) {
        InterstitialAd.load(
            mContext,
            mContext.getString(R.string.test_interstitials), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "Ad failed to load")
                    mInterstitialAd = null
                    adDismissErrorHandler?.onAdDismissOrError()
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = ad
                    setFScreenCallback()
                }
            }
        )
        adLoaderBuilder = AdLoader.Builder(mContext, mContext.getString(R.string.test_native_ad))
        nativePriTextTypeface = ResourcesCompat.getFont(mContext, R.font.product_sans_bold)!!
        nativeActionColor =
            ColorDrawable(ContextCompat.getColor(mContext, R.color.nativeActionColor))
    }

    private fun setFScreenCallback() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed full screen content")
                mInterstitialAd = null
                adDismissErrorHandler?.onAdDismissOrError()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                mInterstitialAd = null
                adDismissErrorHandler?.onAdDismissOrError()
            }
        }
    }

    override fun showAd(callback: (InterstitialAd) -> Unit) {
        if (mInterstitialAd != null) {
            callback.invoke(mInterstitialAd!!)
        } else {
            Log.d(TAG, "The Interstitial ad wasn't ready yet")
            adDismissErrorHandler?.onAdDismissOrError()
        }
    }

    override fun onNativeAdLoaded(callback: (NativeAd, NativeTemplateStyle) -> Unit) {
        adLoaderBuilder?.let {
            Log.d(TAG, "onNativeAdLoaded: $nativeActionColor")
            it.forNativeAd { nativeAd ->
                val style = NativeTemplateStyle.Builder()
                    .withPrimaryTextTypeface(nativePriTextTypeface)
                    .withCallToActionBackgroundColor(nativeActionColor)
                    .build()
                callback.invoke(nativeAd, style)
            }
            val adLoader = it.build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
}
