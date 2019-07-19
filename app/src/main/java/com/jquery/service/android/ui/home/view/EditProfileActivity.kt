package com.jquery.service.android.ui.home.view

import android.support.v4.content.ContextCompat
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.utils.CommonsStatusBarUtil
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.include_title_bar.*

/**
 * 个人资料修改
 * @author J.query
 * @date 2019/3/12
 * @email j-query@foxmail.com
 */
class EditProfileActivity:BaseActivity() {

    override fun createLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }

}