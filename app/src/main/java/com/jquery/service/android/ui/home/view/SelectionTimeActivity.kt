package com.jquery.service.android.ui.home.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.ui.admin.home.view.FaultConditionActivity
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.utils.DateFormatUtils
import com.jquery.service.android.utils.DateUtils.getMonthFirstday
import com.jquery.service.android.widgets.CustomDatePicker
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.activity_selection_time_layout.*
import kotlinx.android.synthetic.main.include_title_bar.*


/**
 * 时间选择
 * @author J.query
 * @date 2019/5/23
 * @email j-query@foxmail.com
 */
class SelectionTimeActivity : BaseActivity() {

    private var mDatePicker: CustomDatePicker? = null
    private var mTimerPicker: CustomDatePicker? = null
    private var mEndTimePicker: CustomDatePicker? = null
    private var mStartTimePicker: CustomDatePicker? = null

    private var mEndMonthTimePicker: CustomDatePicker? = null
    private var mStartMonthTimePicker: CustomDatePicker? = null

    //开始时间（05-01）
    private var mStartTime: String? = ""
    //结束时间（05-01）
    private var mEndTime: String? = ""
    //开始时间（05-01）
    private var mStartDay: String? = ""
    //结束时间（05-01）
    private var mEndDay: String? = ""
    //开始时间（2009）
    private var mStartYear: String? = ""
    //结束时间（2019）
    private var mEndYear: String? = ""
    //时间类型 in_month（05-01）  in_year（2009）
    private var time_type: String? = ""
    //获取当前年份月份,当月第一天
    private var beginTimestamp = getMonthFirstday() + " 00:00"

    //var beginTimestamp = "2009-01-01 00:00"
    override fun createLayout(): Int {
        return R.layout.activity_selection_time_layout
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        val bundle = intent?.getExtras()
        if (bundle != null) {
            if (bundle.containsKey("time_type")) {
                time_type = bundle.getString("time_type")
                if (time_type != null) {
                    if (time_type!!.contains("_month")) {
                        edt_search.visibility = View.GONE
                        initStartMonthTimePicker(beginTimestamp)
                        //beginTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
                        initEndMonthTimePicker(beginTimestamp)
                    } else if (time_type!!.contains("_year")) {
                        initStartTimePicker()
                        initEndTimePicker()
                    }
                }
            }
        }
        // initDatePicker()
        // initTimerPicker()
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setBackgroundColor(resources.getColor(R.color.c_ff))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }


    override fun initData() {
        super.initData()
        if (time_type != null) {
            if (time_type!!.contains("_month")) {
                ll_date.setOnClickListener {
                    mStartMonthTimePicker?.show(tv_start_retrieve_day.getText().toString())
                }
                ll_time.setOnClickListener {
                    mEndMonthTimePicker?.show(tv_selected_time.getText().toString())
                }
                if (time_type.equals("in_sign_month")) {
                    tv_back.setOnClickListener {
                        var bundle = Bundle()
                        if (time_type.equals("in_sign_month")) {
                            bundle.putString("time_type", time_type)
                            bundle.putString("in_sign_end_day", mEndDay)
                            bundle.putString("in_sign_start_day", mStartDay)
                            startActivity(SignRecordActivity::class.java, bundle)
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        }
                    }
                } else if (time_type.equals("in_liastre_month")) {
                    tv_back.setOnClickListener {
                        var bundle = Bundle()
                        if (time_type.equals("in_liastre_month")) {
                            bundle.putString("time_type", time_type)
                            bundle.putString("in_sign_end_day", mEndDay)
                            bundle.putString("in_sign_start_day", mStartDay)
                            startActivity(LiastreActivity::class.java, bundle)
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        }
                    }

                } else if (time_type.equals("in_condition_month")) {
                    tv_back.setOnClickListener {
                        var bundle = Bundle()
                        if (time_type.equals("in_condition_month")) {
                            bundle.putString("time_type", time_type)
                            bundle.putString("in_sign_end_day", mEndDay)
                            bundle.putString("in_sign_start_day", mStartDay)
                            startActivity(FaultConditionActivity::class.java, bundle)
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        }
                    }
                }
            } else if (time_type!!.contains("_year")) {
                ll_date.setOnClickListener {
                    mStartTimePicker?.show(tv_start_retrieve_day.getText().toString())
                }
                ll_time.setOnClickListener {
                    mEndTimePicker?.show(tv_selected_time.getText().toString())
                }
                if (time_type.equals("in_fault_year")) {
                    tv_back.setOnClickListener {
                        var bundle = Bundle()
                        if (time_type.equals("in_fault_year")) {
                            bundle.putString("time_type", time_type)
                            bundle.putString("in_sign_end_day", mEndDay)
                            bundle.putString("in_sign_start_day", mStartDay)
                            startActivity(FaultActivity::class.java, bundle)
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        }
                    }
                }
            }
        }
    }

