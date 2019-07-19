package com.jquery.service.android.permission

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.utils.ToastUtilss
import com.jquery.service.android.widgets.dialog.PermissionDialog

/**
 * 动态权限管理
 * @author J.query
 * @date 2019/4/8
 * @email j-query@foxmail.com
 */
@SuppressLint("StaticFieldLeak")
object PermissionUtils {
    private lateinit var mContext: Context
    private val RESULT_CODE_TAKE_CAMERA = 7461    //拍照
    private val RESULT_CODE_OPEN_ALBUM = 7462     //读写
    private val RESULT_CODE_SOUND_RECORD = 7463   //录音
    private val RESULT_CODE_LOCATION = 7464   //定位


    private var cameraCallback: (() -> Unit)? = null        //相机回调
    private var readAndWriteCallback: (() -> Unit)? = null  //读写回调
    private var audioCallback: (() -> Unit)? = null         //录音回调
    private var locationCallback: (() -> Unit)? = null         //定位回调

    private val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION)

    /*   constructor(context: Context): super() {
           mContext = context.applicationContext
       }*/
    /**
     * 相机权限申请
     */
    fun camera(context: Context, cameraCallback: () -> Unit) {
        this.cameraCallback = cameraCallback
        permission(context, Manifest.permission.CAMERA, RESULT_CODE_TAKE_CAMERA, cameraCallback)
    }

    /**
     * 读写权限申请
     */
    fun readAndWrite(context: Context, readAndWriteCallback: () -> Unit) {
        this.readAndWriteCallback = readAndWriteCallback
        permission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, RESULT_CODE_OPEN_ALBUM, readAndWriteCallback)
    }

    /**
     * 录音权限申请
     */
    fun audio(context: Context, audioCallback: () -> Unit) {
        this.audioCallback = audioCallback
        permission(context, Manifest.permission.RECORD_AUDIO, RESULT_CODE_SOUND_RECORD, audioCallback)
    }

    /**
     * 定位权限申请
     */
    fun location(context: Context, locationCallback: () -> Unit) {
        this.locationCallback = locationCallback
        permissionList(context, permission, RESULT_CODE_LOCATION, locationCallback)
    }
    /**
     * 定位权限申请
     */
    /*fun location(context: Context, locationCallback: () -> Unit) {
        this.locationCallback = locationCallback
        permission(context, Manifest.permission.ACCESS_COARSE_LOCATION, RESULT_CODE_LOCATION, locationCallback)
    }*/
    /**
     * 权限申请结果
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size != 0) {
            val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

            when (requestCode) {
                RESULT_CODE_TAKE_CAMERA -> {    //拍照
                    if (cameraAccepted) {
                        cameraCallback?.let { it() }
                    } else {
                        //用户拒绝
                        ToastUtilss.show("请开启应用拍照权限")

                    }
                }
                RESULT_CODE_OPEN_ALBUM -> { //读写
                    if (cameraAccepted) {
                        readAndWriteCallback?.let { it() }
                    } else {
                        ToastUtilss.show("请开启应用读取权限")
                    }
                }
                RESULT_CODE_SOUND_RECORD -> { //录音
                    if (cameraAccepted) {
                        audioCallback?.let { it() }
                    } else {
                        ToastUtilss.show("请开启应用录音权限")
                    }
                }
                RESULT_CODE_LOCATION -> { //定位
                    if (cameraAccepted) {
                        locationCallback?.let { it() }
                    } else {
                        ToastUtilss.show("请开启应用定位权限")
                        //showPermissionDialog("定位")
                    }
                }
            }
        }
    }

    //权限申请
    private fun permission(context: Context, systemCode: String, resultCode: Int, callback: () -> Unit) {
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(context, systemCode) == PackageManager.PERMISSION_GRANTED) {
            callback()
        } else {
            //申请权限
            ActivityCompat.requestPermissions(context as Activity, arrayOf(systemCode), resultCode)
        }
    }

    /**
     * 多单个权限申请
     */
    private fun permissionList(context: Context, systemCode: Array<String>, resultCode: Int, callback: () -> Unit) {
        //判断是否有权限  ContextCompat.checkSelfPermission
        if (ContextCompat.checkSelfPermission(context, systemCode.toString()) == PackageManager.PERMISSION_GRANTED) {
            callback()
        } else {
            //申请权限
            ActivityCompat.requestPermissions(context as Activity, systemCode, resultCode)
        }
    }

    /**
     * 多个权限申请
     */
    private fun showPermissionDialog(string: String) {
        var permissionDialog = PermissionDialog(mContext, true)
        if (permissionDialog !== null) {
            permissionDialog.setListener(object : SelectDialogListener {
                override fun leftClick() {

                }

                override fun rightClick() {

                }
            })
            permissionDialog.setCanceledOnTouchOutside(false)
            permissionDialog.setCancelable(false)
            permissionDialog.showDialog()
            permissionDialog.setHint("警告！")
            permissionDialog.setTitle("为了保证应用正常，安全的运行，请允许应用获取" + string + "相关权限")
            permissionDialog.setLeftAndRightText("好的", "取消")
            //permissionDialog.setLeftText("知道了")
        }
    }
}