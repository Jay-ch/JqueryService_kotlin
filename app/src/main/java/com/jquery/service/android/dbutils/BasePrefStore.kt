package com.jquery.service.android.dbutils

import android.content.Context
import android.content.SharedPreferences
import com.jquery.service.android.app.App

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
open class BasePrefStore {
    private lateinit var me: BasePrefStore
    //private var me: BasePrefStore

    private val PREFERENCE_COMMON = "preference.common"
    private val PREFERENCE_USER = "preference.user"

    constructor()

    open fun getInstance(): BasePrefStore? {
        if (me == null) {
            me = BasePrefStore()
        }
        return me
    }

    protected fun commonPreference(): SharedPreferences? {
        return this.getContext()?.getSharedPreferences("preference.common", 0)
    }

    protected fun userPreference(): SharedPreferences? {
        return this.getContext()?.getSharedPreferences("preference.user", 0)
    }

    protected fun commonPreferenceEditor(): SharedPreferences.Editor? {
        return this.commonPreference()?.edit()
    }


    private fun getContext(): Context? {
        return App.mContext
    }
}