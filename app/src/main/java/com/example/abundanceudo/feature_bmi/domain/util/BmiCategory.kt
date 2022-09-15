package com.example.abundanceudo.feature_bmi.domain.util

sealed class BmiCategory {
    object UnderWeight : BmiCategory()
    object Normal : BmiCategory()
    object OverWeight : BmiCategory()
    object Obese : BmiCategory()
}
