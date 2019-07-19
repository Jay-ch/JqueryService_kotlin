package com.jquery.service.android.retrofit

import com.jquery.service.android.logger.LogUtil

/**
 * @author j.query
 * @date 2018/12/25
 * @email j-query@foxmail.com
 */
class LogInterceptor : HttpLoggingInterceptorM.Logger {

    var INTERCEPTOR_TAG_STR = "OkHttp"
    constructor() {}

    constructor(tag: String) {
        INTERCEPTOR_TAG_STR = tag
    }

    override fun log(message: String, @LogUtil.LogType type: Int) {
        LogUtil.printLog(false, type, INTERCEPTOR_TAG_STR, message)
    }

    companion object {
        @JvmField
        var INTERCEPTOR_TAG_STR: String = TODO("initialize me")
    }

}