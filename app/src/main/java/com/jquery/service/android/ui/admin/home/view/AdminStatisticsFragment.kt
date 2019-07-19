package com.jquery.service.android.ui.home.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.jquery.service.android.Base.BaseMvpFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.AdminFaultStatueAdapter
import com.jquery.service.android.adapter.AdminSignAdapter
import com.jquery.service.android.app.App
import com.jquery.service.android.logger.LogUtil
import com.jquery.service.android.ui.admin.home.view.FaultConditionActivity
import com.jquery.service.android.ui.home.model.AdminStatisticsContract
import com.jquery.service.android.ui.home.presenter.AdminStatisticsPresenter
import kotlinx.android.synthetic.main.fragment_admin_home_statistics_layout.*


/**
 * 管理员数据统计fragment
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */

class AdminStatisticsFragment : BaseMvpFragment<AdminStatisticsPresenter>(), AdminStatisticsContract.StatisticsView {


    private lateinit var mContent: Context
    private var mTitle: String? = null
    private var unSignList = ArrayList<String>()
    private var faultList = ArrayList<String>()
    private var mSignAdapter: AdminSignAdapter? = null
    private var mFaultStatueAdapter: AdminFaultStatueAdapter? = null


    override fun createPresenter(): AdminStatisticsPresenter {
        return AdminStatisticsPresenter()
    }


    companion object {
        fun getInstance(title: String): AdminStatisticsFragment {
            val fragment = AdminStatisticsFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun createLayout(): Int {
        return R.layout.fragment_admin_home_statistics_layout
    }

    override fun initViews() {
        super.initViews()
        mContent = App.mContext
//        mPresenter.WeatherTest("重庆")
        //showLoading("加载中....")

        //修改密码提示框
        //showPasswordChangeDialog()
        //权限提示框
        //showPermissionDialog()
        initRecentWork()
    }


    private fun initRecentWork() {
        mSignAdapter = activity?.let { AdminSignAdapter(it) }
        mFaultStatueAdapter = activity?.let { AdminFaultStatueAdapter(it) }
        rv_statistics_work.layoutManager = GridLayoutManager(context, 2)
        rv_statistics_work.adapter = mSignAdapter
        rv_statistics_work.isNestedScrollingEnabled = false
        rv_statistics_work_statue.layoutManager = LinearLayoutManager(context)
        rv_statistics_work_statue.adapter = mFaultStatueAdapter
        rv_statistics_work_statue.isNestedScrollingEnabled = false
    }

    override fun initData() {
        super.initData()
        LogUtil.e("getMessage", "initData")
        mSignAdapter?.addAll(unSignList)

        mFaultStatueAdapter?.addAll(faultList)
        mPresenter.getUnsignedEmployee()
        initClick()
    }

    private fun initClick() {
        tv_sign_case.setOnClickListener {
            startActivity(SignInCaseActivity::class.java)
        }
        tv_fault_case.setOnClickListener {
            startActivity(FaultConditionActivity::class.java)
        }
    }


    /**
     * 签到信息结果
     */
    override fun getUnsignedEmployeeSuccess(unSignLists: ArrayList<String>) {
        LogUtil.e("getMessage", "getUnsignedEmployeeSuccess")
        this.unSignList.clear()
        unSignList.addAll(unSignLists)
        mSignAdapter?.addAll(unSignList)

        statistics_circle_sign_statue.setProgress(99)
    }

    override fun getUnsignedEmployeeFail(reason: String?) {
        //TODO 获取未签到 信息失败
        showToast("获取未签到信息失败:" + reason)
    }

    /**
     * 故障信息结果
     */
    override fun getFaultStatueSuccess(faultLists: ArrayList<String>) {
        LogUtil.e("getMessage", "getFaultStatueSuccess" + faultLists.size)
        this.faultList.clear()
        this.faultList = faultLists
        mFaultStatueAdapter?.addAll(faultList)

        statistics_circle_fault_statue.setProgress(55)
    }

    override fun getFaultStatueFail(s: String?) {
        //TODO 获取故障信息失败
        showToast("故障信息结果失败:" + s)
    }
}

