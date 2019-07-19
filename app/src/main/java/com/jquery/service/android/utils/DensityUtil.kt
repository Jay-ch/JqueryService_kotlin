package com.jquery.service.android.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.jquery.service.android.Base.BaseFragmentActivity

/**
 * 像素转换工具类.
 * @author j.query
 * @date 2018/9/30
 * @email j-query@foxmail.com
 */
object DensityUtil {

    fun getScreenHeight(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        (context as BaseFragmentActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * dp转px
     * @param context
     * * @param dpVal
     * * @return     */
    fun dp2px(context: Context, dpFloat: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpFloat, context.resources.displayMetrics).toInt()
    }

    /**  sp转px
     * * @param context
     * * @param spVal     * @return
     * */
    fun sp2px(context: Context, spFloat: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spFloat, context.resources.displayMetrics).toInt()
    }

    /**
     * px转dp
     * @param context
     * *
     * @param pxVal
     * * @return     */
    fun px2dp(context: Context, pxFloat: Float): Float {
        var scale: Float = context.resources.displayMetrics.density
        return (pxFloat / scale)
    }

    /**
     * px转sp
     * @param pxVal
     * @param pxVal
     * @return
     * */
    fun px2sp(context: Context, pxFloat: Float): Float {
        return (pxFloat / context.resources.displayMetrics.scaledDensity)
    }

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
        var statusBarHeight = DensityUtil.dp2px(activity, 25f)
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}