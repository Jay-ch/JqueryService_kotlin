package com.jquery.service.android.retrofit

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.jquery.service.android.api.DownloadService
import com.jquery.service.android.listener.DownloadListener
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * 下载工具类
 * @author J.query
 * @date 2019/5/31
 * @email j-query@foxmail.com
 */
class DownloadUtil {
    private val TAG = DownloadUtil::class.java.simpleName
    private val DEFAULT_TIMEOUT = 15
    private var executor: DownloadUtil.MainThreadExecutor? = null

    private object SingletonHolder {
        internal val INSTANCE = DownloadUtil()
    }

    fun getInstance(): DownloadUtil {
        return DownloadUtil.SingletonHolder.INSTANCE
    }


    constructor() : super()


    fun downloadFile(baseUrl: String?, rUrl: String?, filePath: String?, listener: DownloadListener?) {
        executor = MainThreadExecutor()
        val interceptor = DownloadInterceptor(executor, listener)
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .sslSocketFactory(getSSLFactory()!!, xtm)
                .hostnameVerifier(DO_NOT_VERIFY)
                .build()
        val api = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .build()
                .create(DownloadService::class.java!!)
        Thread {
            try {
                val result = api.downloadWithDynamicUrl(rUrl).execute()
                val file = writeFile(filePath, result.body()!!.byteStream())
                if (listener != null) {
                    executor?.execute { listener.onFinish(file) }
                }

            } catch (e: IOException) {
                if (listener != null) {
                    executor?.execute { listener.onFailed(e.message) }
                }
                e.printStackTrace()
            }
        }.start()
    }

    private fun getSSLFactory(): SSLSocketFactory? {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(xtm), SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private val DO_NOT_VERIFY = HostnameVerifier { hostname, session -> true }

    private val xtm = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    fun stopDown() {
        executor?.execute {

        }

    }

    /**
     * 停止下载
     */
    /*  public void stopDown(DownInfo info) {
        if (info == null) return;
        info.setState(DownState.STOP);
        info.getListener().onStop();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        *//*保存数据库信息和本地文件*//*
        db.save(info);
    }*/


    /**
     * 暂停下载
     *
     * @param info
     */
    /*  public void pause(DownInfo info) {
        if (info == null) return;
        info.setState(DownState.PAUSE);
        info.getListener().onPuase();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        *//*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*//*
        db.update(info);
    }*/

    /**
     * 停止全部下载
     */
    /* public void stopAllDown() {
        for (DownInfo downInfo : downInfos) {
            stopDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }*/

    /**
     * 暂停全部下载
     */
    /* public void pauseAll() {
        for (DownInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }*/


    private fun writeFile(filePath: String?, ins: InputStream?): File? {
        if (ins == null)
            return null
        val file = File(filePath)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            var b = ByteArray(1024)
            var len: Int = -1
            while ((ins.read().also { len = it }) != -1) {
                fos.write(b, 0, len)
            }
            fos.flush()
        } catch (e: FileNotFoundException) {
            Log.e("FileNotFoundException--", e.toString())

            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            throw DownloadException(e.message!!, e)
            Log.e("DownloadException====", e.toString())

        } finally {
            try {
                ins.close()
                fos!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("写文件异常==================", e.toString())
            }

        }
        return file
    }

    private inner class DownloadException(message: String, cause: Throwable) : RuntimeException(message, cause)

    private inner class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(r: Runnable) {
            handler.post(r)
        }
    }
}