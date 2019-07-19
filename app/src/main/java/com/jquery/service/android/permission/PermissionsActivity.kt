package com.jquery.service.android.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.jquery.service.android.R

/**
 * 权限适配
 * @author J.query
 * @date 2018/9/10
 * @email j-query@foxmail.com
 */
class PermissionsActivity : AppCompatActivity() {
    val PERMISSIONS_GRANTED = 0 // 权限授权
    val PERMISSIONS_DENIED = 1 // 权限拒绝

    private val PERMISSION_REQUEST_CODE = 0 // 系统权限管理页面的参数
    private val EXTRA_PERMISSIONS = "me.chunyu.clwang.permission.extra_permission" // 权限参数
    private val PACKAGE_URL_SCHEME = "package:" // 方案

    private var mChecker: PermissionsChecker? = null // 权限检测器
    private var isRequireCheck: Boolean = false // 是否需要系统权限检测

    fun startActivityForResult(activity: Activity, requestCode: Int, vararg permissions: Array<String>) {
        val intent = Intent(activity, PermissionsActivity::class.java)
        intent.putExtra(EXTRA_PERMISSIONS, permissions)
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            throw RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!")
        }
        setContentView(R.layout.activity_permissions)
        mChecker = PermissionsChecker(this)
        isRequireCheck = true
    }

    private var permissions: Array<String>?=null

    override fun onResume() {
        super.onResume()
        if (isRequireCheck) {
             permissions = getPermissions()
            if (mChecker?.lacksPermissions(permissions!!)!!) {
                requestPermissions(*permissions!!) // 请求权限
            } else {
                allPermissionsGranted() // 全部权限都已获取
            }
        } else {
            isRequireCheck = true
        }
    }
    //val name:Array<String>
    // 返回传递的权限参数
    private fun getPermissions(): Array<String> {
        return intent.getStringArrayExtra(EXTRA_PERMISSIONS)
    }

    // 请求权限兼容低版本
    private fun requestPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    // 全部权限均已获取
    private fun allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED)
        finish()
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true
            allPermissionsGranted()
        } else {
            isRequireCheck = false
            showMissingPermissionDialog()
        }
    }

    // 含有全部的权限
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    // 显示缺失权限提示
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this@PermissionsActivity)
        builder.setTitle(R.string.help_permissions)
        builder.setMessage(R.string.string_help_text)

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit) { dialog, which ->
            setResult(PERMISSIONS_DENIED)
            finish()
        }

        builder.setPositiveButton(R.string.settings) { dialog, which -> startAppSettings() }

        builder.show()
    }

    // 启动应用的设置
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }
}