package com.jquery.service.android.ui.home.model

import com.jquery.service.android.api.FaultPageServices
import com.jquery.service.android.api.LoginServices
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.retrofit.RetrofitHelper
import io.reactivex.Flowable


/**
 * @author j.query
 * @date 2018/10/17
 * @email j-query@foxmail.com
 */

class HomeModel : HomeContract.HomeViewModel {
    override fun getToken(seq: String, channelId: String, deviceId: String, sign: String): Flowable<HttpResult<TokenEntity>> {
        return RetrofitHelper.createApi(LoginServices::class.java).getToken(seq, channelId, deviceId, sign)
    }

    override fun WeatherTest(city: String): Flowable<HttpResult<WeatherEntity>> {
       // val hashMap = hashMapOf("city" to city)
        return RetrofitHelper.createApi(LoginServices::class.java).WeatherTestInfo(city)
    }

    override fun loginByPhone(username: String, password: String): Flowable<HttpResult<UserInfoResult>> {
        val hashMap = hashMapOf("username" to username, "password" to password)
        return RetrofitHelper.createApi(LoginServices::class.java).login(createBody(hashMap))
    }

    override fun wxLogin(code: String): Flowable<HttpResult<UserInfoResult>> {
        val hashMap = hashMapOf("code" to code)
        return RetrofitHelper.createApi(LoginServices::class.java).wxLogin(createBody(hashMap))
    }


    override fun getUserInfo(): Flowable<HttpResult<UserInfoResult>> {

        return RetrofitHelper.createApi(FaultPageServices::class.java).getUserInfo()
    }
}