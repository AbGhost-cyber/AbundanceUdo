package com.example.abundanceudo.feature_bmi.presentation.add_bmi

sealed class AddBmiDetailEvent {
    data class EnteredName(val value: String) : AddBmiDetailEvent()
    data class SelectedHeight(val value: Double) : AddBmiDetailEvent()
    data class SelectedWeight(val value: Double) : AddBmiDetailEvent()
    object CalculateBmi : AddBmiDetailEvent()
}
