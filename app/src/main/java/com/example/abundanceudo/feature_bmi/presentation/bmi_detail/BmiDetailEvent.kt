package com.example.abundanceudo.feature_bmi.presentation.bmi_detail

sealed class BmiDetailEvent {
    object ShareResult : BmiDetailEvent()
    object RateApp : BmiDetailEvent()
}
