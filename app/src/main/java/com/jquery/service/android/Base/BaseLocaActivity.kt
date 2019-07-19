package com.jquery.service.android.Base

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.permission.PermissionsActivity
import com.jquery.service.android.permission.PermissionsChecker
import com.jquery.service.android.ui.login.view.LoginActivity
import com.jquery.service.android.utils.DisplayUtils
import com.jquery.service.android.utils.UserHelper



/**
 * @author J.query
 * @date 2019/3/26
 * @email j-query@foxmail.com
 */
abstract class BaseLocaActivity : BaseActivity(), ImBaseView {
    protected var keyHeight = 0

    //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
    private val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val LOCATION_PERMISSION = 202

    protected var userInfoEntity: UserDetailEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userInfoEntity = UserHelper().getUserInfo(this)
        if (userInfoEntity == null) {
            startActivity(LoginActivity::class.java)
            finish()
        } else {
            checkPermissions()
            keyHeight = DisplayUtils.getScreenHeight(this) / 3
            //event().addEventHandler(this)
        }
    }


    private fun checkPermissions() {
        var checker = PermissionsChecker(this)
        var sb = StringBuffer()
        for (i in 0 until permission.size) {
            sb.append(permission[i])
        }
        var s = sb.toString()
        if (checker.lacksPermissions(permission)) {
            PermissionsActivity().startActivityForResult(this, LOCATION_PERMISSION, permission)
        } else {
            initViewAndData()
        }
    }

    protected abstract fun initViewAndData()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.e(TAG, "onActivityResult:定位回调 ")
        if (requestCode == LOCATION_PERMISSION) {
            if (resultCode == PermissionsActivity().PERMISSIONS_GRANTED) {
                initViewAndData()
            } else {
                this.finish()
            }
        }
    }

    /**
     * loading
     *
     * @param title
     */
    override fun showLoading(title: String?) {
        showBaseLoading(title)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKeyPressed()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    protected abstract fun onBackKeyPressed()
}