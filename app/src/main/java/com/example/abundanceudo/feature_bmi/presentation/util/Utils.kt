package com.example.abundanceudo.feature_bmi.presentation.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollLinearOffset(view: View) {
    val itemToScroll = this.getChildAdapterPosition(view)
    val center = this.width / 2 - (view.width / 2)
    this.layoutManager?.let {
        if (it is LinearLayoutManager) {
            it.scrollToPositionWithOffset(itemToScroll - 1, center)
        }
    }
}

fun AppCompatEditText.onTextChanged(
    action: (value: String) -> Unit
) {
    doAfterTextChanged { action(it.toString()) }
}

fun View.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    val drawable = background
    drawable.draw(canvas)
    draw(canvas)
    // after draw, reset view layout params
    updateLayoutParams {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    return bitmap
}

fun formatStringSizes(text1: String, text2: String): SpannableString {
    val spannableStr = SpannableString(text1 + text2)
    val textToNormalizeIndex = spannableStr.toString().indexOf(text2)
    spannableStr.setSpan(
        AbsoluteSizeSpan(
            40,
            true
        ),
        textToNormalizeIndex,
        textToNormalizeIndex + text2.length, 0
    )
    return spannableStr
}

val randomRangeData = (10..999).map { it.toString() }
val genderData = listOf("Male", "Female")
