package com.jquery.service.android.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.SelectFragmentAdapter
import com.jquery.service.android.adapter.TimeLineListCommAdapter
import com.jquery.service.android.entity.RecentWorkItem
import com.jquery.service.android.ui.MainActivity
import com.jquery.service.android.utils.CommonsStatusBarUtil
import jquery.ptr.recyclerview.RecyclerAdapterWithHF
import kotlinx.android.synthetic.main.activity_liastre.*
import kotlinx.android.synthetic.main.include_title.*
import java.util.*

/**
 * 我的近况
 * @author J.query
 * @date 2019/3/12
 * @email j-query@foxmail.com
 */
class LiastreActivity : BaseActivity() {

    var index: Int = 0
    private var mFragments: ArrayList<Fragment>? = null
    private var mSelectAdapter: SelectFragmentAdapter? = null
    private var mCrewAllFragment: CrewAllFragment? = null
    private var mCrewFaultFragment: CrewFaultFragment? = null
    private var mCrewSignFragment: CrewSignFragment? = null

    var TAG = "LiastreActivity"
    override fun createLayout(): Int {
        return R.layout.activity_liastre
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(ContextCompat.getColor(this,R.color.c_ff))
        top_title.setTitleTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }


    override fun initViews() {
        super.initViews()
        setStatusBar()
        top_title.setViewLineVisible(true)
        initClick()
        //initRecentWork()
    }

    override fun initData() {
        super.initData()
        mCrewAllFragment = CrewAllFragment()
        mCrewFaultFragment = CrewFaultFragment()
        mCrewSignFragment = CrewSignFragment()
        mFragments = ArrayList<Fragment>()
        mFragments?.add(mCrewAllFragment!!)
        mFragments?.add(mCrewSignFragment!!)
        mFragments?.add(mCrewFaultFragment!!)
        mSelectAdapter = SelectFragmentAdapter(getSupportFragmentManager(), mFragments!!)
        view_pager.setAdapter(mSelectAdapter)
        ll_date_change.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("time_type", "in_liastre_month")
            startActivity(SelectionTimeActivity::class.java, bundle)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onRestart() {
        super.onRestart()
        val bundle = getIntent().extras
        if (bundle != null) {
            if (bundle.containsKey("time_type")) {
                var time_type = bundle?.getString("time_type")
                if (time_type != null && time_type.equals("in_liastre_month")) {
                    var mEndDay = bundle?.getString("in_sign_end_day")
                    var mStartDay = bundle?.getString("in_sign_start_day")
                    tv_date_change.setText(mStartDay + "~" + mEndDay)
                }
            }
        }
        log(TAG + "-------onResume------")
    }

    private fun initRecentWork() {
        var map1 = RecentWorkItem("11.31", "故障维修（完成）", 1, "重庆市南岸区骑龙山庄3区7幢401", "2018.11.31  10:46:45")
        var map2 = RecentWorkItem("12.01", "签到[成功]", 2, "重庆市南岸区骑龙山庄3区7幢405", "2018.12.01  10:46:45")
        var map3 = RecentWorkItem("12.02", "故障维修（未完成）", 3, "重庆市南岸区骑龙山庄3区7幢402", "2018.12.02  10:46:45")
        var list = mutableListOf<RecentWorkItem>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        list.add(3, map2)
        list.add(4, map1)
        var mTimeLineListCommAdapter = TimeLineListCommAdapter(this)
        //mTimeLineListCommAdapter?.setItemClickListener(this)
        var mAdapter = RecyclerAdapterWithHF(mTimeLineListCommAdapter)
        recyclerView_recent_work.setLayoutManager(LinearLayoutManager(this))
        //解决滑动冲突、滑动不流畅
        recyclerView_recent_work.setHasFixedSize(true)
        recyclerView_recent_work.setNestedScrollingEnabled(false)
        recyclerView_recent_work.setAdapter(mTimeLineListCommAdapter)
        mTimeLineListCommAdapter?.addAll(list)
    }

    private fun initClick() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> {
                        rb_sign.setChecked(true)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_ff)
                    }
                    2 -> {
                        rb_fault.setChecked(true)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_ff)
                    }
                    0 -> {
                        rb_all.setChecked(true)
                        rb_fault.setChecked(false)
                        rb_sign.setChecked(false)
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
                R.id.rb_sign ->
                    index = 1
                R.id.rb_all ->
                    index = 0
                R.id.rb_fault ->
                    index = 2
            }
            if (view_pager.getCurrentItem() != index) {
                view_pager.setCurrentItem(index, false)
            }
        })
        tv_back.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("id", "1")
            startActivity(MainActivity::class.java, bundle)
            finish()

        }
        img_right.setOnClickListener {
            /*   val intent = Intent(this@FaultActivity, SearchActivity::class.java)
               intent.putExtra("id", 1)
               startActivity(intent)*/
            var bundle = Bundle()
            bundle.putString("time_type", "in_fault_year")
            startActivity(SelectionTimeActivity::class.java, bundle)
        }
        /*  bt_new_fault.setOnClickListener {
              val intent = Intent(this@FaultActivity, NewFaultActivity::class.java)
              intent.putExtra("id", 1)
              startActivity(intent)
          }*/
    }
}