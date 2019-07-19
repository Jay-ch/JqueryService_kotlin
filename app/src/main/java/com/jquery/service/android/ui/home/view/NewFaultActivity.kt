package com.jquery.service.android.ui.home.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.jquery.service.android.Base.BaseMvpActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.PhotoUriAdapter
import com.jquery.service.android.app.App
import com.jquery.service.android.entity.*
import com.jquery.service.android.listener.CloseDialogListener
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.permission.PermissionUtils
import com.jquery.service.android.ui.ScanLifeActivity
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.utils.LocationHelper
import com.jquery.service.android.utils.StatusBarUtils
import com.jquery.service.android.widgets.BaseStripAdapter
import com.jquery.service.android.widgets.FullyGridLayoutManager
import com.jquery.service.android.widgets.GridImageAdapter
import com.jquery.service.android.widgets.dialog.ListCommDialog
import com.jquery.service.android.widgets.dialog.NoButtonsDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_new_fault_layout.*
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.android.synthetic.main.include_title_bar.view_status_bar
import kotlinx.android.synthetic.main.layout_number.*
import java.util.*


/**
 * 新建故障
 * @author J.query
 * @date 2019/4/4
 * @email j-query@foxmail.com
 */
class NewFaultActivity : BaseMvpActivity<HomePresenter>(), HomeContract.HomeView,
        RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }


    // 定位相关
    internal lateinit var mLocClient: LocationClient
    //var myListener = MyLocationListenner()
    private var mCurrentMode: MyLocationConfiguration.LocationMode? = null
    internal var mCurrentMarker: BitmapDescriptor? = null
    internal lateinit var mBaiduMap: BaiduMap
    internal lateinit var mFloorListAdapter: BaseStripAdapter
    internal var mMapBaseIndoorMapInfo: MapBaseIndoorMapInfo? = null
    internal lateinit var mContext: Context
    // UI相关
    internal var isFirstLoc = true // 是否首次定位

    private lateinit var mContent: Context
    private var locateHelper: LocationHelper? = null
    private var mLocationListener: LocationHelper.LocationListener? = null
    private val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION)

    private var locationDescribe: String? = null

    private var baiduMap: BaiduMap? = null
    var myListener = MyLocationListenner()
    private var mInfoWindow: InfoWindow? = null//地图文字位置提醒
    var mAdapter: PhotoUriAdapter? = null
    private val REQUEST_CODE_CHOOSE = 23
    private var selectList: MutableList<LocalMedia> = mutableListOf()
    var adapter: GridImageAdapter? = null
    private var mUris: MutableList<Uri>? = null
    private var mPaths: MutableList<String>? = null
    private var data: List<UriViewEntity>? = null
    private var mLocationEntity: LocationHelper.LocationEntity? = null


    private val TAG = SelectPictureActivity::class.java.simpleName
    private var aspect_ratio_x: Int = 0
    private var aspect_ratio_y: Int = 0

    private var maxSelectNum = 5
    private var themeId: Int = 0
    private var chooseMode = PictureMimeType.ofAll()


    override fun onCreate(savedInstanceState: Bundle?) {
        SDKInitializer.initialize(getApplicationContext())
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        super.onCreate(savedInstanceState)
    }

    override fun createLayout(): Int {
        return R.layout.activity_new_fault_layout
    }

    override fun initViews() {
        setStatusWhiteBar()
        mContent = App.mContext

        //showLoading("加载中....")
        if (Build.VERSION.SDK_INT >= 23) {
            initBaiduMap()
        } else {
            initBaiduMap()
        }


        /*  val manager = FullyGridLayoutManager(this@NewFaultActivity, 4, GridLayoutManager.VERTICAL, false)
          recyclerView_new_fault.setLayoutManager(manager)
          adapter = GridImageAdapter(this@NewFaultActivity)
          adapter?.setList(selectList)
          adapter?.setSelectMax(9)
          recyclerView_new_fault.setAdapter(adapter)*/

        mAdapter = PhotoUriAdapter(this)
        //mAdapter?.addAll(data!!)
        recyclerView_new_fault.layoutManager = LinearLayoutManager(this)
        recyclerView_new_fault.setAdapter(mAdapter)

        themeId = R.style.picture_default_style
        minus.setOnClickListener {
            if (maxSelectNum > 1) {
                maxSelectNum--
            }
            tv_select_num.setText("" + maxSelectNum + "")
            adapter?.setSelectMax(maxSelectNum)
        }
        plus.setOnClickListener {
            maxSelectNum++
            tv_select_num.setText("" + maxSelectNum + "")
            adapter?.setSelectMax(maxSelectNum)
        }

        val manager = FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        recyclerView_new_fault.setLayoutManager(manager)
        adapter = GridImageAdapter(this, onAddPicClickListener)
        adapter?.setList(selectList)
        adapter?.setSelectMax(maxSelectNum)
        recyclerView_new_fault.setAdapter(adapter)
        adapter?.setOnItemClickListener(object : GridImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, v: View) {
                if (selectList.size > 0) {
                    val media = selectList.get(position)
                    val pictureType = media.getPictureType()
                    val mediaType = PictureMimeType.pictureToVideo(pictureType)
                    when (mediaType) {
                        1 ->
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(SelectPictureActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(this@NewFaultActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                        2 ->
                            // 预览视频
                            PictureSelector.create(this@NewFaultActivity).externalPictureVideo(media.getPath())
                        3 ->
                            // 预览音频
                            PictureSelector.create(this@NewFaultActivity).externalPictureAudio(media.getPath())
                    }
                }
            }
        })

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        val permissions = RxPermissions(this)
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(object : Observer<Boolean> {
            override fun onNext(aBoolean: Boolean) {
                if (aBoolean!!) {
                    PictureFileUtils.deleteCacheDirFile(this@NewFaultActivity)
                } else {
                    Toast.makeText(this@NewFaultActivity,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })

        ed_editor_detail.addTextChangedListener(object : TextWatcher {

            private var temp: CharSequence? = null
            private var selectionStart: Int = 0
            private var selectionEnd: Int = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                temp = s
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val detailLength = editable.length
                id_editor_detail_font_count.setText(detailLength.toString() + "/500")

                if (temp!!.length > 500) {
                    editable.delete(selectionStart - 1, selectionEnd)
                    val tempSelection = selectionStart
                    showToast("只能输入500个字")
                }
            }
        })
        sc_parent_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                sc_editor_scroll.parent.requestDisallowInterceptTouchEvent(false)
                return false
            }
        })
        sc_editor_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                p0?.parent?.requestDisallowInterceptTouchEvent(true)
                return false
            }
        })

        img_right.setOnClickListener {
            /*   val intent = Intent(this@FaultActivity, SearchActivity::class.java)
               intent.putExtra("id", 1)
               startActivity(intent)*/
            var bundle = Bundle()
            bundle.putString("time_type", "in_fault_year")
            startActivity(FaultActivity::class.java, bundle)
        }

        tv_back.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("id", "1")
            startActivity(FaultActivity::class.java, bundle)
            finish()

        }
    }


    private fun setStatusWhiteBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }

    private val onAddPicClickListener = object : GridImageAdapter.onAddPicClickListener {
        override fun onAddPicClick() {
            val mode = cb_mode.isChecked()
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(this@NewFaultActivity)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(if (cb_choose_mode.isChecked())
                            PictureConfig.MULTIPLE
                        else
                            PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .compress(cb_compress.isChecked())// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(if (cb_hide.isChecked()) false else true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                        //                        .videoMaxSecond(15)
                        //                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
            } else {
                // 单独拍照
                PictureSelector.create(this@NewFaultActivity)
                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .selectionMode(if (cb_choose_mode.isChecked())
                            PictureConfig.MULTIPLE
                        else
                            PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .compress(cb_compress.isChecked())// 是否压缩
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(if (cb_hide.isChecked()) false else true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
                        .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)!!
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (media in selectList) {
                        Log.i("图片-----》", media.getPath())
                    }
                    adapter?.setList(selectList)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup, @IdRes checkedId: Int) {
        when (checkedId) {
            R.id.rb_all -> {
                chooseMode = PictureMimeType.ofAll()
                cb_preview_img.setChecked(true)
                cb_preview_video.setChecked(true)
                cb_isGif.setChecked(false)
                cb_preview_video.setChecked(true)
                cb_preview_img.setChecked(true)
                cb_preview_video.setVisibility(View.VISIBLE)
                cb_preview_img.setVisibility(View.VISIBLE)
                cb_compress.setVisibility(View.VISIBLE)
                cb_crop.setVisibility(View.VISIBLE)
                cb_isGif.setVisibility(View.VISIBLE)
                cb_preview_audio.setVisibility(View.GONE)
            }
            R.id.rb_image -> {
                chooseMode = PictureMimeType.ofImage()
                cb_preview_img.setChecked(true)
                cb_preview_video.setChecked(false)
                cb_isGif.setChecked(false)
                cb_preview_video.setChecked(false)
                cb_preview_video.setVisibility(View.GONE)
                cb_preview_img.setChecked(true)
                cb_preview_audio.setVisibility(View.GONE)
                cb_preview_img.setVisibility(View.VISIBLE)
                cb_compress.setVisibility(View.VISIBLE)
                cb_crop.setVisibility(View.VISIBLE)
                cb_isGif.setVisibility(View.VISIBLE)
            }
            R.id.rb_video -> {
                chooseMode = PictureMimeType.ofVideo()
                cb_preview_img.setChecked(false)
                cb_preview_video.setChecked(true)
                cb_isGif.setChecked(false)
                cb_isGif.setVisibility(View.GONE)
                cb_preview_video.setChecked(true)
                cb_preview_video.setVisibility(View.VISIBLE)
                cb_preview_img.setVisibility(View.GONE)
                cb_preview_img.setChecked(false)
                cb_compress.setVisibility(View.GONE)
                cb_preview_audio.setVisibility(View.GONE)
                cb_crop.setVisibility(View.GONE)
            }
            R.id.rb_audio -> {
                chooseMode = PictureMimeType.ofAudio()
                cb_preview_audio.setVisibility(View.VISIBLE)
            }
            R.id.rb_crop_default -> {
                aspect_ratio_x = 0
                aspect_ratio_y = 0
            }
            R.id.rb_crop_1to1 -> {
                aspect_ratio_x = 1
                aspect_ratio_y = 1
            }
            R.id.rb_crop_3to4 -> {
                aspect_ratio_x = 3
                aspect_ratio_y = 4
            }
            R.id.rb_crop_3to2 -> {
                aspect_ratio_x = 3
                aspect_ratio_y = 2
            }
            R.id.rb_crop_16to9 -> {
                aspect_ratio_x = 16
                aspect_ratio_y = 9
            }
            R.id.rb_default_style -> themeId = R.style.picture_default_style
            R.id.rb_white_style -> themeId = R.style.picture_white_style
            R.id.rb_num_style -> themeId = R.style.picture_QQ_style
            R.id.rb_sina_style -> themeId = R.style.picture_Sina_style
        }
    }

    private var x = 0
    private var y = 0

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.cb_crop -> {
                rgb_crop.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
                cb_hide.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
                cb_crop_circular.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
                cb_styleCrop.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
                cb_showCropFrame.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
                cb_showCropGrid.setVisibility(if (isChecked) View.VISIBLE else View.GONE)
            }
            R.id.cb_crop_circular -> {
                if (isChecked) {
                    x = aspect_ratio_x
                    y = aspect_ratio_y
                    aspect_ratio_x = 1
                    aspect_ratio_y = 1
                } else {
                    aspect_ratio_x = x
                    aspect_ratio_y = y
                }
                rgb_crop.setVisibility(if (isChecked) View.GONE else View.VISIBLE)
                if (isChecked) {
                    cb_showCropFrame.setChecked(false)
                    cb_showCropGrid.setChecked(false)
                } else {
                    cb_showCropFrame.setChecked(true)
                    cb_showCropGrid.setChecked(true)
                }
            }
        }
    }


    override fun initData() {
        super.initData()
        tv_relocation.setOnClickListener {
            initBaiduMap()
        }
        tv_scan.setOnClickListener {
            startActivity(ScanLifeActivity::class.java)
        }
        ll_table_model.setOnClickListener {
            showTableTypeDialog()
        }
        tv_fult_type.setOnClickListener {
            showFultTypeDialog()
        }
        tv_add.setOnClickListener {
            //initPhotoAlbum()
            //initPhoto()
        }
        tv_group.setOnClickListener {
            showFultTypeDialog()
        }
        tv_fault_scheme.setOnClickListener {
            showFultSchemeDialog()
        }
    }


    override fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            StatusBarUtils.setStatusBarColor(this, R.color.c_ff)
        }
    }

    private fun initClick() {

    }

    private fun initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    /**
     * Request permission
     *
     * @param permissions
     */
    /**
     * Request permission
     *
     * @param permissions
     */
    fun requestPermissions(vararg permissions: String) {
        if (Build.VERSION.SDK_INT >= 23) {
            val list = ArrayList<String>()
            for (i in permissions.indices) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permissions[i])
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        Toast.makeText(this, "没有开启权限将会导致部分功能不可使用", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ActivityCompat.requestPermissions(this, list.toTypedArray(), 0)
        }
    }

    private fun initBaiduMap() {
        locateHelper = LocationHelper.Builder(App.mContext)
                .setScanSpan(0)
                .setIsNeedLocationDescribe(true).build()
        mLocationListener = object : LocationHelper.LocationListener() {
            override fun onReceiveLocation(location: LocationHelper.LocationEntity) {
                if ("成功" in location.toString()) {
                    //hideBaseLoading()
                    mLocationEntity = location
                    if (location != null && "在" in location.locationDescribe) {
                        locationDescribe = location.locationDescribe.substring(1)
                        tv_home_address.setText(location.city + location.district + location.street + locationDescribe)
                        tv_relocation.visibility = View.VISIBLE
                        locateHelper?.stop()
                    }
                } else {
                    hideBaseLoading()
                    tv_home_address.setText("定位失败，重新再试")
                    tv_relocation.visibility = View.VISIBLE
                }
                println(" --------------------------location $location")
            }

            override fun onError(e: Throwable) {
                println(" --------------------------throwable $e")
                log(" -------------onError-------------throwable" + e.message.toString())
                tv_home_address.setText(e.message)
                hideBaseLoading()
            }
        }
        locateHelper?.registerLocationListener(mLocationListener as LocationHelper.LocationListener)
        PermissionUtils.location(this, {
            if (locateHelper?.start() == true) {
                initMapView(mLocationEntity)
            } else {
                hideBaseLoading()
                tv_home_address.setText("定位失败，请检查是否开启了定位？")
            }
        })
    }

    private fun initMapView(mLocationEntity: LocationHelper.LocationEntity?) {
        mBaiduMap = mapView?.getMap()!!
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
        mLocClient.start()
        //隐藏放大缩小图标
        //mapView.showZoomControls(false)
        // customMarker(mLocationEntity?.latitude, mLocationEntity?.longitude)
        // mBaiduMap = mapView?.getMap()!!
        // locateHelper?.start()
        locateHelper?.customMarker(this, mBaiduMap, mLocationEntity?.latitude, mLocationEntity?.longitude)
    }

    /**
     * 自定义百度地图的覆盖物
     */


    private fun customMarker(latitude: Double?, longitude: Double?) {
        //目的地图标
        setMarkerOptions(latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }, R.drawable.arrive_icon)
        setTextOption(latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }, "维修地址", "#7ED321")
    }

    /**
     * 设置marker覆盖物
     *
     * @param ll   坐标
     * @param icon 图标
     */
    private fun setMarkerOptions(ll: LatLng?, icon: Int) {
        mBaiduMap.removeMarkerClickListener(onMarkerClickListener)
        mBaiduMap.clear()
        if (ll == null) return
        val bitmap = BitmapDescriptorFactory.fromResource(icon)
        val ooD = MarkerOptions().position(ll).icon(bitmap)
        mBaiduMap?.addOverlay(ooD)
    }

    /**
     * 移除Marker的监听
     */
    internal var onMarkerClickListener: BaiduMap.OnMarkerClickListener = BaiduMap.OnMarkerClickListener {
        //在这里我做了跳转页面

        false
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
        mInfoWindow = InfoWindow(view, point, 100)
        mBaiduMap?.showInfoWindow(mInfoWindow)
    }

    /**
     * 故障类型
     */
    private fun showFultTypeDialog() {
        var map1 = ListCommEntity("张三", 1, "张三")
        var map2 = ListCommEntity("李四", 2, "李四")
        var map3 = ListCommEntity("王五", 3, "王五")
        var list = mutableListOf<ListCommEntity>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        var mListCommDialog = ListCommDialog(this, true)
        if (mListCommDialog !== null) {
            mListCommDialog.setListener(object : ListSelectDialogListener {
                override fun selectType(s: String) {

                }

                override fun onItemClick(position: Int) {
                    var get = list.get(position).token
                    log("===============获取到的值=================" + get)
                }

            })
            mListCommDialog.setCanceledOnTouchOutside(false)
            mListCommDialog.setCancelable(false)
            mListCommDialog.showDialog()
            mListCommDialog.setHint("故障类型")
            mListCommDialog.setLeftAndRightText("好的", "取消")
            mListCommDialog.addData(list)
            //permissionDialog.setLeftText("知道了")
        }
    }

    /**
     * 表具型号
     */
    private fun showTableTypeDialog() {
        var map1 = ListCommEntity("张三", 1, "张三")
        var map2 = ListCommEntity("李四", 2, "李四")
        var map3 = ListCommEntity("王五", 3, "王五")
        var list = mutableListOf<ListCommEntity>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        var mListCommDialog = ListCommDialog(this, true)
        if (mListCommDialog !== null) {
            mListCommDialog.setListener(object : ListSelectDialogListener {
                override fun selectType(s: String) {

                }

                override fun onItemClick(position: Int) {
                    var get = list.get(position).token
                    log("===============获取到的值=================" + get)
                }

            })
            mListCommDialog.setCanceledOnTouchOutside(false)
            mListCommDialog.setCancelable(false)
            mListCommDialog.showDialog()
            mListCommDialog.setHint("表具型号")
            mListCommDialog.setLeftAndRightText("好的", "取消")
            mListCommDialog.addData(list)
            //permissionDialog.setLeftText("知道了")
        }
    }

    /**
     * 故障类型
     */
    private fun showFultSchemeDialog() {
        var mNoButtonsDialog = NoButtonsDialog(this, true)
        if (mNoButtonsDialog !== null) {
            mNoButtonsDialog.setListener(object : CloseDialogListener {
                override fun closeClick() {

                }
            })

            mNoButtonsDialog.setCanceledOnTouchOutside(false)
            mNoButtonsDialog.setCancelable(false)
            mNoButtonsDialog.showDialog()
            mNoButtonsDialog.setCloseTitle("常见解决方案")
            mNoButtonsDialog.setTitle("开关阀故障类型")
            mNoButtonsDialog.setDetails("故障类型你的那个呢西南方式能够毛线，您公开你选哪个性能看过了市房管局两年房价傻妞归来知道你管理局在那几张反正你据了解给你了几年级了法规尽快的租赁费更能保证那估计那种今年年底房价控制了的房价的可能降到了你了的看法那地方在打飞机两口子经典款了房间内")
        }
    }

    /**
     * 定位SDK监听函数
     */
    inner class MyLocationListenner : BDAbstractLocationListener() {

        private var lastFloor: String? = null

        override fun onReceiveLocation(location: BDLocation?) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return
            }
            val bid = location.buildingID
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.e("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo?.getID())
                if (bid == mMapBaseIndoorMapInfo?.getID()) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    val floor = location.floor.toUpperCase()// 楼层
                    Log.e("indoor", "floor = " + floor + " position = " + mFloorListAdapter.getPosition(floor))
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
                        mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo?.getID())
                        mFloorListAdapter.setSelectedPostion(mFloorListAdapter.getPosition(floor))
                        mFloorListAdapter.notifyDataSetInvalidated()
                    }

                    if (!location.isIndoorLocMode) {
                        mLocClient.startIndoorMode()// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.e("indoor", "start indoormod")
                    }
                }
            }

            val locData = MyLocationData.Builder().accuracy(location.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100f).latitude(location.latitude).longitude(location.longitude).build()
            mBaiduMap.setMyLocationData(locData)
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(location.latitude, location.longitude)
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(18.0f)
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
        }

        fun onReceivePoi(poiLocation: BDLocation) {

        }

        override fun onConnectHotSpotMessage(s: String?, i: Int) {

        }
    }

    override fun onStop() {
        super.onStop()
        locateHelper?.unRegisterLocationListener(this!!.mLocationListener!!)
        locateHelper?.stop()
    }

    override fun onPause() {
        mapView.setVisibility(View.INVISIBLE);
        mapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mapView.setVisibility(View.VISIBLE);
        mapView?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        // 退出时销毁定位
        locateHelper?.stop()
        mapView.onDestroy()
        mapView == null
        super.onDestroy()
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
             mAdapter?.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data))
             Log.e("OnActivityResult ", Matisse.obtainOriginalState(data).toString())
         }

     }*/

    override fun loginSuccess(data: UserDetailEntity?, token: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //token.log
    }

    override fun loginFail(e: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wxLoginSuccess(data: UserInfoResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun WeatherTestSuccess(data: WeatherEntity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokenSuccess(token: TokenEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRegisterTokenSuccess(token: TokenEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokenFail(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserInfoSuccess(result: UserInfoResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //权限申请结果
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*val intent = Intent(this@NewFaultActivity, MainActivity::class.java)
            intent.putExtra("id", 1)
            startActivity(intent)
            finish()*/
            var bundle = Bundle()
            bundle.putSerializable("id", 1)
            startActivity(FaultActivity::class.java, bundle)
            finish()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}