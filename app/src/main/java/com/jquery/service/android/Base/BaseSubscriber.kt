package com.jquery.service.android.Base

import com.google.gson.Gson
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.retrofit.ImageUploadResult
import com.jquery.service.android.retrofit.UpdateResult
import io.reactivex.annotations.NonNull
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
abstract class BaseSubscriber<T> : ResourceSubscriber<T>() {

    override fun onNext(@NonNull t: T) {
        if (t is HttpResult<*>) {
            val result = t as HttpResult<*>
            when (result.code) {
            //"0000" -> onSuccess(t)
                "200" -> onSuccess(t)
            //"0" -> onSuccess(t)
                else -> onFail(result.msg)
            }
        } else if (t is ImageUploadResult<*>) {
            onSuccess(t)
        }else if (t is UpdateResult<*>) {
            onSuccess(t)
        }
    }

    override fun onError(@NonNull e: Throwable) {
        onFail(getErrorMsg(e))
    }

    override fun onComplete() {

    }

    protected abstract fun onSuccess(result: T)

    protected abstract fun onFail(s: String?)

    /**
     * token 失效
     *
     * @param error_desc
     */
    protected fun onTokenError(error_desc: Int) {
        //EventBus.getDefault().post(new TokenErrorEvent(not_table));
    }

    /**
     * 返回处理
     *
     * @param paramThrowable
     * @return
     */
    private fun getErrorMsg(paramThrowable: Throwable?): String? {
        if (paramThrowable == null)
            return "未知错误"
        if (paramThrowable is HttpException) {
            val localResponse = paramThrowable.response()
            if (localResponse != null) {
                val localResponseBody = localResponse.errorBody()
                if (localResponseBody != null)
                    try {
                        val gson = Gson()
                        val httpResult = gson.fromJson<HttpResult<*>>(localResponseBody.string(), HttpResult::class.java)
                        if (httpResult != null) {
                            return httpResult.msg
                        }
                    } catch (localException: Exception) {
                        return "未知错误"
                    }

            }
        } else if (paramThrowable is SocketException) {
            return "网络错误"
        } else if (paramThrowable is SocketTimeoutException) {
            return "服务器连接超时"
        } else if (paramThrowable is UnknownHostException) {
            return "网络错误"
        }
        return "未知错误"
    }
}