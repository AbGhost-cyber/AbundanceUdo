package com.example.abundanceudo.feature_bmi.domain.model

import com.example.abundanceudo.feature_bmi.domain.util.BmiCategory

data class BmiData(
    val userName: String,
    val bmiValue: Double,
    val bmiCategory: BmiCategory,
    val ponderalIndex: Double,
    val extraText: String
)
