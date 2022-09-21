package com.example.abundanceudo.featureBmi.presentation.util

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan

fun formatStringSizes(text1: String, text2: String): SpannableString {
    val spannableStr = SpannableString(text1 + text2)
    val textToNormalizeIndex = spannableStr.toString().indexOf(text2)
    spannableStr.setSpan(
        AbsoluteSizeSpan(
            40,
            true
        ),
        textToNormalizeIndex,
        textToNormalizeIndex + text2.length,
        0
    )
    return spannableStr
}

val randomRangeData = (10..999).map { it.toString() }
val genderData = listOf("Male", "Female")
