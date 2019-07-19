package com.jquery.service.android.ui.home.view

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.SelectFragmentAdapter
import kotlinx.android.synthetic.main.activity_viewpager_layout.*
import java.util.*

/**
 * 故障维修
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class FaultFragment : BaseFragment() {
    var index: Int = 0
    private var mFragments: ArrayList<Fragment>? = null
    private var mSelectAdapter: SelectFragmentAdapter? = null
    private var mHandledFaultFragment: HandledFaultFragment? = null
    private var mUntreatedFaultFragment: UntreatedFaultFragment? = null

    override fun createLayout(): Int {
        return R.layout.activity_viewpager_layout
    }

    override fun initViews() {
        super.initViews()
        initClick()
    }


    override fun initData() {
        super.initData()
        mHandledFaultFragment = HandledFaultFragment()
        mUntreatedFaultFragment = UntreatedFaultFragment()
        mFragments = ArrayList<Fragment>()
        mFragments?.add(mHandledFaultFragment!!)
        mFragments?.add(mUntreatedFaultFragment!!)
        mSelectAdapter = SelectFragmentAdapter(fragmentManager!!, mFragments!!)
        view_pager.setAdapter(mSelectAdapter)
    }


    private fun initClick() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        rb1.setChecked(true)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_ff)
                    }
                    1 -> {
                        rb2.setChecked(true)
                        rb1.setChecked(false)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_33)
                        //rb1.setBackgroundResource(R.color.c_ff)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        //rg  点击rg跳转
        rg.setOnCheckedChangeListener({ group, checkedId ->
            when (checkedId) {
                R.id.rb1 ->
                    index = 0
                R.id.rb2 -> index = 1
            }
            if (view_pager.getCurrentItem() != index) {
                view_pager.setCurrentItem(index, false)
            }
        })
    }
}