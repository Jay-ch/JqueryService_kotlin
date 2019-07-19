package com.jquery.service.android.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author j.query
 * @date 2018/12/24
 * @email j-query@foxmail.com
 */
class HeaderInterceptor : Interceptor {
    private var context: Context? = null

    constructor(context: Context?) {
        this.context = context
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
                /* .header("X-ECAPI-Authorization", UserHelper.getToken(context))
             .header("device-type", "android")*/
                .header("device-type", "android")
                .header("application/json", "charset=utf-8")
                /* .header("Access-Control-Allow-Headers", "x-requested-with")*/
                .method(original.method(), original.body())
                .build()
        // Content-Type: application/json;charset=UTF-8   Access-Control-Allow-Headers: x-requested-with
        return chain.proceed(request)
    }
}