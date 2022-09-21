package com.example.abundanceudo.featureBmi.domain.model

import com.example.abundanceudo.featureBmi.domain.util.BmiCategory
import java.io.Serializable

data class BmiData(
    val userName: String,
    val bmiValue: Double,
    val bmiCategory: BmiCategory,
    val ponderalIndex: Double,
    val extraText: String
) : Serializable
