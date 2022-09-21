package com.example.abundanceudo.featureBmi.domain.repository

import com.example.abundanceudo.featureBmi.domain.model.BmiData

interface BmiRepository {
    fun getBmi(name: String, height: Double, weight: Double): BmiData
}
