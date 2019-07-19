package com.jquery.service.android.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * created by caiQiang on 2019/6/13
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */
class PermissionUtil  {

    val TAG = "Permission"

//    init {
//        throw IllegalStateException("you can't instantiate me!")
//    }

    interface RequestPermission {

        /**
         * 权限请求成功
         */
        fun onRequestPermissionSuccess()

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailure(permissions: List<String>)

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>)
    }




    fun requestPermission(requestPermission: RequestPermission, rxPermissions: RxPermissions, vararg permissions: String) {
        if (permissions == null || permissions.size == 0) return

        val needRequest = ArrayList<String>()
        for (permission in permissions) { //过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission)
            }
        }

        if (needRequest.isEmpty()) {//全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess()
        } else {//没有申请过,则开始申请
            rxPermissions
                    .requestEach(*needRequest.toTypedArray())
                    .buffer(permissions.size)
                    .subscribe(object : Observer<List<Permission>> {
                        override fun onComplete() {}

                        override fun onSubscribe(d: Disposable) {}

                        override fun onError(e: Throwable) {
                            Log.e(TAG, "Request permissions onError" + e)
                        }

                        override fun onNext(@NonNull permissions: List<Permission>) {
                            val failurePermissions = ArrayList<String>()
                            val askNeverAgainPermissions = ArrayList<String>()
                            permissions
                                    .filterNot { it.granted }
                                    .forEach {
                                        if (it.shouldShowRequestPermissionRationale) {
                                            failurePermissions.add(it.name)
                                        } else {
                                            askNeverAgainPermissions.add(it.name)
                                        }
                                    }
                            if (failurePermissions.size > 0) {
                                Log.d(TAG, "Request permissions failure")
                                requestPermission.onRequestPermissionFailure(failurePermissions)
                            }

                            if (askNeverAgainPermissions.size > 0) {
                                Log.d(TAG, "Request permissions failure with ask never again")
                                requestPermission.onRequestPermissionFailureWithAskNeverAgain(askNeverAgainPermissions)
                            }

                            if (failurePermissions.size == 0 && askNeverAgainPermissions.size == 0) {
                                Log.d(TAG, "Request permissions success")
                                requestPermission.onRequestPermissionSuccess()
                            }
                        }
                    })
        }

    }

    /**
     * 请求获取定位权限
     */
    fun readLocation(requestPermission: RequestPermission, rxPermissions: RxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    /**
     * 请求获取余姚的批量权限
     */
    fun requestMultPerssions(requestPermission: RequestPermission, rxPermissions: RxPermissions) {
        requestPermission(requestPermission, rxPermissions,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
    }

    fun  gotoAppDetailIntent(activity : Activity){
        val intent =  Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivity(intent)
    }
}

