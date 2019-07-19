package com.jquery.service.android.Base

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
abstract class BaseModel {
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