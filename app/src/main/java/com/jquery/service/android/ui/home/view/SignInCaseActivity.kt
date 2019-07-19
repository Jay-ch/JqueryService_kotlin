package com.jquery.service.android.ui.home.view

import android.view.View
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.CommCalendarView
import kotlinx.android.synthetic.main.activity_sign_in_case.*
import kotlinx.android.synthetic.main.include_title_bar.*
import java.util.*

/**
 * 签到情况
 * @author J.query
 * @date 2019/5/7
 * @email j-query@foxmail.com
 */
class SignInCaseActivity : BaseActivity() {

    override fun createLayout(): Int {
        return R.layout.activity_sign_in_case
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        calendarView.setSelectListener(object : CommCalendarView.SelectListener {
            override fun change(year: Int, month: Int) {
                tv_calendar_show.setText(String.format("%s 年 %s 月", year, month))
            }

            override fun select(year: Int, month: Int, day: Int, week: Int) {
                //Toast.makeText(applicationContext, String.format("%s 年 %s 月 %s日，周%s", year, month, day, week), Toast.LENGTH_SHORT).show()
                showToast(String.format("%s 年 %s 月 %s日，周%s", year, month, day, week))
            }
        })
        //代码选中一个日期
        tv_calendar_show.setOnClickListener(View.OnClickListener {
            calendarView.selectDate(2018, 9, 3)
        })
        //前一月
        calendar_previous.setOnClickListener { calendarView.showPreviousMonth() }
        //后一月
        calendar_next.setOnClickListener { calendarView.showNextMonth() }
        //返回今天
        tv_calendar_today.setOnClickListener { calendarView.backToday() }


        val sign = HashMap<String, Boolean>()
        sign["2018-07-12"] = true
        sign["2018-07-23"] = true
        sign["2018-07-24"] = false
        sign["2018-07-25"] = true
        sign["2018-08-12"] = false
        sign["2018-08-13"] = true
        sign["2018-08-14"] = true
        sign["2018-08-15"] = false
        sign["2018-08-18"] = false
        sign["2018-08-31"] = true
        sign["2018-09-05"] = true
        sign["2018-09-07"] = false
        sign["2018-09-08"] = false
        sign["2018-09-09"] = true
        sign["2018-10-09"] = true
        calendarView.setSignRecords(sign)
    }
}