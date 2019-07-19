package com.jquery.service.android.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.WindowManager

/**
 * 设置状态栏
 * @author J.query
 * @date 2019/5/9
 * @email j-query@foxmail.com
 */
class NewStatusBarUtils {
    private val INVALID_VAL = -1
    private val COLOR_DEFAULT = Color.GREEN


    fun setStatusBarUtils(activity: Activity, isShow: Boolean, statusBar: View) {
        setStatusBarUtils(activity, isShow, statusBar, INVALID_VAL)
    }


    fun setStatusBarUtils(activity: Activity, isShow: Boolean, statusBar: View, statusColor: Int) {
        var statusColor = statusColor
        //设置状态栏默认颜色
        if (isShow) {
            if (statusColor == INVALID_VAL) {
                statusColor = ActivityCompat.getColor(activity, statusColor)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //version_code > 5.0
            setHideStatusBar21(activity)
            if (isShow) {
                val layoutParams = statusBar.layoutParams
                layoutParams.height = getStatusBarHeight(activity)
                statusBar.visibility = View.VISIBLE
                statusBar.setBackgroundColor(statusColor)
            } else {
                statusBar.visibility = View.GONE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //4.4 <= version_code<5.0
            setHideStatusBar19(activity)
            if (isShow) {
                val layoutParams = statusBar.layoutParams
                layoutParams.height = getStatusBarHeight(activity)
                statusBar.visibility = View.VISIBLE
                statusBar.setBackgroundColor(statusColor)
            } else {
                statusBar.visibility = View.GONE
            }
        } else {
            Log.d(NewStatusBarUtils::class.java.simpleName, ":setStatusBarUtils 4.4以下")
        }
    }

    //4.4~~5.0隐藏状态栏
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun setHideStatusBar19(activity: Activity) {
        val window = activity.window
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        //        StatusBarHelper.from(activity).setTransparentStatusbar(true).process();

        val winParams = window.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        winParams.flags = winParams.flags or bits
        window.attributes = winParams
    }

    //5.0++隐藏状态栏
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setHideStatusBar21(activity: Activity) {  //隐藏5.0++状态栏
        val window = activity.window

        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.navigationBarColor = Color.TRANSPARENT

        /* if (AndroidWorkaround.checkDeviceHasNavigationBar(activity)) {
                AndroidWorkaround.assistActivity(activity.findViewById(android.R.id.content));
            }*/
    }

    //获取状态栏高度，网上的教程很多都是这个方法一模一样= = 果然天下代码一大抄 哈哈哈
    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}