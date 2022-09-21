package com.example.abundanceudo.featureBmi.domain.useCases

import android.graphics.Bitmap
import com.example.abundanceudo.featureBmi.domain.repository.BmiBitmapCache
import com.example.abundanceudo.featureBmi.domain.repository.Cache
import java.io.File
import javax.inject.Inject

class BitmapUseCase @Inject constructor(
    private val bmiBitmapCache: BmiBitmapCache
) {
    suspend operator fun invoke(bitmap: Bitmap, cachedFile: File): Result<Cache> {
        return bmiBitmapCache.onImageCache(bitmap, cachedFile)
    }
}
