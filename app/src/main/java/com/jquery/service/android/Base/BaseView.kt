package com.jquery.service.android.Base

import android.content.Context
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * @author J.query
 * @date 2019/1/22
 * @email j-query@foxmail.com
 */

interface ImBaseView {

    fun providerContext(): Context

    fun showLoading(s: String?)

    fun hideLoading()
}

interface ImBasePresenter {

    fun attachView(view: ImBaseView)

    fun detachView()
}

interface ImBaseModel {
    /**
     * 生成body
     *
     * @param hashMap
     * @return
     */
    fun createBody(hashMap: HashMap<*, *>): RequestBody {
        val gson = Gson()
        val s = gson.toJson(hashMap)
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s)
    }
}



