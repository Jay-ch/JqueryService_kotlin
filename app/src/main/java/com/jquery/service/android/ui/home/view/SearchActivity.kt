package com.jquery.service.android.ui.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import butterknife.OnClick
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.utils.DateFormatUtils
import com.jquery.service.android.widgets.CustomDatePicker
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.activity_search_layout.*
import kotlinx.android.synthetic.main.include_title_bar.*

/**
 * 检索+时间选择
 * @author J.query
 * @date 2019/4/3
 * @email j-query@foxmail.com
 */
class SearchActivity : BaseActivity() {

    private var mDatePicker: CustomDatePicker? = null
    private var mTimerPicker: CustomDatePicker? = null


    override fun createLayout(): Int {
        return R.layout.activity_search_layout
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        initDatePicker()
        initTimerPicker()
    }

    override fun initData() {
        super.initData()
        ll_date.setOnClickListener {
            mDatePicker?.show(tv_start_retrieve_day.getText().toString())
        }
        ll_time.setOnClickListener {
            mTimerPicker?.show(tv_selected_time.getText().toString())
        }
    }


    @SuppressLint("ResourceType")
    override fun setStatusBar() {
        top_title.visibility = View.VISIBLE
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setBackgroundResource(ContextCompat.getColor(this, R.color.c_ff))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }

    @OnClick(R.id.tv_back)
    fun onBackClick() {
        finish()
        val intent = Intent(this@SearchActivity, FaultActivity::class.java)
        intent.putExtra("id", 1)
        startActivity(intent)
    }

    private fun initDatePicker() {
        val beginTimestamp = "2009-01-01 00:00"
        //val beginTimestamp = DateFormatUtils.str2Long("2018-10-17 18:00", false)
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        //val endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.toString().subSequence(10, 16)
        var day = endTimestamp.toString().subSequence(0, 10)
        tv_selected_date.setText(time)
        tv_start_retrieve_day.setText(day)
        //tv_start_retrieve_day.setText(DateFormatUtils.long2Str(endTimestamp, false))
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                tv_start_retrieve_day.setText(DateFormatUtils.long2Str(timestamp, false))
            }
        }, beginTimestamp, endTimestamp)
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker?.setCancelable(false)
        // 不显示时和分
        mDatePicker?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker?.setScrollLoop(false)
        // 不允许滚动动画
        mDatePicker?.setCanShowAnim(false)
    }

    private fun initTimerPicker() {
        val beginTime = "2018-01-01 00:00"
        val endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTime.toString().subSequence(10, 16)
        var day = endTime.toString().subSequence(0, 10)
        tv_selected_time.setText(time)
        tv_end_retrieve_day.setText(day)
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                try {
                    var time = DateFormatUtils.getTimeString(timestamp)
                    var day = DateFormatUtils.getDayTimeString(timestamp)
                    tv_selected_time.setText(time)
                    tv_end_retrieve_day.setText(day)
                } catch (e: Exception) {
                    Log.e("時間：aaaaaaa", "" + e.toString())
                }

            }
        }, beginTime, endTime)
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker?.setCancelable(true)
        // 显示时和分
        mTimerPicker?.setCanShowPreciseTime(true)
        // 允许循环滚动
        mTimerPicker?.setScrollLoop(true)
        // 允许滚动动画
        mTimerPicker?.setCanShowAnim(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatePicker?.onDestroy()
    }
}