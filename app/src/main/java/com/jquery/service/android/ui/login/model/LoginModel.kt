package com.jquery.service.android.ui.login.model

import com.jquery.service.android.api.LoginServices
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.retrofit.RetrofitHelper
import io.reactivex.Flowable
import java.util.*

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class LoginModel : LoginContract.LoginModel {
    override fun loginByPhone(username: String, password: String): Flowable<HttpResult<UserInfoResult>> {
        val hash = HashMap<String, String>(2)
        hash["username"] = username
        hash["password"] = password
        return RetrofitHelper.createApi(LoginServices::class.java).login(createBody(hash))
    }

    override fun wxLogin(code: String): Flowable<HttpResult<UserInfoResult>> {
        val hashMap = HashMap<String, String>()
        hashMap["code"] = code
        return RetrofitHelper.createApi(LoginServices::class.java).wxLogin(createBody(hashMap))
    }
}