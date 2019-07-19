package com.jquery.service.android.utils

import android.content.Context
import android.util.TypedValue
import com.jquery.service.android.app.App

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
object DisplayUtils {

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(): Int {
        return App.mContext.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(): Int {
        return App.mContext.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context?): Int {
        return return App.mContext.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        return return App.mContext.resources.displayMetrics.widthPixels
    }


    /**
     * dp转换成px
     * @param context
     * @param dpVale
     * @return
     */
    fun dip2px(context: Context, dpVale: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpVale * scale + 0.5f).toInt()
    }

    /**
     * sp转换成px
     * @param context
     * @param sp
     * @return
     */
    fun dip2sp(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
    }

    /**
     * px转换成dp
     * @param context
     * @param pxValue
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}
