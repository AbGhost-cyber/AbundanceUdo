package com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.abundanceudo.feature_bmi.domain.use_case.AdsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AdsViewModel
@Inject constructor(
    private val adsUseCases: AdsUseCases,
    @ApplicationContext context: Context
) : ViewModel() {

    init {
        adsUseCases.initAd(context)
    }

    fun onEvent(bmiAdsEvent: BmiAdsEvent) {
        when (bmiAdsEvent) {
            is BmiAdsEvent.AddFailedOrDismissed -> {
                Log.d("TAG", "onEvent: called")
                adsUseCases.onAddDismissOrError(bmiAdsEvent.adDismissErrorHandler)
            }
            is BmiAdsEvent.ShowAd -> {
                adsUseCases.onShowAd(bmiAdsEvent.callback)
            }
            is BmiAdsEvent.OnNativeAdLoaded -> {
                adsUseCases.onNativeAdLoaded(bmiAdsEvent.callback)
            }
        }
    }
}
