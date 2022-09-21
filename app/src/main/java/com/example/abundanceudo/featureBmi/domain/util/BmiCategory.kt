package com.example.abundanceudo.featureBmi.domain.util

sealed class BmiCategory {
    object UnderWeight : BmiCategory()
    object Normal : BmiCategory()
    object OverWeight : BmiCategory()
    object Obese : BmiCategory()
}
