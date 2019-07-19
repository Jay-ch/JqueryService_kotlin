package com.jquery.service.android.ui.admin.home.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jquery.service.android.Base.BaseMvpActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.FaultConditionAdapter
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.logger.LogUtil
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import com.jquery.service.android.ui.home.view.SelectionTimeActivity
import com.jquery.service.android.utils.CommonsStatusBarUtil
import kotlinx.android.synthetic.main.activity_fault_condition.*
import kotlinx.android.synthetic.main.include_title_bar.*

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class FaultConditionActivity : BaseMvpActivity<HomePresenter>(), HomeContract.HomeView, View.OnClickListener {


    private var mFaultConditionAdapter: FaultConditionAdapter? = null

    private var faultList = ArrayList<String>()

    override fun createLayout(): Int {
        return R.layout.activity_fault_condition
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun initViews() {
        super.initViews()
        setStatusWhiteBar()
        mFaultConditionAdapter = this.let { FaultConditionAdapter(it) }
        rv_fault_condition.layoutManager = LinearLayoutManager(this)
        rv_fault_condition.adapter = mFaultConditionAdapter

        ll_fault_statistics_way.setOnClickListener {
            showWaySelectionDialog()
        }
    }
     fun setStatusWhiteBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }
    private fun showWaySelectionDialog() {


    }

    override fun initData() {
        super.initData()
        LogUtil.e("getMessage", "initData")
        faultList.add("异常类型1")
        faultList.add("异常类型1")
        faultList.add("异常类型2")
        faultList.add("异常类型2异常类型2")
        faultList.add("异常类型3")
        faultList.add("异常类型3")
        faultList.add("异常类型4")
        faultList.add("异常类型4")
        faultList.add("异常类型5")
        faultList.add("异常类型5异常类型5")
        mFaultConditionAdapter?.addAll(faultList)


        rl_date_change.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("time_type", "in_condition_month")
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
                if (time_type != null && time_type.equals("in_condition_month")) {
                    var mEndDay = bundle?.getString("in_sign_end_day")
                    var mStartDay = bundle?.getString("in_sign_start_day")
                    tv_start_time.setText(mStartDay)
                    tv_end_time.setText(mEndDay)
                }
            }
        }
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


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}