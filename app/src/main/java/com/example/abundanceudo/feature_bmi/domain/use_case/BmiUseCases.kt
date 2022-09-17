package com.example.abundanceudo.feature_bmi.domain.use_case

import com.example.abundanceudo.feature_bmi.domain.model.BmiData
import com.example.abundanceudo.feature_bmi.domain.repository.BmiRepository

class BmiUseCases(
    private val bmiRepository: BmiRepository
) {
    operator fun invoke(name: String, weight: Double, height: Double): BmiData {
        return bmiRepository.getBmi(name, height, weight)
    }
}
