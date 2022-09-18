package com.example.abundanceudo.feature_bmi.presentation.shared_viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abundanceudo.feature_bmi.domain.model.BmiData
import com.example.abundanceudo.feature_bmi.domain.use_case.BmiUseCases
import com.example.abundanceudo.feature_bmi.domain.util.BmiCategory
import com.example.abundanceudo.feature_bmi.presentation.add_bmi.AddBmiDetailEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BmiSharedViewModel @Inject constructor(
    private val bmiUseCases: BmiUseCases
) : ViewModel() {

    private val userName = MutableStateFlow("")

    private val height = MutableStateFlow(0.0)
    private val weight = MutableStateFlow(0.0)

    private val _events = MutableSharedFlow<UiEvent>()
    val event = _events.asSharedFlow()

    private val _bmiData = MutableStateFlow(
        BmiData(
            "", 0.0,
            BmiCategory.Normal, 0.0, ""
        )
    )
    val bmiData = _bmiData.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(TimeUnit.MILLISECONDS.toMillis(1))
            _isLoading.value = false
        }
    }

    fun onEvent(event: AddBmiDetailEvent) = viewModelScope.launch {
        when (event) {
            is AddBmiDetailEvent.EnteredName -> {
                userName.emit(event.value)
            }
            is AddBmiDetailEvent.SelectedHeight -> {
                height.emit(event.value)
            }
            is AddBmiDetailEvent.SelectedWeight -> {
                weight.emit(event.value)
            }
            is AddBmiDetailEvent.CalculateBmi -> {
                if (userName.value.isBlank()) {
                    _events.emit(UiEvent.ShowSnackBar("a name is required"))
                    return@launch
                }
                val calBmiData = bmiUseCases(userName.value, weight.value, height.value)
                _bmiData.emit(
                    bmiData.value.copy(
                        userName = calBmiData.userName,
                        bmiValue = calBmiData.bmiValue,
                        bmiCategory = calBmiData.bmiCategory,
                        ponderalIndex = calBmiData.ponderalIndex,
                        extraText = calBmiData.extraText
                    )
                )
                _events.emit(UiEvent.GotoResults)
            }
        }
    }

    sealed class UiEvent {
        object GotoResults : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}
