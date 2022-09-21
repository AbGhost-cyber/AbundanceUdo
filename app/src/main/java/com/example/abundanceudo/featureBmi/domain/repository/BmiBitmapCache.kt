package com.example.abundanceudo.featureBmi.domain.repository

import android.graphics.Bitmap
import java.io.File

interface BmiBitmapCache {
    suspend fun onImageCache(bitmap: Bitmap, cachedFile: File): Result<Cache>
}

data class Cache(
    val didCache: Boolean,
    val file: File?
)
