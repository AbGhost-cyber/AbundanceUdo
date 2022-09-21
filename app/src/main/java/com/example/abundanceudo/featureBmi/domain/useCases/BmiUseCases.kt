package com.example.abundanceudo.featureBmi.domain.useCases

import com.example.abundanceudo.featureBmi.domain.model.BmiData
import com.example.abundanceudo.featureBmi.domain.repository.BmiRepository
import javax.inject.Inject

class BmiUseCases @Inject constructor(
    private val bmiRepository: BmiRepository
) {
    operator fun invoke(name: String, weight: Double, height: Double): BmiData {
        return bmiRepository.getBmi(name, height, weight)
    }
}
