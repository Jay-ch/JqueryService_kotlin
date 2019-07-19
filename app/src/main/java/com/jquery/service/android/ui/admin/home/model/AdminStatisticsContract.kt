package com.jquery.service.android.ui.home.model


import com.jquery.service.android.Base.ImBaseModel
import com.jquery.service.android.Base.ImBasePresenter
import com.jquery.service.android.Base.ImBaseView


/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
interface AdminStatisticsContract {

    interface StatisticsView : ImBaseView {
        fun getUnsignedEmployeeSuccess(unSignList: ArrayList<String>)

        fun getUnsignedEmployeeFail(reason : String?)
        fun getFaultStatueSuccess(unSignList: ArrayList<String>)
        fun getFaultStatueFail(s: String?)

//        fun loginSuccess(data: UserDetailEntity?, token: String?)
//
//        fun loginFail(e: String?)
//
//        fun wxLoginSuccess(data: UserInfoResult?)
//
//        fun WeatherTestSuccess(data: WeatherEntity?)
//
//
//        fun getTokenSuccess(token: TokenEntity)
//
//
//        fun getRegisterTokenSuccess(token: TokenEntity)
//
//        fun getTokenFail(s: String)
//
//        fun getUserInfoSuccess(result: UserInfoResult)
    }

    interface StatisticsModel : ImBaseModel {
//
//        fun loginByPhone(phone: String, password: String): Flowable<HttpResult<UserInfoResult>>
//
//        fun wxLogin(code: String): Flowable<HttpResult<UserInfoResult>>
//
//        fun WeatherTest(city: String): Flowable<HttpResult<WeatherEntity>>
//
//        fun getToken(seq: String, channelId: String, deviceId: String, sign: String): Flowable<HttpResult<TokenEntity>>
//
//        fun getUserInfo(): Flowable<HttpResult<UserInfoResult>>
    }

    interface StatisticsPresenter : ImBasePresenter {
//
//        fun loginByPhone(phone: String, password: String)
//
//        fun getToken(seq: String, channelId: String, deviceId: String, sign: String)
//
//        fun getUserInfo()
        fun getUnsignedEmployee()

        fun getFaultStatue()
    }
}