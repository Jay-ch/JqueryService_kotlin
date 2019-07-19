package com.jquery.service.android.ui.admin.home.view

import android.os.Bundle
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R

/**
 * 故障
 * @author J.query
 * @date 2019/2/21
 * @email j-query@foxmail.com
 */
class AdminFaultFragment : BaseFragment() {

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): AdminFaultFragment {
            val fragment = AdminFaultFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun createLayout(): Int {
        return R.layout.fragment_admin_fault_layout
    }

}