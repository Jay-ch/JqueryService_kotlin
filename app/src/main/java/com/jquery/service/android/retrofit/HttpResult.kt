package com.jquery.service.android.retrofit

/**
 * @author j.query
 * @date 2018/12/21
 * @email j-query@foxmail.com
 */
class HttpResult<T>(var token: String, var code: String,
                    var is_new_user: Boolean, var data: T,
                    var msg: String, var error_desc: String,
                    var name: String, var resType: String, var displayAddr: String,
                    var subsType: String, var state: String,
                    var timestamp: String, var sign: String,
                    var sessionKey: String, var resultMsg: String,
                    var expireTime: Long, var error_code: Int,
                    var message: String, var subsCode: String)