package com.jquery.service.android.ui.home.view

import android.Manifest
import android.content.Intent
import android.os.Environment
import android.support.annotation.IdRes
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import com.luck.picture.lib.tools.PictureFileUtils
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.widgets.FullyGridLayoutManager
import com.jquery.service.android.widgets.GridImageAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_picture_selector_layout.*
import kotlinx.android.synthetic.main.layout_number.*
import java.io.File

class SelectPictureActivity : BaseActivity(),
        RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private val TAG = SelectPictureActivity::class.java.simpleName
    private var aspect_ratio_x: Int = 0
    private var aspect_ratio_y: Int = 0

    private var selectList: MutableList<LocalMedia> = mutableListOf()
    private var adapter: GridImageAdapter? = null
    private var maxSelectNum = 9
    private var themeId: Int = 0
    private var chooseMode = PictureMimeType.ofAll()


    override fun createLayout(): Int {
        return R.layout.activity_picture_selector_layout
    }

    override fun setStatusBar() {

    }



    override fun initViews() {
        super.initViews()
        themeId = R.style.picture_default_style
        rgb_crop.setOnCheckedChangeListener(this)
        rgb_style.setOnCheckedChangeListener(this)
        rgb_photo_mode.setOnCheckedChangeListener(this)
        left_back.setOnClickListener{
            finish()
        }
        minus.setOnClickListener{
            if (maxSelectNum > 1) {
                maxSelectNum--
            }
            tv_select_num.setText("" + maxSelectNum + "")
            adapter?.setSelectMax(maxSelectNum)
        }
        plus.setOnClickListener{
            maxSelectNum++
            tv_select_num.setText("" + maxSelectNum + "")
            adapter?.setSelectMax(maxSelectNum)
        }
        cb_crop.setOnCheckedChangeListener(this)
        cb_crop_circular.setOnCheckedChangeListener(this)
        cb_compress.setOnCheckedChangeListener(this)
        val manager = FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        recycler.setLayoutManager(manager)
        adapter = GridImageAdapter(this, onAddPicClickListener)
        adapter?.setList(selectList)
        adapter?.setSelectMax(maxSelectNum)
        recycler.setAdapter(adapter)
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
                            PictureSelector.create(this@SelectPictureActivity).themeStyle(themeId).openExternalPreview(position, selectList)
                        2 ->
                            // 预览视频
                            PictureSelector.create(this@SelectPictureActivity).externalPictureVideo(media.getPath())
                        3 ->
                            // 预览音频
                            PictureSelector.create(this@SelectPictureActivity).externalPictureAudio(media.getPath())
                    }
                }
            }
        })

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        val permissions = RxPermissions(this)
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(object : Observer<Boolean> {
            override fun onNext(aBoolean: Boolean) {
                if (aBoolean!!) {
                    PictureFileUtils.deleteCacheDirFile(this@SelectPictureActivity)
                } else {
                    Toast.makeText(this@SelectPictureActivity,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onSubscribe(d: Disposable) {}

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
    }

    private val onAddPicClickListener = object : GridImageAdapter.onAddPicClickListener {
        override fun onAddPicClick() {
            val mode = cb_mode.isChecked()
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(this@SelectPictureActivity)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(if (cb_choose_mode.isChecked())
                            PictureConfig.MULTIPLE
                        else
                            PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
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
                PictureSelector.create(this@SelectPictureActivity)
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

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private fun getPath(): String {
        val path = Environment.getExternalStorageDirectory().toString() + "/Luban/image/"
        val file = File(path)
        return if (file.mkdirs()) {
            path
        } else path
    }
}
