package com.jquery.service.android.permission

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * 动态权限检查
 * @author J.query
 * @date 2018/9/10
 * @email j-query@foxmail.com
 */
class PermissionsChecker() {

    private lateinit var mContext: Context

  /*  constructor(context: Context) : this() {
        mContext = context.applicationContext
    }

    fun lacksPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun lacksPermission(permission: String): Boolean {
        return mContext?.let { ContextCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_DENIED
    }*/


    constructor(context: Context): this() {
        mContext = context.applicationContext
    }

    fun lacksPermissions(vararg permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun lacksPermission(permission: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(mContext, permission.toString())  == PackageManager.PERMISSION_GRANTED
    }
}