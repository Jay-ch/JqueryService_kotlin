package com.jquery.service.android.ui.home.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R
import com.jquery.service.android.ui.admin.home.view.FaultConditionActivity
import com.jquery.service.android.utils.FastBlurUtils
import kotlinx.android.synthetic.main.fragment_mine_layout.*


/**
 * 我的
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class MineHomeFragment : BaseFragment() {

    private var mTitle: String? = null
    private var resource: Bitmap? = null
    private var url: String? = "http://img5.imgtn.bdimg.com/it/u=3300305952,1328708913&fm=26&gp=0.jpg"

    companion object {
        fun getInstance(title: String): MineHomeFragment {
            val fragment = MineHomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun createLayout(): Int {
        return R.layout.fragment_mine_layout
    }

    override fun initViews() {
        super.initViews()
        setOnClick()
    }

    override fun initData() {
        super.initData()
        alphaHeader()
    }

    @SuppressLint("NewApi")
    private fun alphaHeader() {
        //  image_avatar.setImageURI(url)
        val res = resources
        val originBitmap = BitmapFactory.decodeResource(res, R.drawable.panda)
        val scaleRatio = 10
        val blurRadius = 2
        val scaledBitmap = Bitmap.createScaledBitmap(originBitmap,
                originBitmap.getWidth() / scaleRatio,
                originBitmap.getHeight() / scaleRatio,
                false)
        val blurBitmap = FastBlurUtils().doBlur(scaledBitmap, blurRadius, true)
        fl_mine.setBackground(BitmapDrawable(resources, blurBitmap))
    }

    private fun alphaTask(context: Activity) {
        context.getWindow().requestFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = context.getWindow()
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(Color.TRANSPARENT)
            window.setNavigationBarColor(Color.TRANSPARENT)
        }
    }

    private fun setOnClick() {
        tv_sign_record.setOnClickListener {
            startActivity(SignRecordActivity::class.java)
        }
        tv_address_book.setOnClickListener {
            startActivity(AddressBookActivity::class.java)
        }
        tv_liastre.setOnClickListener {
            startActivity(LiastreActivity::class.java)
        }
        tv_fault_condition.setOnClickListener {
            startActivity(FaultConditionActivity::class.java)
        }
        tv_about.setOnClickListener {
            startActivity(AboutActivity::class.java)
        }
        fl_mine.setOnClickListener {
            startActivity(UserInfoActivity::class.java)
        }
        tv_sign_case.setOnClickListener {
            startActivity(SignInCaseActivity::class.java)
        }
        tv_personnel_situation.setOnClickListener {
            startActivity(StaffProfileActivity::class.java)
        }

    }
}