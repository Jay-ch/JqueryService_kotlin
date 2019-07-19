package com.jquery.service.android.ui.home.view

import android.support.v7.widget.LinearLayoutManager
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.TimeLineListCommAdapter
import com.jquery.service.android.entity.RecentWorkItem
import jquery.ptr.recyclerview.RecyclerAdapterWithHF
import kotlinx.android.synthetic.main.activity_liastre.*

/**
 * 故障维修
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class CrewAllFragment : BaseFragment() {
    var index: Int = 0
    override fun createLayout(): Int {
        return R.layout.fragment_crew_sign_layout
    }

    override fun initViews() {
        super.initViews()
        initRecentWork()
    }


    override fun initData() {
        super.initData()
    }


    private fun initRecentWork() {
        var map1 = RecentWorkItem("11.31", "故障维修（完成）", 1, "重庆市南岸区骑龙山庄3区7幢401", "2018.11.31  10:46:45")
        var map2 = RecentWorkItem("12.01", "签到[成功]", 2, "重庆市南岸区骑龙山庄3区7幢405", "2018.12.01  10:46:45")
        var map3 = RecentWorkItem("12.02", "故障维修（未完成）", 3, "重庆市南岸区骑龙山庄3区7幢402", "2018.12.02  10:46:45")
        var map4 = RecentWorkItem("12.03", "故障维修（完成）", 3, "重庆市南岸区骑龙山庄3区7幢402", "2018.12.02  10:46:45")
        var map5 = RecentWorkItem("12.04", "签到[未成功]", 2, "重庆市南岸区骑龙山庄3区7幢402", "2018.12.02  10:46:45")


        var list = mutableListOf<RecentWorkItem>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        list.add(3, map4)
        list.add(4, map5)
        var mTimeLineListCommAdapter = context?.let { TimeLineListCommAdapter(it) }
        //mTimeLineListCommAdapter?.setItemClickListener(this)
        var mAdapter = RecyclerAdapterWithHF(mTimeLineListCommAdapter)
        recyclerView_recent_work.setLayoutManager(LinearLayoutManager(context))
        //解决滑动冲突、滑动不流畅
        recyclerView_recent_work.setHasFixedSize(true)
        recyclerView_recent_work.setNestedScrollingEnabled(false)
        recyclerView_recent_work.setAdapter(mTimeLineListCommAdapter)
        mTimeLineListCommAdapter?.addAll(list)
    }
}