package com.example.abundanceudo.feature_bmi.domain.util

import kotlin.math.roundToInt

fun getBmiCategory(bmiValue: Double): BmiCategory {
    return when (bmiValue) {
        in 0.0..18.4 -> BmiCategory.UnderWeight
        in 18.5..25.0 -> BmiCategory.Normal
        in 25.1..40.0 -> BmiCategory.OverWeight
        else -> BmiCategory.Obese
    }
}

fun BmiCategory.getExtraText(): String {
    return when (this) {
        is BmiCategory.UnderWeight -> "${this.javaClass.simpleName} BMI range: 0.0kg/m2 - 18.4kg/m2"
        is BmiCategory.Normal -> "${this.javaClass.simpleName} BMI range: 18.5kg/m2 - 25kg/m2"
        is BmiCategory.OverWeight -> "${this.javaClass.simpleName} BMI range: 25.1kg/m2 - 40.0kg/m2"
        is BmiCategory.Obese -> "${this.javaClass.simpleName} BMI range: over 40kg/m2"
    }
}

fun Double.roundToTwoDecimal(): Double {
    return (this * 100.0).roundToInt() / 100.0
}
