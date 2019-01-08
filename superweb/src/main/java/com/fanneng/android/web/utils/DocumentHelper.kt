package com.fanneng.android.web.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.fanneng.android.web.net.DownloadApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.exceptions.Exceptions
import rx.schedulers.Schedulers
import java.io.File

/**
 * 项目 ： SuperWeb
 * 作者 ： Chuckifan
 * 时间 ： 2019/1/8 17:34
 * 内容 ：
 */
class DocumentHelper {
    private val retrofit: Retrofit
    private val api: DownloadApi

    init {
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createClient())
                .baseUrl("http://www.baidu.com/")
                .build()

        api = retrofit.create(DownloadApi::class.java)


    }

    @WorkerThread
    fun loadDocument(url: String, parentPath: String, fileName: String) = Observable
            .just(url)
            .concatMap {
                val cachePath = loadDocumentFromCache(parentPath + fileName)
                if (null != cachePath) {
                    Observable.just(cachePath)
                } else {
                    downloadFile(url, parentPath, fileName)
                }
            }

    private fun loadDocumentFromCache(filePath: String): String? {
        val file = File(filePath)

        return if (file.isFile && file.exists()) {
            file.absolutePath
        } else {
            null
        }


    }

    private fun downloadFile(url: String, parentPath: String, fileName: String) =
            api.download(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .concatMap {
                        val file = saveFile(it.bytes(), parentPath, fileName)
                        file ?: Exceptions.propagate(NullPointerException("save file failed"))
                        Observable.just(file!!.absolutePath)

                    }

    private fun saveFile(bytes: ByteArray?, parentPath: String, fileName: String): File? {
        return try {
            bytes ?: throw NullPointerException("bytes should not be null")

            if (!parentPath.endsWith("/")) throw IllegalArgumentException("parentPath should end with '/'. ")

            val parentDir = File(parentPath)

            if (!parentDir.exists()) parentDir.mkdir()


            val file = File(parentPath + fileName)
            if (file.exists())file.delete()
            file.createNewFile()
            if (!file.isFile) throw IllegalArgumentException("Illegal parentPath or fileName")
            if (!file.canRead() || !file.canWrite()) throw IllegalArgumentException("No permission.")

            file.writeBytes(bytes)
            file
        } catch (e: Exception) {
            Log.e(TAG, "save file failed: ${e.message}")
            e.printStackTrace()
            null
        }

    }


    private fun createClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(logInterceptor)
                .build()
    }


    companion object {
        private const val TAG = "DocumentHelper"
        private var instance: DocumentHelper? = null

        @Synchronized
        fun get(): DocumentHelper {
            if (null == instance) {
                instance = DocumentHelper()
            }
            return instance!!
        }
    }
}