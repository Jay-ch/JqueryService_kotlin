package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author j.query
 * @date 2018/11/14
 * @email j-query@foxmail.com
 */
data class TokenEntity(var token: String,
                       var is_new_user: Boolean,
                       var user: UserDetailEntity, var expireTime: Long, var timestamp: String, var resultCode: String, var sign: String, var sessionKey: String) : Serializable
