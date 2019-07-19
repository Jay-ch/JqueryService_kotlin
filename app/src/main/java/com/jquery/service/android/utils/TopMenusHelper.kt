package com.jquery.service.android.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jquery.service.android.constant.Constants
import com.jquery.service.android.entity.MenuItemEntity

/**
 * 顶部菜单切换工具类
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class TopMenusHelper {
    private var topMenusSp: SharedPreferences? = null
    private val key = "menus"

    @Synchronized
    private fun getMenusSp(context: Context): SharedPreferences {
        if (topMenusSp == null) {
            topMenusSp = context.getSharedPreferences(Constants().TOP_MENUS, Context.MODE_PRIVATE)
        }
        return topMenusSp!!
    }

    fun saveMenus(context: Context, data: List<MenuItemEntity>) {
        val gson = Gson()
        val s = gson.toJson(data)
        getMenusSp(context).edit().putString(key, s).apply()
    }

    fun getMenus(context: Context): List<MenuItemEntity>? {
        val string = getMenusSp(context).getString(key, null)
        return getMenus(string)
    }

    fun getMenus(string: String?): List<MenuItemEntity>? {
        if (string != null) {
            val gson = Gson()
            return gson.fromJson<List<MenuItemEntity>>(string, object : TypeToken<List<MenuItemEntity>>() {

            }.type)
        }
        return null
    }
}