package com.jquery.service.android.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.IntDef
import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClientOption
import com.baidu.location.Poi
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.baidu.mapapi.map.InfoWindow
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.model.LatLng
import com.jquery.service.android.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 百度定位Helper
 * @author J.query
 * @date 2019/3/25
 * @email j-query@foxmail.com
 */
class LocationHelper {
    private var locationService: BDLocationServices? = null
    private var option: LocationClientOption? = null
    var locationStart: Boolean = false
    private var mInfoWindow: InfoWindow? = null//地图文字位置提醒
    private var context: Context? = null

    constructor(context: Context) : super() {
        (Builder(context))
    }

    constructor(builder: Builder) : super() {
        this.locationService = builder.mLocationService
        this.option = builder.mOption
    }

    fun registerLocationListener(listener: LocationListener): Boolean {
        return locationService?.registerLocationListener(listener)!!
    }

    fun unRegisterLocationListener(listener: LocationListener) {
        locationService?.unRegisterLocationListener(listener)!!
    }

    /*    @Synchronized
        fun start(): Boolean*/
    @Synchronized
    fun start(): Boolean {
        if (locationService?.start() == true) {
            locationService?.start()
            locationStart = true
        } else {
            locationStart = false
        }
        return locationStart
    }

    fun stop() {
        locationService?.stop()
    }

    class Builder(context: Context) {

        internal var mOption: LocationClientOption
        internal var mLocationService: BDLocationServices

        @IntDef(LOCATION_MODE_HIGH_ACCURACY, LOCATION_MODE_BATTERY_SAVING, LOCATION_MODE_DEVICE_SENSORS)
        @Retention(RetentionPolicy.SOURCE)
        annotation class LocateMode

        init {
            this.mLocationService = BDLocationServices(context).getSingleton(context)!!
            this.mOption = mLocationService.getDefaultLocationClientOption()
        }

        /**
         * //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
         *
         * @param mode one of [.LOCATION_MODE_HIGH_ACCURACY], [.LOCATION_MODE_BATTERY_SAVING] ,or [.LOCATION_MODE_DEVICE_SENSORS]
         */
        fun setLocationMode(@LocateMode mode: Int): Builder {
            var bdMode: LocationClientOption.LocationMode = LocationClientOption.LocationMode.Hight_Accuracy
            when (mode) {
                LOCATION_MODE_BATTERY_SAVING -> bdMode = LocationClientOption.LocationMode.Battery_Saving
                LOCATION_MODE_DEVICE_SENSORS -> bdMode = LocationClientOption.LocationMode.Device_Sensors
            }
            mOption.locationMode = bdMode
            return this
        }

        /**
         * 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
         * @param coorType
         */
        fun setCoorType(coorType: String): Builder {
            mOption.setCoorType(coorType)
            return this
        }

        /**
         * 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
         * @param milliseconds
         */
        fun setScanSpan(milliseconds: Int): Builder {
            mOption.setScanSpan(milliseconds)
            return this
        }

        /**
         * 可选，设置是否需要地址信息，默认不需要
         * @param isNeed
         */
        fun setIsNeedAddress(isNeed: Boolean): Builder {
            mOption.setIsNeedAddress(isNeed)
            return this
        }

        /**
         * 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
         * @param isNeed
         */
        fun setIsNeedLocationDescribe(isNeed: Boolean): Builder {
            mOption.setIsNeedLocationDescribe(isNeed)
            return this
        }

        /**
         * 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
         * @param isNeed
         */
        fun setIsNeedAltitude(isNeed: Boolean): Builder {
            mOption.setIsNeedAltitude(isNeed)
            return this
        }

        /**
         * 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
         * @param isNeed
         */
        fun setIsNeedLocationPoiList(isNeed: Boolean): Builder {
            mOption.setIsNeedLocationPoiList(isNeed)
            return this
        }

        /**
         * 可选，设置是否需要设备方向结果
         * @param isNeed
         */
        fun setNeedDeviceDirect(isNeed: Boolean): Builder {
            mOption.setNeedDeviceDirect(isNeed)
            return this
        }

        /**
         * 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
         * @param isNotify
         */
        fun setLocationNotify(isNotify: Boolean): Builder {
            mOption.isLocationNotify = isNotify
            return this
        }

