package com.jquery.service.android.ui.login.presenter

import com.jquery.service.android.Base.BasePresenter
import com.jquery.service.android.Base.BaseSubscriber
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.ui.home.model.HomeModel
import com.jquery.service.android.ui.login.model.LoginContract

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class LoginPresenter : BasePresenter<LoginContract.LoginView>(), LoginContract.LoginPresenter {
    override fun loginByPhone(phone: String, password: String) {
        getView()?.showLoading(null)
        addSubscriber(mModel.loginByPhone(phone, password),
                object : BaseSubscriber<HttpResult<UserInfoResult>>() {
                    override fun onFail(s: String?) {
                        getView()?.hideLoading()
                        getView()?.loginFail(s!!)
                    }

                    override fun onSuccess(result: HttpResult<UserInfoResult>) {
                        getView()?.hideLoading()
                        getView()?.loginSuccess(result.data.user, result.data.token)
                    }
                })
    }

    override fun getWxToken(code: String) {
        getView()?.showLoading(null)
        addSubscriber(mModel.wxLogin(code), object : BaseSubscriber<HttpResult<UserInfoResult>>() {
            override fun onFail(s: String?) {
                getView()?.loginFail(s!!)
                getView()?.hideLoading()
            }

            override fun onSuccess(result: HttpResult<UserInfoResult>) {
                getView()?.hideLoading()
                getView()?.wxLoginSuccess(result.data)
            }
        })

    }

    val mModel: HomeModel by lazy {
        HomeModel()
    }
}