    private fun initEndTimePicker() {
        val beginTimestamp = "2009-01-01 00:00"
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.toString().subSequence(10, 16)
        var day = endTimestamp.toString().subSequence(0, 10)
        tv_selected_time.setText(day)
        // 通过时间戳初始化日期，毫秒级别
        mEndTimePicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                tv_selected_time.setText(DateFormatUtils.long2Str(timestamp, false))
                var mEndDays = DateFormatUtils.long2Str(timestamp, false)
                mEndDays = mEndDays.toString().substring(5, 10)
                var mEndYear = mEndDays.toString().substring(0, 4)

            }
        }, beginTimestamp, endTimestamp)
        // 不允许点击屏幕或物理返回键关闭
        mEndTimePicker?.setCancelable(false)
        // 不显示时和分
        mEndTimePicker?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mEndTimePicker?.setScrollLoop(false)
        // 不允许滚动动画
        mEndTimePicker?.setCanShowAnim(false)
    }


    private fun initStartTimePicker() {
        val beginTimestamp = "2009-01-01 00:00"
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.toString().subSequence(10, 16)
        var day = endTimestamp.toString().subSequence(0, 10)
        tv_selected_date.setText(day)
        // 通过时间戳初始化日期，毫秒级别
        mStartTimePicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                tv_selected_date.setText(DateFormatUtils.long2Str(timestamp, false))
                var mStartDays = DateFormatUtils.long2Str(timestamp, false)
                var mStartYear = mStartDays.toString().substring(0, 4)
                mStartDays = mStartDays.toString().substring(5, 10)
            }
        }, beginTimestamp, endTimestamp)
        // 不允许点击屏幕或物理返回键关闭
        mStartTimePicker?.setCancelable(false)
        // 不显示时和分
        mStartTimePicker?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mStartTimePicker?.setScrollLoop(false)
        // 不允许滚动动画
        mStartTimePicker?.setCanShowAnim(false)
    }

    fun initStartMonthTimePicker(mBeginTimestamp: String) {
        beginTimestamp = mBeginTimestamp
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.subSequence(10, 16)
        var day = endTimestamp.subSequence(0, 10)
        var days = mBeginTimestamp.subSequence(0, 10)
        mStartDay = days.toString()
        tv_selected_date.setText(days)
        // 通过时间戳初始化日期，毫秒级别
        mStartMonthTimePicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                // tv_start_retrieve_day.setText(DateFormatUtils.long2Str(timestamp, false))
                tv_selected_date.setText(DateFormatUtils.long2Str(timestamp, false))
                var mStartDays = DateFormatUtils.long2Str(timestamp, false)
                var mStartYear = mStartDays.toString().substring(0, 4)
                mStartDays = mStartDays.toString().substring(5, 10)
                mStartDay = mStartDays
            }
        }, beginTimestamp, endTimestamp)
        // 不允许点击屏幕或物理返回键关闭
        mStartMonthTimePicker?.setCancelable(false)
        // 不显示时和分
        mStartMonthTimePicker?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mStartMonthTimePicker?.setScrollLoop(false)
        // 不允许滚动动画
        mStartMonthTimePicker?.setCanShowAnim(false)
    }

    private fun initEndMonthTimePicker(mBeginTimestamp: String) {
        beginTimestamp = mBeginTimestamp
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.toString().subSequence(10, 16)
        var day = endTimestamp.toString().subSequence(0, 10)
        tv_selected_time.setText(day)
        mEndDay = day.toString()
        // 通过时间戳初始化日期，毫秒级别
        mEndMonthTimePicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                tv_selected_time.setText(DateFormatUtils.long2Str(timestamp, false))
                var mEndDays = DateFormatUtils.long2Str(timestamp, false)
                mEndDays = mEndDays.toString().substring(5, 10)
                var mEndYear = mEndDays.toString().substring(0, 4)
                mEndDay = mEndDays
            }
        }, beginTimestamp, endTimestamp)
        // 不允许点击屏幕或物理返回键关闭
        mEndMonthTimePicker?.setCancelable(false)
        // 不显示时和分
        mEndMonthTimePicker?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mEndMonthTimePicker?.setScrollLoop(false)
        // 不允许滚动动画
        mEndMonthTimePicker?.setCanShowAnim(false)
    }

    private fun initDatePicker() {
        val beginTimestamp = "2009-01-01 00:00"
        val endTimestamp = DateFormatUtils.long2Str(System.currentTimeMillis(), true)
        var time = endTimestamp.toString().subSequence(10, 16)
        var day = endTimestamp.toString().subSequence(0, 10)
        tv_selected_date.setText(time)
        tv_start_retrieve_day.setText(day)
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
        tv_selected_time.setText(day)
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = CustomDatePicker(this, object : CustomDatePicker.Callback {
            override fun onTimeSelected(timestamp: Long) {
                Log.e("時間：", "" + DateFormatUtils.long2Str(timestamp, true))
                try {
                    var time = DateFormatUtils.getTimeString(timestamp)
                    var day = DateFormatUtils.getDayTimeString(timestamp)
                    tv_selected_time.setText(day)
                    Log.e("時間：aaaaaaa", "" + day)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            var bundle = Bundle()
            if (time_type.equals("in_sign_month")) {
                bundle.putString("time_type", time_type)
                bundle.putString("in_sign_end_day", mEndDay)
                bundle.putString("in_sign_start_day", mStartDay)
                startActivity(SignRecordActivity::class.java, bundle)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            } else if (time_type.equals("in_fault_year")) {
                bundle.putString("time_type", time_type)
                bundle.putString("in_sign_end_day", mEndDay)
                bundle.putString("in_sign_start_day", mStartDay)
                startActivity(FaultActivity::class.java, bundle)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            } else if (time_type.equals("in_liastre_month")) {
                var bundle = Bundle()
                if (time_type.equals("in_liastre_month")) {
                    bundle.putString("time_type", time_type)
                    bundle.putString("in_sign_end_day", mEndDay)
                    bundle.putString("in_sign_start_day", mStartDay)
                    startActivity(LiastreActivity::class.java, bundle)
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            } else if (time_type.equals("in_condition_month")) {
                var bundle = Bundle()
                if (time_type.equals("in_condition_month")) {
                    bundle.putString("time_type", time_type)
                    bundle.putString("in_sign_end_day", mEndDay)
                    bundle.putString("in_sign_start_day", mStartDay)
                    startActivity(FaultConditionActivity::class.java, bundle)
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatePicker?.onDestroy()
        mTimerPicker?.onDestroy()
        mEndTimePicker?.onDestroy()
        mStartTimePicker?.onDestroy()
        mStartMonthTimePicker?.onDestroy()
        mEndMonthTimePicker?.onDestroy()
    }
}