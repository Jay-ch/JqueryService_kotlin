package com.jquery.service.android.utils

import android.content.Context
import android.util.Log
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * 百度定位Service
 * @author J.query
 * @date 2019/4/2
 * @email j-query@foxmail.com
 */
class BDLocationServices {
    private val TAG = "BDLocationService"
    private var mClient: LocationClient? = null
    private var mOption: LocationClientOption? = null
    //locationService_status==true
    var locationService_status: Boolean = false
    @Volatile
    private var sInstance: BDLocationServices? = null

    constructor(context: Context) : super() {
        mClient = LocationClient(context)
        mClient?.locOption = getDefaultLocationClientOption()
    }

    /**
     * @param context applicationContext
     * @return
     */
    fun getSingleton(context: Context): BDLocationServices? {
        if (sInstance == null) {
            synchronized(BDLocationServices::class.java) {
                if (sInstance == null) {
                    sInstance = BDLocationServices(context)
                }
            }
        }
        return sInstance
    }

    fun registerLocationListener(listener: BDLocationListener?): Boolean {
        var isSuccess = false
        if (listener != null) {
            mClient?.registerLocationListener(listener)
            isSuccess = true
        }
        return isSuccess
    }

    fun unRegisterLocationListener(listener: BDLocationListener?) {
        if (listener != null) {
            mClient?.unRegisterLocationListener(listener)
        }
    }

    /**
     * start locate
     */
    /*  @Synchronized
      fun start() {
          if (mClient != null && !mClient!!.isStarted) {
              mClient?.start()
              locationService_status = true
          } else {
              locationService_status = false
              Log.i(TAG, "System.out start: 失败")
          }
      }*/
    @Synchronized
    fun start(): Boolean {
        if (mClient != null && !mClient!!.isStarted) {
            mClient?.start()
            locationService_status = true
        } else {
            locationService_status = false
            Log.i(TAG, "System.out start: 失败")
        }
        return locationService_status
    }

    /**
     * stop locate
     */
    @Synchronized
    fun stop() {
        if (mClient != null && mClient!!.isStarted) {
            mClient?.stop()
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    fun setLocationOption(option: LocationClientOption?): Boolean {
        var isSuccess = false
        if (option != null) {
            if (mClient?.isStarted!!)
                mClient?.stop()
            mOption = option
            mClient?.locOption = option
            isSuccess = true
        }
        return isSuccess
    }


    fun getDefaultLocationClientOption(): LocationClientOption {
        if (mOption == null) {
            mOption = LocationClientOption()
            mOption!!.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption!!.setCoorType("bd09ll")//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption!!.setScanSpan(0)//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption!!.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
            mOption!!.setIsNeedLocationDescribe(true)//可选，设置是否需要地址描述
            mOption!!.setNeedDeviceDirect(false)//可选，设置是否需要设备方向结果
            mOption!!.isLocationNotify = false//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption!!.setIgnoreKillProcess(true)//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption!!.setIsNeedLocationDescribe(true)//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption!!.setIsNeedLocationPoiList(false)//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption!!.SetIgnoreCacheException(false)//可选，默认false，设置是否收集CRASH信息，默认收集

            mOption!!.setIsNeedAltitude(false)//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption as LocationClientOption
    }
}