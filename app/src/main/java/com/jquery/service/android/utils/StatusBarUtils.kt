package com.jquery.service.android.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import android.view.View
import android.view.Window
import android.view.WindowManager


/**
 * 状态栏设置
 * @author J.query
 * @date 2019/4/9
 * @email j-query@foxmail.com
 */
object StatusBarUtils {

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影（隐藏状态栏）
     */

    fun setStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            val decorView = activity.window.getDecorView()
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.setSystemUiVisibility(option)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.setStatusBarColor(Color.TRANSPARENT)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            val localLayoutParams = activity.window.getAttributes()
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            activity.window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN )
//            if (isLightColor(color)) {
//                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else {
//                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//            }
        }
    }

    /**
     * 沉侵式状态栏（不隐藏状态栏）
     */

    fun setSubmergedStatusBar(activity: Activity, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            val decorView = activity.window.decorView
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            val localLayoutParams = activity.window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        /*  //修改字体颜色
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
              activity.window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
              activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
              *//* window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.setStatusBarColor(Color.WHITE)*//*
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色颜色
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = color

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    fun setStatusBarColor(activity: Activity, colorId: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            //      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = activity.resources.getColor(colorId)
        }
        //        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
        //            transparencyBar(activity);
        //            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        //            tintManager.setStatusBarTintEnabled(true);
        //            tintManager.setStatusBarTintResource(colorId);
        //        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    fun StatusBarLightMode(activity: Activity): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                result = 2
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                result = 3
            }
        }
        return result
    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    fun StatusBarLightMode(activity: Activity, type: Int) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity, true)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.window, true)
        } else if (type == 3) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    fun StatusBarDarkMode(activity: Activity, type: Int) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity, false)
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.window, false)
        } else if (type == 3) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window!!.getAttributes()
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window!!.setAttributes(lp)
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {

            }

        }
        return result
    }


    /*     */
    /**
     * 获取StatusBar颜色，默认白色
     *
     * @return
     *//*
        protected val statusBarColor: Int
            @ColorInt get() = Color.WHITE

        override fun setContentView(layoutResID: Int) {
            super.setContentView(layoutResID)
            setStatusBar(statusBarColor)
        }

        override fun setContentView(view: View) {
            super.setContentView(view)
            setStatusBar(statusBarColor)
        }

        */
    /**
     * Android 6.0 以上设置状态栏颜色
     *//*
        protected fun setStatusBar(@ColorInt color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // 设置状态栏底色颜色
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = color

                // 如果亮色，设置状态栏文字为黑色
                if (isLightColor(color)) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }

        }

        */
    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    fun isLightColor(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) >= 0.5
    }

}
