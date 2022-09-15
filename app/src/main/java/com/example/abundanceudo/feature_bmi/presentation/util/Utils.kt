package com.example.abundanceudo.feature_bmi.presentation.util

import android.content.Context
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

val randomRangeData = (10..999).map { it.toString() }
val genderData = listOf("Male", "Female")
