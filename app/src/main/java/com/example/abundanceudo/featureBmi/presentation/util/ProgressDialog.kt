package com.example.abundanceudo.featureBmi.presentation.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.abundanceudo.R

class ProgressDialog {
    companion object {
        fun progressDialog(context: Context, root: ViewGroup): Dialog {
            val dialog = Dialog(context)
            val inflate =
                LayoutInflater.from(context).inflate(R.layout.progress_dialog, root, false)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog
        }
    }
}
