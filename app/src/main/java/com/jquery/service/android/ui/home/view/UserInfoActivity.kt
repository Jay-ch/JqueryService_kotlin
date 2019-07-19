package com.jquery.service.android.ui.home.view

import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.utils.CommonsStatusBarUtil
import kotlinx.android.synthetic.main.activity_userinfo_layout.*
import kotlinx.android.synthetic.main.include_title_bar.*

/**
 * 修改个人资料
 * @author J.query
 * @date 2019/3/13
 * @email j-query@foxmail.com
 */
class UserInfoActivity : BaseActivity() {



    override fun createLayout(): Int {

        return R.layout.activity_userinfo_layout
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }



    override fun initViews() {
        super.initViews()
        setStatusBar()
        top_title.setTvBackDrawableLeft(this, R.drawable.back_arrow)
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        userinfo_avatar.setOnClickListener {
            startActivity(SelectPictureActivity::class.java)
        }
    }
}