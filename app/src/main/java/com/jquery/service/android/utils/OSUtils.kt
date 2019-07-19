package com.jquery.service.android.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 * @author J.query
 * @date 2019/7/2
 * @email j-query@foxmail.com
 */
 class OSUtils {


    private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_EMUI_VERSION_CODE = "ro.build.hw_emui_api_level"
    private val KEY_FLYME_ID_FALG_KEY = "ro.build.display.id"
    private val KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon"
    private val KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme"
    private val KEY_FLYME_PUBLISH_FALG = "ro.flyme.published"
    private var mProper: Properties? = null

    var mPhoneInfoUtil: OSUtils? = null

    enum class ROM_TYPE {

        MIUI_ROM,

        FLYME_ROM,

        EMUI_ROM,

        OTHER_ROM

    }

    constructor() {
        if (mProper == null) {
            mProper = Properties()
        }
        try {
            mProper!!.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    ;


    fun getInstance(): OSUtils? {
        if (mPhoneInfoUtil == null) {
            synchronized(OSUtils::class.java) {
                if (mPhoneInfoUtil == null) {
                    mPhoneInfoUtil = OSUtils()
                }
            }
        }
        return mPhoneInfoUtil
    }


    /**
     * 获取手机厂商
     *
     * @return
     */
    fun getDeviceBrand(): String {
        return android.os.Build.BRAND
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    fun getPhoneModel(): String {
        return android.os.Build.MODEL
    }


    /**
     * 获取系统版本号
     *
     * @return
     */
    fun getSystemVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }


    /**
     * 获取当前应用版本
     *
     * @return
     */
    fun getVersion(context: Context): String {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    /**
     * 判断ROM是否为MIUI
     *
     * @return
     */
    fun isMIUI(): Boolean {

        return mProper!!.containsKey(KEY_MIUI_VERSION_CODE) || mProper!!.containsKey(KEY_MIUI_VERSION_NAME)
    }

    /**
     * 判断ROM是否为EMUI
     *
     * @param
     * @return
     */
    fun isEMUI(): Boolean {

        return mProper!!.containsKey(KEY_EMUI_VERSION_CODE)
    }


    /**
     * 判断ROM是否为Flyme
     *
     * @return
     */
    fun isFlyme(): Boolean {
        return mProper!!.containsKey(KEY_FLYME_ICON_FALG) || mProper!!.containsKey(KEY_FLYME_SETUP_FALG) || mProper!!.containsKey(KEY_FLYME_PUBLISH_FALG)
    }


    /**
     * 获取ROM版本信息
     *
     * @return
     */
    fun getRomInfo(): String {
        return if (isMIUI()) {
            ROM_TYPE.MIUI_ROM.toString() + " " + mProper!!.getProperty(KEY_MIUI_VERSION_NAME)
        } else if (isFlyme()) {
            ROM_TYPE.FLYME_ROM.toString() + " " + mProper!!.getProperty(KEY_FLYME_ID_FALG_KEY)
        } else if (isEMUI()) {
            ROM_TYPE.EMUI_ROM.toString() + " " + mProper!!.getProperty(KEY_EMUI_VERSION_CODE)
        } else {
            ROM_TYPE.OTHER_ROM.toString() + ""
        }


    }
}