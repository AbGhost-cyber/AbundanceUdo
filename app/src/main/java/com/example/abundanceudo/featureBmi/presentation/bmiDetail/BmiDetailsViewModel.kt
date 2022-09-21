package com.example.abundanceudo.featureBmi.presentation.bmiDetail

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abundanceudo.featureBmi.domain.repository.Cache
import com.example.abundanceudo.featureBmi.domain.useCases.AdsUseCases
import com.example.abundanceudo.featureBmi.domain.useCases.BitmapUseCase
import com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel.BmiAdsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BmiDetailsViewModel @Inject constructor(
    private val bitmapUseCase: BitmapUseCase,
    private val adsUseCases: AdsUseCases,
    @ApplicationContext context: Context
) : ViewModel() {

    init {
        adsUseCases.initAd(context)
    }

    private val _cache = MutableStateFlow(Cache(false, null))
    val cache = _cache.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val event = _events.asSharedFlow()

    fun onSharePressed(bitmap: Bitmap, cachedFile: File) = viewModelScope.launch {
        val result = bitmapUseCase.invoke(bitmap, cachedFile)
        result.onSuccess {
            _cache.emit(it)
        }.onFailure {
            _events.emit(UiEvent.ShowSnackBar(it.message ?: "an error occurred"))
        }
    }

    fun onNativeAdLoaded(event: BmiAdsEvent.OnNativeAdLoaded) {
        adsUseCases.onNativeAdLoaded(event.callback)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}
