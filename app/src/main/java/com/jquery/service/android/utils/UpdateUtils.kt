package com.jquery.service.android.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

/**
 * @author J.query
 * @date 2019/5/29
 * @email j-query@foxmail.com
 */
object UpdateUtils{
    /**
     * 判断SDcard是否可用
     * @return
     */
    fun sdcardExists(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /**
     * 获取SDcard的总存储量,返回-1则不存在
     * @return
     */
    fun getTotalSpace(): Long {
        if (sdcardExists()) {
            val file = Environment.getExternalStorageDirectory()
            return file.totalSpace//文件的总大小（此方法应用于8以上，需要在此方法打上NewApi的注解）
        } else {
            return -1
        }

    }

    /**
     * 获取SDcard的剩余存储量,返回-1则不存在
     * @return
     */
    fun getUsableSpace(): Long {
        if (sdcardExists()) {
            val file = Environment.getExternalStorageDirectory()
            return file.usableSpace//文件的总大小（此方法应用于8以上，需要在此方法打上NewApi的注解）
        } else {
            return -1
        }
    }

    /**
     * 安装apk包
     * @param context Context 上下文对象
     * @param apkFile File apk文件对象
     */
    fun installApk(context: Context, apkFile: File) {
        Log.e("OpenFile", apkFile.name)
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * 安装apk包
     * @param context Context 上下文对象
     * @param apkFile String 文件路径
     */
    fun installApk(context: Context, apkFile: String) {
        installApk(context, File(apkFile))
    }


    /**
     * 判断你是否在为wifi环境
     * @param context
     * @return
     */
    fun isWifi(context: Context): Boolean {

        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI) {
            true
        } else false
    }
}