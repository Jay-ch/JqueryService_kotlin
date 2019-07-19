package com.jquery.service.android.ui.login.model

import com.jquery.service.android.Base.*
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
interface LoginContract {

    interface LoginView : ImBaseView {
        abstract fun loginSuccess(data: UserDetailEntity, token: String)

        abstract fun loginFail(e: String)

        abstract fun wxLoginSuccess(data: UserInfoResult)
    }

    interface LoginModel : ImBaseModel {
        abstract fun loginByPhone(phone: String, password: String): Flowable<HttpResult<UserInfoResult>>

        abstract fun wxLogin(code: String): Flowable<HttpResult<UserInfoResult>>
    }

    interface LoginPresenter : ImBasePresenter {
        abstract fun loginByPhone(phone: String, password: String)

        abstract fun getWxToken(code: String)
    }
}