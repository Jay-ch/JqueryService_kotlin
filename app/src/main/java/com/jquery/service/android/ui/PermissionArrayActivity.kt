package com.jquery.service.android.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import java.util.*

/**
 * @author J.query
 * @date 2019/6/13
 * @email j-query@foxmail.com
 */
class PermissionArrayActivity : BaseActivity() {

    private var needPermission: MutableList<String>? = null

    private val REQUEST_CODE_PERMISSION = 0

    private var permissionArrays = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION)


    //申请两个权限，录音和文件读写
    //1、首先声明一个数组permissions，将需要的权限都放在里面
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    internal var mPermissionList: MutableList<String> = ArrayList()

    private val mRequestCode = 100//权限请求码

    override fun createLayout(): Int {
        return R.layout.activity_main
    }

    override fun setStatusBar() {

    }

    override fun initViews() {
        super.initViews()
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission()
        }
    }


    //权限判断和申请
    private fun initPermission() {

        mPermissionList.clear()//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i])//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode)
        } else {
            //说明权限都已经通过，可以做你想做的事情去
            showToast("权限都已经通过")
        }
    }


    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionDismiss = false//有权限没有通过
        if (mRequestCode == requestCode) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog()//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                //全部权限通过，可以进行下一步操作。。。

            }
        }

    }


    /**
     * 不再提示权限时的展示对话框
     */
    internal var mPermissionDialog: AlertDialog? = null
    internal var mPackName = "com.huawei.liwenzhi.weixinasr"

    private fun showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置") { dialog, which ->
                        cancelPermissionDialog()

                        val packageURI = Uri.parse("package:$mPackName")
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        startActivity(intent)
                    }
                    .setNegativeButton("取消") { dialog, which ->
                        //关闭页面或者做其他操作
                        cancelPermissionDialog()
                    }
                    .create()
        }
        mPermissionDialog!!.show()
    }

    //关闭对话框
    private fun cancelPermissionDialog() {
        mPermissionDialog!!.cancel()
    }
}