        /**
         * 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
         * @param isIgnore
         */
        fun setIgnoreKillProcess(isIgnore: Boolean): Builder {
            mOption.setIgnoreKillProcess(isIgnore)
            return this
        }

        /**
         * 可选，默认false，设置是否收集CRASH信息，默认收集
         * @param isIgnore
         */
        fun SetIgnoreCacheException(isIgnore: Boolean): Builder {
            mOption.SetIgnoreCacheException(isIgnore)
            return this
        }

        fun build(): LocationHelper {
            return LocationHelper(this)
        }

        companion object {
            const val LOCATION_MODE_HIGH_ACCURACY = 1
            const val LOCATION_MODE_BATTERY_SAVING = 2
            const val LOCATION_MODE_DEVICE_SENSORS = 3
        }
    }

     fun customMarker(mContent:Context,mBaiduMap: BaiduMap?,latitude: Double?, longitude: Double?) {
        //目的地图标
        //setMarkerOptions(latitude,latitude,context.resources.getDrawable(R.drawable.arrive_icon))  context.resources.getDrawable(R.drawable.arrive_icon)
        setMarkerOptions(mContent,mBaiduMap,latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } },R.drawable.arrive_icon)
        setTextOption(mContent,mBaiduMap,latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }, "维修地址", "#7ED321")
    }

    /**
     * 设置marker覆盖物
     *
     * @param ll   坐标
     * @param icon 图标
     */
     fun setMarkerOptions(mContent:Context,mBaiduMap: BaiduMap?,ll: LatLng?, icon: Int) {
        mBaiduMap?.removeMarkerClickListener(onMarkerClickListener)
        mBaiduMap?.clear()
        if (ll == null) return
        val bitmap = BitmapDescriptorFactory.fromResource(icon)
        val ooD = MarkerOptions().position(ll).icon(bitmap)
        mBaiduMap?.addOverlay(ooD)
    }
    /**
     * 添加地图文字
     *
     * @param point
     * @param str
     * @param color 字体颜色
     */
     fun setTextOption(mContent:Context,mBaiduMap: BaiduMap?,point: LatLng?, str: String, color: String) {
        //使用MakerInfoWindow
        if (point == null) return
        val view = TextView(mContent)
        view.setBackgroundResource(R.drawable.map_textbg)
        view.setPadding(0, 23, 0, 0)
        view.typeface = Typeface.DEFAULT_BOLD
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.text = str
        view.setTextColor(Color.parseColor(color))
        mInfoWindow = InfoWindow(view, point, 100)
        mBaiduMap?.showInfoWindow(mInfoWindow)
    }
    /**
     * 移除Marker的监听
     */
    internal var onMarkerClickListener: BaiduMap.OnMarkerClickListener = BaiduMap.OnMarkerClickListener {
        //在这里我做了跳转页面

        false
    }

    /**
     * 定位信息
     */
    class LocationEntity(internal var bdLocation: BDLocation) {

        val time: String
            get() = this.bdLocation?.time

        // 纬度
        val latitude: Double
            get() = this.bdLocation?.latitude

        // 经度
        val longitude: Double
            get() = this.bdLocation?.longitude

        val countryCode: String
            get() = this.bdLocation?.countryCode

        val country: String
            get() = this.bdLocation?.country

        // 地址信息
        val addrStr: String
            get() = this.bdLocation?.addrStr

        val province: String
            get() = this.bdLocation?.province

        val city: String
            get() = this.bdLocation?.city

        val cityCode: String
            get() = this.bdLocation?.cityCode

        // 区
        val district: String
            get() = this.bdLocation?.district

        // 街道
        val street: String
            get() = this.bdLocation?.street

        val streetNumber: String
            get() = this.bdLocation?.streetNumber

        val locationDescribe: String
            get() = this.bdLocation?.locationDescribe

        //方向
        val direction: Float
            get() = this.bdLocation?.direction

        // GPS定位结果  海拔高度 单位：米
        // 网络定位结果  单位：米
        val altitude: Double
            get() = if (this.bdLocation?.hasAltitude()) {
                bdLocation?.altitude
            } else {
                0.0
            }

        override fun toString(): String {
            if (null != bdLocation && bdLocation?.locType != BDLocation.TypeServerError) {
                val sb = StringBuffer(256)
                sb.append("time : ")
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * bdLocation.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(bdLocation?.time)
                sb.append("\nlocType : ")// 定位类型
                sb.append(bdLocation?.locType)
                sb.append("\nlocType description : ")// *****对应的定位类型说明*****
                sb.append(bdLocation?.locTypeDescription)
                sb.append("\nlatitude : ")// 纬度
                sb.append(bdLocation?.latitude)
                sb.append("\nlontitude : ")// 经度
                sb.append(bdLocation?.longitude)
                sb.append("\nradius : ")// 半径
                sb.append(bdLocation?.radius)
                sb.append("\nCountryCode : ")// 国家码
                sb.append(bdLocation?.countryCode)
                sb.append("\nCountry : ")// 国家名称
                sb.append(bdLocation?.country)
                sb.append("\ncitycode : ")// 城市编码
                sb.append(bdLocation?.cityCode)
                sb.append("\ncity : ")// 城市
                sb.append(bdLocation?.city)
                sb.append("\nDistrict : ")// 区
                sb.append(bdLocation?.district)
                sb.append("\nStreet : ")// 街道
                sb.append(bdLocation?.street)
                sb.append("\naddr : ")// 地址信息
                sb.append(bdLocation?.addrStr)
                sb.append("\nUserIndoorState: ")// *****返回用户室内外判断结果*****
                sb.append(bdLocation?.userIndoorState)
                sb.append("\nDirection(not all devices have value): ")
                sb.append(bdLocation?.direction)// 方向
                sb.append("\nlocationdescribe: ")
                sb.append(bdLocation?.locationDescribe)// 位置语义化信息
                sb.append("\nPoi: ")// POI信息
                if (bdLocation?.poiList != null && !bdLocation?.poiList.isEmpty()) {
                    for (i in 0 until bdLocation?.poiList.size) {
                        val poi = bdLocation?.poiList[i] as Poi
                        sb.append(poi.name + ";")
                    }
                }
                if (bdLocation?.locType == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ")
                    sb.append(bdLocation?.speed)// 速度 单位：km/h
                    sb.append("\nsatellite : ")
                    sb.append(bdLocation?.satelliteNumber)// 卫星数目
                    sb.append("\nheight : ")
                    sb.append(bdLocation?.altitude)// 海拔高度 单位：米
                    sb.append("\ngps status : ")
                    sb.append(bdLocation?.gpsAccuracyStatus)// *****gps质量判断*****
                    sb.append("\ndescribe : ")
                    sb.append("gps定位成功")
                } else if (bdLocation?.locType == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (bdLocation?.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ")
                        sb.append(bdLocation?.altitude)// 单位：米
                    }
                    sb.append("\noperationers : ")// 运营商信息
                    sb.append(bdLocation?.operators)
                    sb.append("\ndescribe : ")
                    sb.append("网络定位成功")
                } else if (bdLocation?.locType == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ")
                    sb.append("离线定位成功，离线定位结果也是有效的")
                } else if (bdLocation?.locType == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ")
                    sb.append("服务端网络定位失败")
                } else if (bdLocation?.locType == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ")
                    sb.append("网络不同导致定位失败，请检查网络是否通畅")
                } else if (bdLocation?.locType == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ")
                    sb.append("无法获取有效定位依据导致定位失败，可以试着重启手机")
                }
                return sb.toString()
            }
            return ""
        }
    }

    abstract class LocationListener : BDLocationListener {

        override fun onReceiveLocation(bdLocation: BDLocation?) {
            var radius = bdLocation?.getRadius()    //获取定位精度，默认值为0.0f
            if (null != bdLocation && bdLocation.locType != BDLocation.TypeServerError) {
                val location = LocationEntity(bdLocation)
                onReceiveLocation(location)
                var errorMsg: String? = null
                if (bdLocation.locType == BDLocation.TypeServerError) {
                    errorMsg = "服务端网络定位失败"
                } else if (bdLocation.locType == BDLocation.TypeNetWorkException) {
                    errorMsg = "网络不同导致定位失败，请检查网络是否通畅"
                } else if (bdLocation.locType == BDLocation.TypeCriteriaException) {
                    errorMsg = "无法获取有效定位依据导致定位失败，可以试着重启手机"
                }
                if (!TextUtils.isEmpty(errorMsg)) {
                    onError(Throwable(errorMsg))
                }
            } else {
                onError(Throwable("uncaught exception"))
            }

        }

        abstract fun onReceiveLocation(location: LocationEntity)

        open fun onError(e: Throwable) {

        }
    }
}