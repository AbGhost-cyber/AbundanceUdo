package com.example.abundanceudo.featureBmi.data.repository

import android.graphics.Bitmap
import com.example.abundanceudo.featureBmi.domain.repository.BmiBitmapCache
import com.example.abundanceudo.featureBmi.domain.repository.Cache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class BmiBitmapCacheImpl @Inject constructor() : BmiBitmapCache {
    private var cacheIsSucceed = false

    override suspend fun onImageCache(bitmap: Bitmap, cachedFile: File) =
        withContext(Dispatchers.IO) {
            var stream: FileOutputStream? = null
            try {
                stream = FileOutputStream(cachedFile)
                cacheIsSucceed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return@withContext errorResult("file wasn't saved", e.cause)
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext errorResult("an error occurred", e.cause)
            } finally {
                stream?.close()
            }
            val cacheResult = Cache(
                didCache = cacheIsSucceed,
                file = cachedFile
            )
            return@withContext Result.success(cacheResult)
        }

    private fun <T> errorResult(optional: String, throwable: Throwable?) =
        Result.failure<T>(throwable ?: Throwable(optional))
}
