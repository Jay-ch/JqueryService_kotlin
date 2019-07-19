package com.jquery.service.android.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import android.view.View
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_test_scan.*
import java.util.*

/**
 * 二维码扫描
 * @author J.query
 * @date 2019/3/15
 * @email j-query@foxmail.com
 */
class ScanLifeActivity : BaseActivity(), QRCodeView.Delegate {
    private var TAG = ScanLifeActivity::class.java.simpleName
    private var REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666
    private var permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    private var LIVING_PERMISSION = 201


    override fun createLayout(): Int {
        return R.layout.activity_test_scan
    }

    override fun setStatusBar() {

    }

    override fun initViews() {
        super.initViews()
        zxingview.setDelegate(this)
        checkPermissions()
    }

    private fun checkPermissions() {
        /*  val checker = PermissionsChecker(this)
          if (checker.lacksPermissions(permission)) {
              PermissionsActivity().startActivityForResult(this, LIVING_PERMISSION, permission)
          } else {
              initViewAndData()
          }*/

        /*val rxPermissions = RxPermissions(this)
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
                })*/

        PermissionUtils.camera(this, {
            initViewAndData()
        })
    }

    private fun initViewAndData() {
        zxingview.setDelegate(this)
    }


    fun onClick(v: View) {
        when (v.id) {
            R.id.start_preview ->
                // 打开后置摄像头开始预览，但是并未开始识别
                zxingview.startCamera()
            R.id.stop_preview ->
                // 关闭摄像头预览，并且隐藏扫描框
                zxingview.stopCamera()
            R.id.start_spot ->
                // 延迟0.5秒后开始识别
                zxingview.startSpot()
            R.id.stop_spot ->
                // 停止识别
                zxingview.stopSpot()
            R.id.start_spot_showrect ->
                // 显示扫描框，并且延迟0.5秒后开始识别
                zxingview.startSpotAndShowRect()
            R.id.stop_spot_hiddenrect ->
                // 停止识别，并且隐藏扫描框
                zxingview.stopSpotAndHiddenRect()
            R.id.show_scan_rect ->
                // 显示扫描框
                zxingview.showScanRect()
            R.id.hidden_scan_rect ->
                // 隐藏扫描框
                zxingview.hiddenScanRect()
            R.id.decode_scan_box_area ->
                // 仅识别扫描框中的码
                zxingview.getScanBoxView().setOnlyDecodeScanBoxArea(true)
            R.id.decode_full_screen_area ->
                // 识别整个屏幕中的码
                zxingview.getScanBoxView().setOnlyDecodeScanBoxArea(false)
            R.id.open_flashlight -> zxingview.openFlashlight() // 打开闪光灯
            R.id.close_flashlight -> zxingview.closeFlashlight() // 关闭闪光灯
            R.id.scan_one_dimension -> {
                zxingview.changeToScanBarcodeStyle() // 切换成扫描条码样式
                zxingview.setType(BarcodeType.ONE_DIMENSION, null) // 只识别一维条码
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_two_dimension -> {
                zxingview.changeToScanQRCodeStyle() // 切换成扫描二维码样式
                zxingview.setType(BarcodeType.TWO_DIMENSION, null) // 只识别二维条码
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_qr_code -> {
                zxingview.changeToScanQRCodeStyle() // 切换成扫描二维码样式
                zxingview.setType(BarcodeType.ONLY_QR_CODE, null) // 只识别 QR_CODE
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_code128 -> {
                zxingview.changeToScanBarcodeStyle() // 切换成扫描条码样式
                zxingview.setType(BarcodeType.ONLY_CODE_128, null) // 只识别 CODE_128
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_ean13 -> {
                zxingview.changeToScanBarcodeStyle() // 切换成扫描条码样式
                zxingview.setType(BarcodeType.ONLY_EAN_13, null) // 只识别 EAN_13
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_high_frequency -> {
                zxingview.changeToScanQRCodeStyle() // 切换成扫描二维码样式
                zxingview.setType(BarcodeType.HIGH_FREQUENCY, null) // 只识别高频率格式，包括 QR_CODE、EAN_13、CODE_128
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_all -> {
                zxingview.changeToScanQRCodeStyle() // 切换成扫描二维码样式
                zxingview.setType(BarcodeType.ALL, null) // 识别所有类型的码
                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.scan_custom -> {
                zxingview.changeToScanQRCodeStyle() // 切换成扫描二维码样式
                val hintMap = EnumMap<DecodeHintType, Any>(DecodeHintType::class.java)
                val formatList = ArrayList<BarcodeFormat>()
                formatList.add(BarcodeFormat.QR_CODE)
                formatList.add(BarcodeFormat.EAN_13)
                formatList.add(BarcodeFormat.CODE_128)
                hintMap[DecodeHintType.POSSIBLE_FORMATS] = formatList // 可能的编码格式
                hintMap[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
                hintMap[DecodeHintType.CHARACTER_SET] = "utf-8" // 编码字符集
                zxingview.setType(BarcodeType.CUSTOM, hintMap) // 自定义识别的类型

                zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
            }
            R.id.choose_qrcde_from_gallery -> {
                /*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 */
                val photoPickerIntent = BGAPhotoPickerActivity.IntentBuilder(this)
                        .cameraFileDir(null)
                        .maxChooseCount(1)
                        .selectedPhotos(null)
                        .pauseOnScroll(false)
                        .build()
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        zxingview.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        //mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        zxingview.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
    }

    override fun onStop() {
        zxingview.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        zxingview.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    override fun onScanQRCodeSuccess(result: String?) {
        Log.i(TAG, "result:$result")
        setTitle("扫描结果为：$result")
        vibrate()
        zxingview.startSpot() // 延迟0.5秒后开始识别
    }

    override fun onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        zxingview.startSpotAndShowRect()
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            val picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data)[0]
            zxingview.decodeQRCode(picturePath)
        }
    }

    //权限申请结果
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}