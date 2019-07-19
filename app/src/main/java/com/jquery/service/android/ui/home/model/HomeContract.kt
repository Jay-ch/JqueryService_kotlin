package com.jquery.service.android.ui.home.model


import com.jquery.service.android.Base.ImBaseModel
import com.jquery.service.android.Base.ImBasePresenter
import com.jquery.service.android.Base.ImBaseView
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable


/**
 * @author j.query
 * @date 2018/10/17
 * @email j-query@foxmail.com
 */
interface HomeContract {

    interface HomeView : ImBaseView {

        fun loginSuccess(data: UserDetailEntity?, token: String?)

        fun loginFail(e: String?)

        fun wxLoginSuccess(data: UserInfoResult?)

        fun WeatherTestSuccess(data: WeatherEntity?)


        fun getTokenSuccess(token: TokenEntity)


        fun getRegisterTokenSuccess(token: TokenEntity)

        fun getTokenFail(s: String)

        fun getUserInfoSuccess(result: UserInfoResult)
    }

    interface HomeViewModel : ImBaseModel {

        fun loginByPhone(phone: String, password: String): Flowable<HttpResult<UserInfoResult>>

        fun wxLogin(code: String): Flowable<HttpResult<UserInfoResult>>

        fun WeatherTest(city: String): Flowable<HttpResult<WeatherEntity>>

        fun getToken(seq: String, channelId: String, deviceId: String, sign: String): Flowable<HttpResult<TokenEntity>>

        fun getUserInfo(): Flowable<HttpResult<UserInfoResult>>
    }

    interface HomeViewPresenter : ImBasePresenter {

        fun loginByPhone(phone: String, password: String)

        fun getWxToken(code: String)

        fun WeatherTest(city: String)

        fun getToken(seq: String, channelId: String, deviceId: String, sign: String)

        fun getUserInfo()
    }
}