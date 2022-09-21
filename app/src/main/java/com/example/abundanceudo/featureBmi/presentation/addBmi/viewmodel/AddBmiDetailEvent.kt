package com.example.abundanceudo.featureBmi.presentation.addBmi.viewmodel

sealed class AddBmiDetailEvent {
    data class EnteredName(val value: String) : AddBmiDetailEvent()
    data class SelectedHeight(val value: Double) : AddBmiDetailEvent()
    data class SelectedWeight(val value: Double) : AddBmiDetailEvent()
    object CalculateBmi : AddBmiDetailEvent()
}
