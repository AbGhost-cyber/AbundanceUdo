package com.example.abundanceudo.feature_bmi.presentation.util

import java.io.File

interface BmiBitmapCache {
    fun onImageCache(onCacheCallback: (Cache) -> Unit)
}

data class Cache(
    val didCache: Boolean,
    val didComplete: Boolean,
    val file: File
)
