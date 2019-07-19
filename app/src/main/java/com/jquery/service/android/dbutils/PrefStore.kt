package com.jquery.service.android.dbutils

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
class PrefStore : BasePrefStore() {
    private val LIST_HOME_JSON = "list_home_json"
    private val CUR_API_HOST = "cur_api_host"
    private var me: PrefStore? = null

    override fun getInstance(): PrefStore? {
        if (me == null) {
            synchronized(PrefStore::class.java) {
                if (me == null) {
                    me = PrefStore()
                }
            }
        }
        return me
    }


    fun cacheList(json: String) {
        commonPreference()?.edit()?.putString(LIST_HOME_JSON, json)?.apply()
    }

    fun getListGiftJson(): String? {
        return commonPreference()?.getString(LIST_HOME_JSON, "")
    }

    fun setCurApiHost(host: String) {
        commonPreference()?.edit()?.putString(CUR_API_HOST, host)?.apply()
    }

    fun getCurApiHost(): String? {
        return commonPreference()?.getString(CUR_API_HOST, "official")
    }
}