package com.example.abundanceudo.feature_bmi.domain.repository

import com.example.abundanceudo.feature_bmi.domain.model.BmiData

interface BmiRepository {
    fun getBmi(name: String, height: Double, weight: Double): BmiData
//    fun shareBmiData()
//    suspend fun saveScreenShot()
//    fun rateApp()
}
