package com.example.abundanceudo.feature_bmi.data.repository

import com.example.abundanceudo.feature_bmi.domain.model.BmiData
import com.example.abundanceudo.feature_bmi.domain.repository.BmiRepository
import com.example.abundanceudo.feature_bmi.domain.util.getBmiCategory
import com.example.abundanceudo.feature_bmi.domain.util.getExtraText
import com.example.abundanceudo.feature_bmi.domain.util.roundToOneDecimal
import kotlin.math.pow

class BmiRepositoryImpl : BmiRepository {
    override fun getBmi(name: String, height: Double, weight: Double): BmiData {
        val heightInMeters = (height / 100)

        // bmi formula is kg/m^2
        val bmiValue = weight / heightInMeters.pow(2.0)

        val bmiCategory = getBmiCategory(bmiValue)

        // ponderal Index is kg/m^3
        val ponderalIndex = weight / heightInMeters.pow(3.0)

        return BmiData(
            name,
            bmiValue.roundToOneDecimal(),
            bmiCategory,
            ponderalIndex.roundToOneDecimal(),
            bmiCategory.getExtraText()
        )
    }
}
