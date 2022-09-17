package com.example.abundanceudo.feature_bmi.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollLinearOffset(mContext: Context, view: View) {
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

fun getBitmapFromView(view: View): Bitmap {
    val spec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.UNSPECIFIED)
    view.measure(spec, spec)

    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    val bitmap =
        Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

val randomRangeData = (10..999).map { it.toString() }
val genderData = listOf("Male", "Female")
