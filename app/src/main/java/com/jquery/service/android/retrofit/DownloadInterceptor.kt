package com.jquery.service.android.retrofit

import com.jquery.service.android.listener.DownloadListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Executor

/**
 * @author J.query
 * @date 2019/5/29
 * @email j-query@foxmail.com
 */
class DownloadInterceptor : Interceptor {
    private var listener: DownloadListener? = null
    private var executor: Executor? = null

    constructor(executor: Executor?, listener: DownloadListener?) : super() {
        this.listener = listener
        this.executor = executor
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
                .body(DownloadResponseBody(originalResponse.body()!!, executor!!, listener!!))
                .build()
    }
}