package com.jquery.service.android.ui.home.presenter

import com.jquery.service.android.Base.BasePresenter
import com.jquery.service.android.Base.BaseSubscriber
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.model.HomeModel


/**
 * @author j.query
 * @date 2018/10/18
 * @email j-query@foxmail.com
 */
class HomePresenter : BasePresenter<HomeContract.HomeView>(), HomeContract.HomeViewPresenter {


    override fun getToken(seq: String, channelId: String, deviceId: String, sign: String) {
        addSubscriber(mModel.getToken(seq, channelId, deviceId, sign),
                object : BaseSubscriber<HttpResult<TokenEntity>>() {
                    override fun onSuccess(result: HttpResult<TokenEntity>) {
                        //val tokenEntity = TokenEntity()
                        //val tokenEntity = TokenEntity()
                        // tokenEntity.token = result.token
                        // tokenEntity.expireTime = result.expireTime
                        // tokenEntity.timestamp = result.timestamp
                        //tokenEntity.sessionKey = result.sessionKey
                        getView()?.hideLoading()
                        getView()?.getTokenSuccess(result.data)
                    }

                    override fun onFail(s: String?) {
                        getView()?.getTokenFail(s!!)
                        // Log.e("", "onFail: ")
                        getView()?.hideLoading()

                    }
                })
    }

    override fun WeatherTest(city: String) {
        getView()?.showLoading("加载中...")
        addSubscriber(mModel.WeatherTest(city), object : BaseSubscriber<HttpResult<WeatherEntity>>() {
            override fun onSuccess(result: HttpResult<WeatherEntity>) {
                getView()?.hideLoading()
                getView()?.WeatherTestSuccess(result.data)
            }

            override fun onFail(s: String?) {
                getView()?.hideLoading()
                getView()?.loginFail(s)
            }
        })
    }

    override fun loginByPhone(phone: String, password: String) {
        getView()?.showLoading(null)
        addSubscriber(mModel.loginByPhone(phone, password),
                object : BaseSubscriber<HttpResult<UserInfoResult>>() {
                    override fun onSuccess(result: HttpResult<UserInfoResult>) {
                        getView()?.hideLoading()
                        getView()?.loginSuccess(result.data.user, result.data.token)
                    }

                    override fun onFail(s: String?) {
                        getView()?.hideLoading()
                        getView()?.loginFail(s)
                    }
                })
    }

    override fun getWxToken(code: String) {
        getView()?.showLoading(null)
        addSubscriber(mModel.wxLogin(code), object : BaseSubscriber<HttpResult<UserInfoResult>>() {
            override fun onSuccess(result: HttpResult<UserInfoResult>) {
                getView()?.hideLoading()
                getView()?.wxLoginSuccess(result.data)
            }

            override fun onFail(s: String?) {
                getView()?.loginFail(s)
                getView()?.hideLoading()
            }
        })
    }

    /**
     * 更新用户信息
     */
    override fun getUserInfo() {
        addSubscriber(mModel.getUserInfo(), object : BaseSubscriber<HttpResult<UserInfoResult>>() {
            override fun onFail(s: String?) {
                getView()?.getTokenFail(s!!)
            }

            override fun onSuccess(result: HttpResult<UserInfoResult>) {
                getView()?.getUserInfoSuccess(result.data)
            }


        })
    }

    val mModel: HomeModel by lazy {
        HomeModel()
    }

}
