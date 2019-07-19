package com.jquery.service.android.listener

import com.jquery.service.android.entity.UserDetailEntity

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class LoginResultEvent {
    private var detailEntity: UserDetailEntity? = null
    private var isUpdate = false

    constructor(detailEntity: UserDetailEntity?, b: Boolean) {
        this.detailEntity = detailEntity
        this.isUpdate = b
    }

    constructor(detailEntity: UserDetailEntity) {
        this.detailEntity = detailEntity
    }


    fun isUpdate(): Boolean {
        return isUpdate
    }

    fun getDetailEntity(): UserDetailEntity? {
        return detailEntity
    }
}