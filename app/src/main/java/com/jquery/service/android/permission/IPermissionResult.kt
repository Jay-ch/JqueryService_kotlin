package com.jquery.service.android.permission

import android.app.Activity



/**
 * 权限处理结果的接口
 * @author J.query
 * @date 2019/4/8
 * @email j-query@foxmail.com
 */
interface IPermissionResult {

        fun getPermissionFailed(activity: Activity, requestCode: Int, deniedPermissions: Array<String>)

        fun getPermissionSuccess(activity: Activity, requestCode: Int)
}