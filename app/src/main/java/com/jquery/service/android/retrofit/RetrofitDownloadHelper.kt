package com.jquery.service.android.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Download utils
 * Created by J.query on 2019/5/29.
 * email j-query@foxmail.com
 */

object RetrofitDownloadHelper {

    @Volatile
    private var retrofit: Retrofit? = null

    fun <T> createApi(paramClass: Class<T>): T {
        return retrofit!!.create(paramClass)
    }

    private fun initRetrofit(httpClient: OkHttpClient?, url: String) {
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(httpClient!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
    }

    fun initialize(url: String) {
        initRetrofit(null, url)
    }

    fun initialize(client: OkHttpClient, s: String) {
        initRetrofit(client, s)
    }

    fun reload(client: OkHttpClient, s: String) {
        retrofit = null
        initialize(client, s)
    }

}
