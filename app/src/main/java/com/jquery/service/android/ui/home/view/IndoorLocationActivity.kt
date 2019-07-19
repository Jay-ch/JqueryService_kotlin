package com.jquery.service.android.ui.home.view

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.jquery.service.android.R
import com.jquery.service.android.widgets.BaseStripAdapter
import com.jquery.service.android.widgets.StripListView

/**
 * 用来展示如何结合定位SDK实现室内定位，并使用MyLocationOverlay绘制定位位置
 * @author J.query
 * @date 2019/5/16
 * @email j-query@foxmail.com
 */
class IndoorLocationActivity : Activity() {
    // 定位相关
    private var mLocClient: LocationClient? = null
    var myListener = MyLocationListenner()
    private var mCurrentMode: MyLocationConfiguration.LocationMode? = null
    internal var mCurrentMarker: BitmapDescriptor? = null

    internal var mMapView: MapView? = null
    private var mBaiduMap: BaiduMap? = null

    private var stripListView: StripListView? = null
    private var mFloorListAdapter: BaseStripAdapter? = null
    internal var mMapBaseIndoorMapInfo: MapBaseIndoorMapInfo? = null
    private var mContext: Context? = null
    // UI相关

    private var requestLocButton: Button? = null
    private var isFirstLoc = true // 是否首次定位

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val layout = RelativeLayout(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mainview = inflater.inflate(R.layout.activity_location, null)
        layout.addView(mainview)
        requestLocButton = mainview.findViewById<View>(R.id.button1) as Button
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL
        requestLocButton?.text = "普通"
        val btnClickListener = View.OnClickListener {
            when (mCurrentMode) {
                MyLocationConfiguration.LocationMode.NORMAL -> {
                    requestLocButton?.text = "跟随"
                    mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING
                    mBaiduMap?.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                MyLocationConfiguration.LocationMode.COMPASS -> {
                    requestLocButton?.text = "普通"
                    mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL
                    mBaiduMap?.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                MyLocationConfiguration.LocationMode.FOLLOWING -> {
                    requestLocButton?.text = "罗盘"
                    mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS
                    mBaiduMap?.setMyLocationConfigeration(MyLocationConfiguration(mCurrentMode, true,
                            mCurrentMarker))
                }
                else -> {
                }
            }
        }
        requestLocButton?.setOnClickListener(btnClickListener)

        // 地图初始化
        mMapView = mainview.findViewById<View>(R.id.bmapView) as MapView
        mBaiduMap = mMapView!!.map
        // 开启定位图层
        mBaiduMap?.isMyLocationEnabled = true
        // 开启室内图
        mBaiduMap?.setIndoorEnable(true)
        // 定位初始化
        mLocClient = LocationClient(this)
        mLocClient?.registerLocationListener(myListener)
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(3000)
        mLocClient?.locOption = option
        mLocClient?.start()

        stripListView = StripListView(this)
        layout.addView(stripListView)
        setContentView(layout)
        mFloorListAdapter = BaseStripAdapter(this@IndoorLocationActivity)

        mBaiduMap?.setOnBaseIndoorMapListener(BaiduMap.OnBaseIndoorMapListener { b, mapBaseIndoorMapInfo ->
            if (b == false || mapBaseIndoorMapInfo == null) {
                stripListView?.visibility = View.INVISIBLE

                return@OnBaseIndoorMapListener
            }

            mFloorListAdapter?.setmFloorList(mapBaseIndoorMapInfo.floors)
            stripListView?.visibility = View.VISIBLE
            stripListView?.setStripAdapter(mFloorListAdapter)
            mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo
        })

    }


    /**
     * 定位SDK监听函数
     */
    inner class MyLocationListenner : BDAbstractLocationListener() {

        private var lastFloor: String? = null

        override fun onReceiveLocation(location: BDLocation?) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }
            val bid = location.buildingID
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.e("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo!!.id)
                if (bid == mMapBaseIndoorMapInfo!!.id) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    val floor = location.floor.toUpperCase()// 楼层
                    Log.e("indoor", "floor = " + floor + " position = " + mFloorListAdapter?.getPosition(floor))
                    Log.e("indoor", "radius = " + location.radius + " type = " + location.networkLocationType)

                    var needUpdateFloor = true
                    if (lastFloor == null) {
                        lastFloor = floor
                    } else {
                        if (lastFloor == floor) {
                            needUpdateFloor = false
                        } else {
                            lastFloor = floor
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap?.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo!!.id)
                        mFloorListAdapter?.setSelectedPostion(mFloorListAdapter?.getPosition(floor)!!)
                        mFloorListAdapter?.notifyDataSetInvalidated()
                    }

                    if (!location.isIndoorLocMode) {
                        mLocClient?.startIndoorMode()// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.e("indoor", "start indoormod")
                    }
                }
            }

            val locData = MyLocationData.Builder().accuracy(location.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100f).latitude(location.latitude).longitude(location.longitude).build()
            mBaiduMap?.setMyLocationData(locData)
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(location.latitude, location.longitude)
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(18.0f)
                mBaiduMap?.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
        }

        fun onReceivePoi(poiLocation: BDLocation) {}

        override fun onConnectHotSpotMessage(s: String?, i: Int) {}
    }

    override fun onPause() {
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        mMapView!!.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        // 退出时销毁定位
        mLocClient?.stop()
        // 关闭定位图层
        mBaiduMap?.isMyLocationEnabled = false
        mMapView?.onDestroy()
        mMapView = null
        super.onDestroy()
    }
}