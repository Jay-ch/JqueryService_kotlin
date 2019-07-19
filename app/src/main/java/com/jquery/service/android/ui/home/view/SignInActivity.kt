package com.jquery.service.android.ui.home.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.CoordUtil
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.luck.picture.lib.permissions.Permission
import com.luck.picture.lib.permissions.RxPermissions
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.widgets.dialog.SelectDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_map.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 打卡模拟
 * @author J.query
 * @date 2019/4/11
 * @email j-query@foxmail.com
 */
class SignInActivity : BaseActivity(), SensorEventListener, View.OnClickListener {
    private val REQUEST_CODE_CHOOSE_LOCATION_GALLERY = 888
    private val TAG = "SignInActivity"

    /**
     * 规定到达距离范围距离
     */
    private val DISTANCE = 200

    private var mBaiduMap: BaiduMap? = null
    private var mSensorManager: SensorManager? = null//方向传感器
    private var mDestinationPoint: LatLng? = null//目的地坐标点
    private var client: LocationClient? = null//定位监听
    private var mOption: LocationClientOption? = null//定位属性
    private var locData: MyLocationData? = null//定位坐标
    private var mInfoWindow: InfoWindow? = null//地图文字位置提醒
    private var mCurrentLat = 0.0
    private var mCurrentLon = 0.0
    private var mCurrentDirection = 0
    private var mDistance = 0.0
    private var mCenterPos: LatLng? = null
    private var mZoomScale = 0f //比例
    private var lastX: Double? = 0.0

    //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
    private val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val LOCATION_PERMISSION = 202
    private var mSelectDialog: SelectDialog? = null

    /*  override fun createLayout(): Int {
          return R.layout.activity_sign_record
      }*/

