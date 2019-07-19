package com.jquery.service.android.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class SelectFragmentAdapter: FragmentPagerAdapter {

    private val context: Context? = null
    private var list = ArrayList<Fragment>()

    constructor(fm: FragmentManager, list: ArrayList<Fragment>): super(fm) {
        this.list = list
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return if (list != null) list.size else 0
    }
}