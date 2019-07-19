package com.jquery.service.android.ui.admin.home.view

import android.os.Bundle
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class AdminMineHomeFragment : BaseFragment() {
    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): AdminMineHomeFragment {
            val fragment = AdminMineHomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun createLayout(): Int {
        return R.layout.fragment_admin_home_layout
    }
}