    override fun initViews() {
        super.initViews()
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions()
        }
    }

    private fun initViewAndData() {
        initBaiduMap()     //1、初始化地图
        getLocationClientOption()//2、定位开启
        mHandler.post(run)//设置系统时间
        arriver_bt?.setOnClickListener(this)
    }

    override fun createLayout(): Int {
        // 注册 SDK 广播监听者
        return R.layout.activity_map
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        SDKInitializer.initialize(getApplicationContext())
        RegisterBroadcast()
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        super.onCreate(savedInstanceState)
    }
    /**
     * 初始化地图
     */
    private fun initBaiduMap() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager//获取传感器管理服务
        mBaiduMap = mapview?.getMap()
        mBaiduMap?.setMapType(BaiduMap.MAP_TYPE_NORMAL)
        mBaiduMap?.setMyLocationEnabled(true)
        mBaiduMap?.setIndoorEnable(true)

    }

    override fun setStatusBar() {

    }



    fun initMapView() {
        /* mBaiduMap = mapView?.getMap()!!
         // 开启定位图层
         mBaiduMap.isMyLocationEnabled = true
         // 开启室内图
         mBaiduMap.setIndoorEnable(true)
         // 定位初始化
         mLocClient = LocationClient(this)
         mLocClient.registerLocationListener(myListener)
         val option = LocationClientOption()
         option.isOpenGps = true // 打开gps
         option.setCoorType("bd09ll") // 设置坐标类型
         option.setScanSpan(3000)
         mLocClient.locOption = option
         mLocClient.start()*/
    }

    //设置打卡目标范围圈
    private fun setCircleOptions() {
        if (mDestinationPoint == null) return
        val ooCircle = CircleOptions().fillColor(0x4057FFF8)
                .center(mDestinationPoint).stroke(Stroke(1, -0x49000001)).radius(DISTANCE)
        mBaiduMap?.addOverlay(ooCircle)
    }

    /***
     * 定位选项设置
     * @return
     */
    fun getLocationClientOption() {
        mOption = LocationClientOption()
        mOption?.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption?.setCoorType("bd09ll")//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption?.setScanSpan(2000)//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption?.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
        mOption?.setIsNeedLocationDescribe(true)//可选，设置是否需要地址描述
        mOption?.setNeedDeviceDirect(true)//可选，设置是否需要设备方向结果
        mOption?.setLocationNotify(true)//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption?.setIgnoreKillProcess(true)//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption?.setIsNeedLocationDescribe(false)//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption?.setIsNeedLocationPoiList(false)//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption?.SetIgnoreCacheException(false)//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption?.setOpenGps(true)//可选，默认false，设置是否开启Gps定位
        mOption?.setIsNeedAltitude(false)//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        client = LocationClient(this)
        client?.setLocOption(mOption)
        client?.registerLocationListener(BDAblistener)
        client?.start()
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private val BDAblistener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //定位方向
            mCurrentLat = location.latitude
            mCurrentLon = location.longitude
            //骑手定位
            locData = MyLocationData.Builder()
                    .direction(mCurrentDirection.toFloat()).latitude(location.latitude)
                    .longitude(location.longitude).build()
            mBaiduMap?.setMyLocationData(locData)
            mBaiduMap?.setMyLocationConfiguration(MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, null))
            //更改UI
            val message = Message()
            message.obj = location
            mHandler.sendMessage(message)
        }
    }

    /**
     * 处理连续定位的地图UI变化
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val location = msg.obj as BDLocation
            val LocationPoint = LatLng(location.latitude, location.longitude)
            //打卡范围
            mDestinationPoint = LatLng(location.latitude * 1.0001, location.longitude * 1.0001)//假设公司坐标

            setCircleOptions()
            //计算两点距离,单位：米
            mDistance = DistanceUtil.getDistance(mDestinationPoint, LocationPoint)
            if (mDistance <= DISTANCE) {
                //显示文字
                setTextOption(mDestinationPoint, "您已在打卡范围内", "#7ED321")
                //目的地图标
                setMarkerOptions(mDestinationPoint, R.drawable.arrive_icon)
                //按钮颜色
                arriver_bt?.setBackgroundDrawable(resources.getDrawable(R.drawable.restaurant_btbg_yellow))
                mBaiduMap?.setMyLocationEnabled(false)
            } else {
                setTextOption(LocationPoint, "您不在打卡范围之内", "#FF6C6C")
                setMarkerOptions(mDestinationPoint, R.drawable.restaurant_icon)
                arriver_bt?.setBackgroundDrawable(resources.getDrawable(R.drawable.restaurant_btbg_gray))
                mBaiduMap?.setMyLocationEnabled(true)
            }
            distance_tv?.setText("距离目的地：" + mDistance + "米")
            //缩放地图
            setMapZoomScale(LocationPoint)
        }
    }

    /**
     * 添加地图文字
     *
     * @param point
     * @param str
     * @param color 字体颜色
     */
    private fun setTextOption(point: LatLng?, str: String, color: String) {
        //使用MakerInfoWindow
        if (point == null) return
        val view = TextView(applicationContext)
        view.setBackgroundResource(R.drawable.map_textbg)
        view.setPadding(0, 23, 0, 0)
        view.typeface = Typeface.DEFAULT_BOLD
        view.textSize = 14f
        view.gravity = Gravity.CENTER
        view.text = str
        view.setTextColor(Color.parseColor(color))
        mInfoWindow = InfoWindow(view, point, 170)
        mBaiduMap?.showInfoWindow(mInfoWindow)
    }

    /**
     * 设置marker覆盖物
     *
     * @param ll   坐标
     * @param icon 图标
     */
    private fun setMarkerOptions(ll: LatLng?, icon: Int) {
        if (ll == null) return
        val bitmap = BitmapDescriptorFactory.fromResource(icon)
        val ooD = MarkerOptions().position(ll).icon(bitmap)
        mBaiduMap?.addOverlay(ooD)
    }

    //改变地图缩放
    private fun setMapZoomScale(ll: LatLng) {
        if (mDestinationPoint == null) {
            mZoomScale = getZoomScale(ll)
            mBaiduMap?.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, mZoomScale))//缩放
        } else {
            mZoomScale = getZoomScale(ll)
            mBaiduMap?.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mCenterPos, mZoomScale))//缩放
        }
    }

    /**
     * 获取地图的中心点和缩放比例
     *
     * @return float
     */
    private fun getZoomScale(LocationPoint: LatLng?): Float {
        var maxLong: Double    //最大经度
        var minLong: Double    //最小经度
        var maxLat: Double     //最大纬度
        var minLat: Double     //最小纬度
        val longItems = ArrayList<Double>()    //经度集合
        val latItems = ArrayList<Double>()     //纬度集合

        if (null != LocationPoint) {
            longItems.add(LocationPoint.longitude)
            latItems.add(LocationPoint.latitude)
        }
        if (null != mDestinationPoint) {
            longItems.add(mDestinationPoint!!.longitude)
            latItems.add(mDestinationPoint!!.latitude)
        }

        maxLong = longItems[0]    //最大经度
        minLong = longItems[0]    //最小经度
        maxLat = latItems[0]     //最大纬度
        minLat = latItems[0]     //最小纬度

        for (i in longItems.indices) {
            maxLong = Math.max(maxLong, longItems[i])   //获取集合中的最大经度
            minLong = Math.min(minLong, longItems[i])   //获取集合中的最小经度
        }

        for (i in latItems.indices) {
            maxLat = Math.max(maxLat, latItems[i])   //获取集合中的最大纬度
            minLat = Math.min(minLat, latItems[i])   //获取集合中的最小纬度
        }
        val latCenter = (maxLat + minLat) / 2
        val longCenter = (maxLong + minLong) / 2
        val jl = getDistance(LatLng(maxLat, maxLong), LatLng(minLat, minLong)).toInt()//缩放比例参数
        mCenterPos = LatLng(latCenter, longCenter)   //获取中心点经纬度
        val zoomLevel = intArrayOf(2500000, 2000000, 1000000, 500000, 200000, 100000, 50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0)
        var i: Int
        i = 0
        while (i < 18) {
            if (zoomLevel[i] < jl) {
                break
            }
            i++
        }
        val zoom = (i + 4).toFloat()
        return zoom
    }

    /**
     * 缩放比例参数
     *
     * @param var0
     * @param var1
     * @return
     */
    fun getDistance(var0: LatLng?, var1: LatLng?): Double {
        if (var0 != null && var1 != null) {
            val var2 = CoordUtil.ll2point(var0)
            val var3 = CoordUtil.ll2point(var1)
            return if (var2 != null && var3 != null) CoordUtil.getDistance(var2, var3) else -1.0
        } else {
            return -1.0
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val x = sensorEvent.values[SensorManager.DATA_X].toDouble()
        if (Math.abs(x - this!!.lastX!!) > 1.0) {
            mCurrentDirection = x.toInt()
            locData = MyLocationData.Builder()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection.toFloat()).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build()
            mBaiduMap?.setMyLocationData(locData)
        }
        lastX = x
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {

    }

    /**
     * 设置系统时间
     */
    private val run = object : Runnable {
        override fun run() {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss")// HH:mm:ss
            val date = Date(System.currentTimeMillis())//获取当前时间
            arriver_timetv?.setText(simpleDateFormat.format(date)) //更新时间
            mHandler.postDelayed(this, 1000)
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.arriver_bt) {
            if (mDistance <= DISTANCE) {
                showToast(this, "打卡成功")
            } else {
                showToast(this, "外勤打卡")
            }

        }
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    private var mReceiver: SDKReceiver? = null

    inner class SDKReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val s = intent.action
            var tx = ""

            if (s == SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR) {

                tx = ("key 验证出错! 错误码 :" + intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        + " ; 请在 AndroidManifest.xml 文件中检查 key 设置")
            } else if (s == SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK) {
                tx = "key 验证成功! 功能可以正常使用"
            } else if (s == SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR) {
                tx = "网络出错"
            }
            if (tx.contains("错")) {
                setHintShowDialog("提示", "确定", "关闭")
            } else {
                showToast(this@SignInActivity, tx)
            }

        }
    }

    /**
     * 提示框
     */
    protected fun setHintShowDialog(title: String, left: String, right: String) {

        if (mSelectDialog == null) {
            mSelectDialog = SelectDialog(this, true)
            mSelectDialog!!.setListener(object : SelectDialogListener {
                override fun leftClick() {
                    finish()
                }

                override fun rightClick() {}
            })
            mSelectDialog?.setCanceledOnTouchOutside(false)
            mSelectDialog?.setCancelable(false)
            mSelectDialog?.setTitle(title)
            mSelectDialog?.setLeftAndRightButtonText(left, right)
        }
        mSelectDialog?.showDialog()
    }

    fun RegisterBroadcast() {
        val iFilter = IntentFilter()
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)
        mReceiver = SDKReceiver()
        registerReceiver(mReceiver, iFilter)
    }

    private fun checkPermissions() {
        /*val checker = PermissionsChecker(this)
        if (checker.lacksPermissions(permission)) {
            PermissionsActivity().startActivityForResult(this, LOCATION_PERMISSION, permission)
        } else {
            initViewAndData()
        }*/

        val rxPermissions = RxPermissions(this)
        rxPermissions.requestEach(*permission)
                .subscribe(object : Observer<Permission> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        initViewAndData()
                    }
                })
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
         super.onActivityResult(requestCode, resultCode, data)
         Log.e(TAG, "onActivityResult:定位回调 ")
         if (requestCode == LOCATION_PERMISSION) {
             if (resultCode == PermissionsActivity().PERMISSIONS_GRANTED) {
                 initViewAndData()
             } else {
                 this.finish()
             }
         }
     }*/


    override fun onDestroy() {
        if (BDAblistener != null) {
            client?.unRegisterLocationListener(BDAblistener)
        }
        if (client != null && client!!.isStarted()) {
            client?.stop()
        }
        mapview?.onDestroy()
        mapview == null
        mHandler.removeCallbacks(run)
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun onResume() {
        super.onResume()
        mapview?.onResume()
        if (mSensorManager != null) {
            //为系统的方向传感器注册监听器
            mSensorManager?.registerListener(this, mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        mapview?.onPause()
    }

    override fun onStop() {
        super.onStop()
        //取消注册传感器监听
        if (mSensorManager != null) {
            mSensorManager?.unregisterListener(this)
        }

    }
}