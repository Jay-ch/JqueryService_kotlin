package com.jquery.service.android.ui.home.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jquery.service.android.Base.BaseMvpFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.AdminSignAdapter
import com.jquery.service.android.adapter.TimeLineListCommAdapter
import com.jquery.service.android.app.App
import com.jquery.service.android.entity.*
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.fragment_home_maintenance_layout.*

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class HomeMyStatueFragment : BaseMvpFragment<HomePresenter>(), HomeContract.HomeView {


    private lateinit var mContent: Context
    private var mTitle: String? = null

    private var mAdminSignAdapter: AdminSignAdapter? = null


    private var mTimeLineListCommAdapter: TimeLineListCommAdapter? = null


    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }


    companion object {
        fun getInstance(title: String): HomeMyStatueFragment {
            val fragment = HomeMyStatueFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun createLayout(): Int {
        return R.layout.fragment_home_maintenance_layout
    }

    override fun initData() {
        super.initData()

    }

    override fun initViews() {
        super.initViews()
        mContent = App.mContext
        //showLoading("加载中....")
        initRecentWork()
        initMarquee()
        initClick()
    }

    private fun initClick() {
        tv_recent_work.setOnClickListener {
            startActivity(LiastreActivity::class.java)
        }
        corrective_maintenance.setOnClickListener {
            startActivity(FaultActivity::class.java)
        }
    }

    private fun initMarquee() {
        var data = mutableListOf<String>()
        for (i in 1..15) {
            data.add("有工程尚未完成！请及时处理 $i")
        }
        vertical_text.setTextStyle(resources.getDimension(R.dimen.dp_3), 0, resources.getColor(R.color.c_99))
        vertical_text.setAnimTime(1000)
        vertical_text.setTextStillTime(2000)
        vertical_text.setTextList(data)
        vertical_text.startAutoScroll()
    }

    private fun initRecentWork() {
        var map1 = RecentWorkItem("今日", "故障维修（完成）", 1, "重庆市南岸区骑龙山庄3区7幢401", "2018.11.31  10:46:45")
        var map2 = RecentWorkItem("昨天", "签到[成功]", 2, "重庆市南岸区骑龙山庄3区7幢405", "2018.12.01  10:46:45")
        var map3 = RecentWorkItem("前天", "故障维修（未完成）", 3, "重庆市南岸区骑龙山庄3区7幢402", "2018.12.02  10:46:45")
        var list = mutableListOf<RecentWorkItem>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        //list.add(3, map2)
        // list.add(4, map1)
        mTimeLineListCommAdapter = activity?.let { TimeLineListCommAdapter(it) }
        //mTimeLineListCommAdapter?.setItemClickListener(this)
        // mAdapter = RecyclerAdapterWithHF(mTimeLineListCommAdapter)
        recyclerView_recent_work.setLayoutManager(LinearLayoutManager(context))
        //解决滑动冲突、滑动不流畅
        recyclerView_recent_work.setHasFixedSize(true)
        recyclerView_recent_work.setNestedScrollingEnabled(false)
        recyclerView_recent_work.setAdapter(mTimeLineListCommAdapter)
        mTimeLineListCommAdapter?.addAll(list)
    }

    override fun loginSuccess(data: UserDetailEntity?, token: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loginFail(e: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wxLoginSuccess(data: UserInfoResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun WeatherTestSuccess(data: WeatherEntity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokenSuccess(token: TokenEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRegisterTokenSuccess(token: TokenEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTokenFail(s: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserInfoSuccess(result: UserInfoResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}