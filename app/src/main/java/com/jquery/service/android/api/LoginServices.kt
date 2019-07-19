package com.jquery.service.android.api

import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * @author j.query
 * @date 2018/10/17
 * @email j-query@foxmail.com
 */
interface LoginServices {
    /**
     * 登录
     *
     * @param body
     * @return
     */
    @POST("ecapi.auth.signin")
    fun login(@Body body: RequestBody): Flowable<HttpResult<UserInfoResult>>

    /**
     * 第三方登录
     *
     * @param body
     * @return
     */
    @POST("ecapi.auth.weixin")
    fun wxLogin(@Body body: RequestBody): Flowable<HttpResult<UserInfoResult>>


    /**
     * 登录
     *
     * @param
     * @return
     */
    @POST("ecapi.user.info")
    fun getUserInfo(@Body body: RequestBody): Flowable<HttpResult<UserInfoResult>>

    /**
     * 天气测试数据
     *https://www.apiopen.top/weatherApi?city
     * @param
     * @return
     */
    @POST("weatherApi?")
    fun WeatherTestInfo(@Query("city") city: String): Flowable<HttpResult<WeatherEntity>>

    /**
     * 读卡获取token
     * "https://api.towngasvcc.com/vcc-openapi/oauth/getToken?seq=xxxxxxxxxxxxx&channelId=xxxx&deviceId=xxxxxxxxxx&sign=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
     * /oauth/getToken?seq=xxxxxxxxxxxxx&channelId=xxxx&deviceId=xxxxxxxxxx&sign=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     *
     * @return
     */
    @GET("/vcc-openapi/oauth/getToken?")
    fun getToken(@Query("seq") seq: String, @Query("channelId") channelId: String, @Query("deviceId") deviceId: String, @Query("sign") sign: String): Flowable<HttpResult<TokenEntity>>
}