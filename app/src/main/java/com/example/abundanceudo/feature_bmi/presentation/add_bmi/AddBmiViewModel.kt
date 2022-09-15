package com.example.abundanceudo.feature_bmi.presentation.add_bmi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abundanceudo.feature_bmi.domain.model.BmiData
import com.example.abundanceudo.feature_bmi.domain.use_case.BmiUseCases
import com.example.abundanceudo.feature_bmi.domain.util.BmiCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBmiViewModel @Inject constructor(
    private val bmiUseCases: BmiUseCases
) : ViewModel() {

    private val userName = MutableStateFlow("")

    private val heightWeight = MutableStateFlow(Pair(0.0, 0.0))

    private val _events = MutableSharedFlow<UiEvent>()
    val event = _events.asSharedFlow()

    private val _bmiData = MutableStateFlow(
        BmiData(
            "", 0.0,
            BmiCategory.Normal, 0.0, ""
        )
    )
    val bmiData = _bmiData.asStateFlow()

    fun onEvent(event: AddBmiDetailEvent) = viewModelScope.launch {
        when (event) {
            is AddBmiDetailEvent.EnteredName -> {
                userName.emit(event.value)
            }
            is AddBmiDetailEvent.SelectedHeight -> {
                heightWeight.emit(
                    heightWeight.value.copy(first = event.value)
                )
            }
            is AddBmiDetailEvent.SelectedWeight -> {
                heightWeight.emit(
                    heightWeight.value.copy(second = event.value)
                )
            }
            is AddBmiDetailEvent.CalculateBmi -> {
                if (userName.value.isBlank()) {
                    _events.emit(UiEvent.ShowSnackBar("a name is required"))
                    return@launch
                }
                heightWeight.collect {
                    val height = it.first
                    val weight = it.second
                    val calBmiData = bmiUseCases.getBmi(userName.value, weight, height)
                    _bmiData.emit(
                        bmiData.value.copy(
                            userName = calBmiData.userName,
                            bmiValue = calBmiData.bmiValue,
                            bmiCategory = calBmiData.bmiCategory,
                            ponderalIndex = calBmiData.ponderalIndex,
                            extraText = calBmiData.extraText
                        )
                    )
                    _events.emit(UiEvent.SaveNote)
                }
            }
        }
    }

    sealed class UiEvent {
        object SaveNote : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}
