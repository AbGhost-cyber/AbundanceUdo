package com.example.abundanceudo.featureBmi.data.repository

import com.example.abundanceudo.featureBmi.domain.model.BmiData
import com.example.abundanceudo.featureBmi.domain.repository.BmiRepository
import com.example.abundanceudo.featureBmi.domain.util.getBmiCategory
import com.example.abundanceudo.featureBmi.domain.util.getExtraText
import com.example.abundanceudo.featureBmi.domain.util.roundToTwoDecimal
import javax.inject.Inject
import kotlin.math.pow

class BmiRepositoryImpl @Inject constructor() : BmiRepository {
    override fun getBmi(name: String, height: Double, weight: Double): BmiData {
        val heightInMeters = (height / 100)

        // bmi formula is kg/m^2
        val bmiValue = weight / heightInMeters.pow(2.0)

        val bmiCategory = getBmiCategory(bmiValue)

        // ponderal Index is kg/m^3
        val ponderalIndex = weight / heightInMeters.pow(3.0)

        return BmiData(
            name,
            bmiValue.roundToTwoDecimal(),
            bmiCategory,
            ponderalIndex.roundToTwoDecimal(),
            bmiCategory.getExtraText()
        )
    }
}
