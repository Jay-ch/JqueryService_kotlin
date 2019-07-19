package com.jquery.service.android.entity

import java.io.Serializable

data class UserInfoResult(
        var token: String,
        var is_new_user: Boolean,
        var user: UserDetailEntity) : Serializable
