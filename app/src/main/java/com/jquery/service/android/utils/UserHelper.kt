package com.jquery.service.android.utils

import android.content.Context
import android.content.SharedPreferences
import com.jquery.service.android.entity.UserDetailEntity

/**
 * 用户信息工具类
 * @author j.query
 * @date 2018/12/25
 * @email j-query@foxmail.com
 */
class UserHelper {
    private lateinit var userInfo: UserDetailEntity
    private var token: String? = null

    private lateinit var userPreferences: SharedPreferences
    private val userKey = "user"

    /**
     * @param context
     * @return
     */
    @Synchronized
    private fun getUserSp(context: Context): SharedPreferences {
        userPreferences = context.getSharedPreferences(userKey, Context.MODE_PRIVATE)
        return userPreferences
    }

    /**
     * 获取当前用户信息
     *
     * @param context
     * @return
     */
    fun getUserInfo(context: Context): UserDetailEntity? {
        synchronized(UserDetailEntity::class.java) {
            userInfo = this.getUserInfoFromFile(context)!!
        }
        return userInfo
    }

    fun getToken(context: Context): String? {
        if (token != null && token?.isEmpty()!!) {
            synchronized(UserDetailEntity::class.java) {
                if (token != null && token?.isEmpty()!!) {
                    token = getTokenFromFile(context)
                }
            }
        }
        return token
    }

    /**
     * 更新基本信息
     *
     * @param context
     * @param entity
     */
    fun updateUserInfo(context: Context, entity: UserDetailEntity) {
        userInfo = entity
        UserHelper().saveData(context, "token", entity.token)
        UserHelper().saveData(context, "deviceId", entity.deviceId)
        UserHelper().saveData(context, "baseKey", entity.baseKey)
    }

    /**
     * 保存用户进行
     *
     * @param context
     * @param data
     */
    fun saveUserInfo(context: Context, data: UserDetailEntity) {
        userInfo = data
        val editor = getUserSp(context).edit()
        editor.putString("token", data.token)
        editor.putString("imei", data.imei)
        editor.putString("deviceId", data.deviceId)
        editor.putString("baseKey", data.baseKey)
        editor.putString("createTime", data.createTime)
        editor.putString("expireTime", data.expireTime)
        editor.apply()
    }

    fun saveUserInfo(context: Context, data: UserDetailEntity, t: String) {
        userInfo = data
        token = t
        val editor = getUserSp(context).edit()
        editor.putString("token", data.token)
        editor.putString("imei", data.imei)
        editor.putString("deviceId", data.deviceId)
        editor.putString("baseKey", data.baseKey)
        editor.putString("createTime", data.createTime)
        editor.putString("expireTime", data.expireTime)
        editor.apply()
    }

    /**
     * @param context
     */
    fun getUserInfoFromFile(context: Context): UserDetailEntity? {
        val userEntity = UserDetailEntity()
        userEntity.deviceId = getUserSp(context).getString("deviceId", "")
        if (userEntity.deviceId == null) {
            return null
        }
        userEntity.createTime = getUserSp(context).getString("createTime", "")
        userEntity.expireTime = getUserSp(context).getString("expireTime", "")
        userEntity.imei = getUserSp(context).getString("imei", "")
        userEntity.token = getUserSp(context).getString("token", "")
        userEntity.deviceId = getUserSp(context).getString("deviceId", "")
        userEntity.baseKey = getUserSp(context).getString("baseKey", "")
        return userEntity
    }

    /**
     * 保存数据
     *
     * @param context
     * @param key
     * @param data
     */
    fun saveData(context: Context, key: String, data: Any?) {
        if (data == null) {
            return
        }

        val type = data.javaClass.simpleName
        val editor = getUserSp(context).edit()

        if ("Integer" == type) {
            editor.putInt(key, (data as Int?)!!)
        } else if ("Boolean" == type) {
            editor.putBoolean(key, (data as Boolean?)!!)
        } else if ("String" == type) {
            editor.putString(key, data as String?)
        } else if ("Float" == type) {
            editor.putFloat(key, (data as Float?)!!)
        } else if ("Long" == type) {
            editor.putLong(key, (data as Long?)!!)
        }
        editor.apply()
    }


    fun getTokenFromFile(context: Context): String {
        return getUserSp(context).getString("token", "")
    }

    fun setTokenToFile(context: Context,tokenIn:String) {
        val editor = getUserSp(context).edit()
        editor.putString("token", tokenIn)
        editor.apply()
    }

    /**
     * 清除用户数据
     *
     * @param context
     */
    fun clear(context: Context) {
        getUserSp(context).edit().clear().apply()
        userInfo == null
        token = ""
    }

    /**
     * 清除用户token数据
     *
     * @param context
     */
    fun clearToken(context: Context) {
        userInfo.token = token
        token = ""
        val editor = getUserSp(context).edit()
        editor.putString("token", token)
        editor.apply()

    }

    fun saveRegisterToken(context: Context, token: String) {
        userInfo.registerToken = token
        val editor = getUserSp(context).edit()
        editor.putString("registerToken", token)
        editor.apply()
    }

    fun saveToken(context: Context, token: String) {
        userInfo.token = token
        val editor = getUserSp(context).edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun saveSessionKey(context: Context, sessionKey: String) {
        userInfo.sessionKey = sessionKey
        val editor = getUserSp(context).edit()
        editor.putString("sessionKey", sessionKey)
        editor.apply()
    }

    fun saveDeviceId(context: Context, deviceId: String) {
        userInfo.deviceId = deviceId
        val editor = getUserSp(context).edit()
        editor.putString("deviceId", deviceId)
        editor.apply()
    }

    fun saveBaseKey(context: Context, baseKey: String) {
        userInfo.baseKey = baseKey
        val editor = getUserSp(context).edit()
        editor.putString("baseKey", baseKey)
        editor.apply()
    }
}