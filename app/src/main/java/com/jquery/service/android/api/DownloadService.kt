package com.jquery.service.android.api

import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
interface DownloadService {

    @Streaming
    @GET
    abstract fun download(@Url url: String?): Observable<ResponseBody>

    @Streaming
    @GET
    fun downloads(@Url url: String?): Flowable<HttpResult<UserInfoResult>>

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("ecapi.user.info")
    abstract fun getAppInfo(): Flowable<HttpResult<UserInfoResult>>

    @Streaming
    @GET
    fun downloadWithDynamicUrl(@Url fileUrl: String?): Call<ResponseBody>
}