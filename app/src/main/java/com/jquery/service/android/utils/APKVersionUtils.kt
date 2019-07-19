package com.jquery.service.android.utils

import android.content.Context
import android.content.pm.PackageManager
import com.jquery.service.android.logger.LogUtil

/**
 * @author J.query
 * @date 2019/4/2
 * @email j-query@foxmail.com
 */
object APKVersionUtils {
    /**
     * 获取本地软件版本号
     */
    fun getLocalVersion(ctx: Context): Int {
        var localVersion = 0
        try {
            val packageInfo = ctx.applicationContext.packageManager.getPackageInfo(ctx.packageName, 0)
            localVersion = packageInfo.versionCode
            LogUtil.d("TAG", "本软件的版本号。。$localVersion")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return localVersion
    }

    /**
     * 获取本地软件版本号名称
     */
    fun getLocalVersionName(ctx: Context): String {
        var localVersion = ""
        try {
            val packageInfo = ctx.applicationContext
                    .packageManager
                    .getPackageInfo(ctx.packageName, 0)
            localVersion = packageInfo.versionName
            LogUtil.d("TAG", "本软件的版本号。。$localVersion")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return localVersion
    }
}