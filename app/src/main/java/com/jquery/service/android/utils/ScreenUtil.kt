package com.jquery.service.android.utils

import android.app.Activity
import android.util.DisplayMetrics

/** 屏幕相关的工具类.
 * @author j.query
 * @date 2018/12/25
 * @email j-query@foxmail.com
 */
class ScreenUtil {
    fun getDisplayMetrics(activity: Activity): DisplayMetrics {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    fun getScreenWidth(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    /**
     * 获取系统状态栏的高度
     *
     * @param activity activity
     * @return 状态栏的高度
     */
    fun getStatusBarHeight(activity: Activity): Int {
        var statusBarHeight = DensityUtils.dp2px(activity, 25f)
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}