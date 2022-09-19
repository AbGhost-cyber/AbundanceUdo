package com.example.abundanceudo.feature_bmi.presentation.util

import android.graphics.Bitmap
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class BmiBitmapCacheImpl(
    private val bitmap: Bitmap,
    private val cachedFile: File
) : BmiBitmapCache {
    private var cacheIsSucceed = false
    private var processIsCompleted = false

    override fun onImageCache(onCacheCallback: (Cache) -> Unit) {
        var outstream: FileOutputStream? = null
        try {
            outstream = FileOutputStream(cachedFile)
            cacheIsSucceed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream.flush()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outstream?.close()
            processIsCompleted = true
        }
        val currentCache = Cache(
            didCache = cacheIsSucceed,
            didComplete = processIsCompleted,
            file = cachedFile
        )
        onCacheCallback.invoke(currentCache)
    }
}
