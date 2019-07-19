package com.jquery.service.android.retrofit

import com.jquery.service.android.listener.DownloadProgressListener
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
class DownloadProgressInterceptor : Interceptor {
    private val listener: DownloadProgressListener

    constructor(listener: DownloadProgressListener) : super() {
        this.listener = listener
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalResponse = chain.proceed(chain.request())

        return originalResponse.newBuilder()
                .body(DownloadProgressResponseBody(originalResponse.body(), listener))
                .build()
    }
}