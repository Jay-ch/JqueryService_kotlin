package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class UserDetailEntity : Serializable {
    var registerToken: String? = null
    var token: String? = null
    var deviceId: String? = null
    var baseKey: String? = null
    var expireTime: String? = null
    var createTime: String? = null
    var imei: String? = null
    var sessionKey: String? = null
    var is_completed: Int = 0
    var mobile_binded: Int = 0
}