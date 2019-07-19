package com.jquery.service.android.widgets

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.jquery.service.android.R
import com.jquery.service.android.utils.DateFormatUtils
import java.text.DecimalFormat
import java.util.*

/**
 * @author J.query
 * @date 2019/5/17
 * @email j-query@foxmail.com
 */
class CustomDatePicker : View.OnClickListener, PickerView.OnSelectListener {


    private var mContext: Context? = null
    private var mCallback: Callback? = null
    private var mBeginTime: Calendar? = null
    private var mEndTime: Calendar? = null
    private var mSelectedTime: Calendar? = null
    private var mCanDialogShow: Boolean = false

    private var mPickerDialog: Dialog? = null
    private var mDpvYear: PickerView? = null
    private var mDpvMonth: PickerView? = null
    private var mDpvDay: PickerView? = null
    private var mDpvHour: PickerView? = null
    private var mDpvMinute: PickerView? = null
    private var mTvHourUnit: TextView? = null
    private var mTvMinuteUnit: TextView? = null

    private var mBeginYear: Int = 0
    private var mBeginMonth: Int = 0
    private var mBeginDay: Int = 0
    private var mBeginHour: Int = 0
    private var mBeginMinute: Int = 0
    private var mEndYear: Int = 0
    private var mEndMonth: Int = 0
    private var mEndDay: Int = 0
    private var mEndHour: Int = 0
    private var mEndMinute: Int = 0
    private val mYearUnits = ArrayList<String>()
    private val mMonthUnits = ArrayList<String>()
    private val mDayUnits = ArrayList<String>()
    private val mHourUnits = ArrayList<String>()
    private val mMinuteUnits = ArrayList<String>()
    private val mDecimalFormat = DecimalFormat("00")

    private var mCanShowPreciseTime: Boolean = false


    /**
     * 时间单位：时、分
     */
    private var SCROLL_UNIT_HOUR = 1
    private var SCROLL_UNIT_MINUTE = 2

    private var mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE
    /**
     * 时间单位的最大显示值
     */
    private val MAX_MINUTE_UNIT = 59
    private val MAX_HOUR_UNIT = 23
    private val MAX_MONTH_UNIT = 12

    /**
     * 级联滚动延迟时间
     */
    private val LINKAGE_DELAY_DEFAULT = 100L

    /**
     * 时间选择结果回调接口
     */
    interface Callback {
        fun onTimeSelected(timestamp: Long)
    }

    /**
     * 通过日期字符串初始换时间选择器
     *
     * @param context      Activity Context
     * @param callback     选择结果回调
     * @param beginDateStr 日期字符串，格式为 yyyy-MM-dd HH:mm
     * @param endDateStr   日期字符串，格式为 yyyy-MM-dd HH:mm
     */
    constructor(context: Context, callback: Callback, beginDateStr: String, endDateStr: String) : this(context, callback, DateFormatUtils.str2Long(beginDateStr, true), DateFormatUtils.str2Long(endDateStr, true))


    /**
     * 通过时间戳初始换时间选择器，毫秒级别
     *
     * @param context        Activity Context
     * @param callback       选择结果回调
     * @param beginTimestamp 毫秒级时间戳
     * @param endTimestamp   毫秒级时间戳
     */
    constructor(context: Context?, callback: Callback?, beginTimestamp: Long, endTimestamp: Long) : super() {
        if (context == null || callback == null || beginTimestamp <= 0 || beginTimestamp >= endTimestamp) {
            mCanDialogShow = false
            return
        }

        mContext = context
        mCallback = callback
        mBeginTime = Calendar.getInstance()
        mBeginTime?.timeInMillis = beginTimestamp
        mEndTime = Calendar.getInstance()
        mEndTime?.timeInMillis = endTimestamp
        mSelectedTime = Calendar.getInstance()

        initView()
        initData()
        mCanDialogShow = true
    }

    private fun initView() {
        mPickerDialog = Dialog(mContext, R.style.date_picker_dialog)
        mPickerDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mPickerDialog?.setContentView(R.layout.dialog_date_picker)

        val window = mPickerDialog?.window
        if (window != null) {
            val lp = window.attributes
            lp.gravity = Gravity.BOTTOM
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = lp
        }

        mPickerDialog?.findViewById<View>(R.id.tv_cancel)?.setOnClickListener(this)
        mPickerDialog?.findViewById<View>(R.id.tv_confirm)?.setOnClickListener(this)
        mTvHourUnit = mPickerDialog?.findViewById(R.id.tv_hour_unit)
        mTvMinuteUnit = mPickerDialog?.findViewById<TextView>(R.id.tv_minute_unit)

        mDpvYear = mPickerDialog?.findViewById<View>(R.id.dpv_year) as PickerView?
        mDpvYear?.setOnSelectListener(this)
        mDpvMonth = mPickerDialog?.findViewById<View>(R.id.dpv_month) as PickerView?
        mDpvMonth?.setOnSelectListener(this)
        mDpvDay = mPickerDialog?.findViewById<View>(R.id.dpv_day) as PickerView?
        mDpvDay?.setOnSelectListener(this)
        mDpvHour = mPickerDialog?.findViewById<View>(R.id.dpv_hour) as PickerView?
        mDpvHour?.setOnSelectListener(this)
        mDpvMinute = mPickerDialog?.findViewById<View>(R.id.dpv_minute) as PickerView?
        mDpvMinute?.setOnSelectListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> {
            }

            R.id.tv_confirm -> if (mCallback != null) {
                mCallback?.onTimeSelected(mSelectedTime?.timeInMillis!!)
            }
        }

        if (mPickerDialog != null && mPickerDialog?.isShowing!!) {
            mPickerDialog?.dismiss()
        }
    }

    override fun onSelect(view: View?, selected: String?) {
        if (view == null || TextUtils.isEmpty(selected)) return

        val timeUnit: Int
        try {
            timeUnit = Integer.parseInt(selected)
        } catch (ignored: Throwable) {
            return
        }

        when (view.id) {
            R.id.dpv_year -> {
                mSelectedTime?.set(Calendar.YEAR, timeUnit)
                linkageMonthUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_month -> {
                // 防止类似 2018/12/31 滚动到11月时因溢出变成 2018/12/01
                val lastSelectedMonth = mSelectedTime?.get(Calendar.MONTH)!! + 1
                mSelectedTime?.add(Calendar.MONTH, timeUnit - lastSelectedMonth)
                linkageDayUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_day -> {
                mSelectedTime?.set(Calendar.DAY_OF_MONTH, timeUnit)
                linkageHourUnit(true, LINKAGE_DELAY_DEFAULT)
            }

            R.id.dpv_hour -> {
                mSelectedTime?.set(Calendar.HOUR_OF_DAY, timeUnit)
                linkageMinuteUnit(true)
            }

            R.id.dpv_minute -> mSelectedTime?.set(Calendar.MINUTE, timeUnit)
        }
    }

    private fun initData() {
        mSelectedTime?.timeInMillis = mBeginTime?.timeInMillis!!

        mBeginYear = mBeginTime?.get(Calendar.YEAR)!!
        // Calendar.MONTH 值为 0-11
        mBeginMonth = mBeginTime?.get(Calendar.MONTH)!! + 1
        mBeginDay = mBeginTime?.get(Calendar.DAY_OF_MONTH)!!
        mBeginHour = mBeginTime?.get(Calendar.HOUR_OF_DAY)!!
        mBeginMinute = mBeginTime?.get(Calendar.MINUTE)!!

        mEndYear = mEndTime?.get(Calendar.YEAR)!!
        mEndMonth = mEndTime?.get(Calendar.MONTH)!! + 1
        mEndDay = mEndTime?.get(Calendar.DAY_OF_MONTH)!!
        mEndHour = mEndTime?.get(Calendar.HOUR_OF_DAY)!!
        mEndMinute = mEndTime?.get(Calendar.MINUTE)!!

        val canSpanYear = mBeginYear != mEndYear
        val canSpanMon = !canSpanYear && mBeginMonth != mEndMonth
        val canSpanDay = !canSpanMon && mBeginDay != mEndDay
        val canSpanHour = !canSpanDay && mBeginHour != mEndHour
        val canSpanMinute = !canSpanHour && mBeginMinute != mEndMinute
        if (canSpanYear) {
            initDateUnits(MAX_MONTH_UNIT, mBeginTime?.getActualMaximum(Calendar.DAY_OF_MONTH)!!, MAX_HOUR_UNIT, MAX_MINUTE_UNIT)
        } else if (canSpanMon) {
            initDateUnits(mEndMonth, mBeginTime?.getActualMaximum(Calendar.DAY_OF_MONTH)!!, MAX_HOUR_UNIT, MAX_MINUTE_UNIT)
        } else if (canSpanDay) {
            initDateUnits(mEndMonth, mEndDay, MAX_HOUR_UNIT, MAX_MINUTE_UNIT)
        } else if (canSpanHour) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, MAX_MINUTE_UNIT)
        } else if (canSpanMinute) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, mEndMinute)
        }
    }

    private fun initDateUnits(endMonth: Int, endDay: Int, endHour: Int, endMinute: Int) {
        for (i in mBeginYear..mEndYear) {
            mYearUnits.add(i.toString())
        }

        for (i in mBeginMonth..endMonth) {
            mMonthUnits.add(mDecimalFormat.format(i.toLong()))
        }

        for (i in mBeginDay..endDay) {
            mDayUnits.add(mDecimalFormat.format(i.toLong()))
        }

        if (mScrollUnits and SCROLL_UNIT_HOUR != SCROLL_UNIT_HOUR) {
            mHourUnits.add(mDecimalFormat.format(mBeginHour.toLong()))
        } else {
            for (i in mBeginHour..endHour) {
                mHourUnits.add(mDecimalFormat.format(i.toLong()))
            }
        }

        if (mScrollUnits and SCROLL_UNIT_MINUTE != SCROLL_UNIT_MINUTE) {
            mMinuteUnits.add(mDecimalFormat.format(mBeginMinute.toLong()))
        } else {
            for (i in mBeginMinute..endMinute) {
                mMinuteUnits.add(mDecimalFormat.format(i.toLong()))
            }
        }

        mDpvYear?.setDataList(mYearUnits)
        mDpvYear?.setSelected(0)
        mDpvMonth?.setDataList(mMonthUnits)
        mDpvMonth?.setSelected(0)
        mDpvDay?.setDataList(mDayUnits)
        mDpvDay?.setSelected(0)
        mDpvHour?.setDataList(mHourUnits)
        mDpvHour?.setSelected(0)
        mDpvMinute?.setDataList(mMinuteUnits)
        mDpvMinute?.setSelected(0)

        setCanScroll()
    }

    private fun setCanScroll() {
        mDpvYear?.setCanScroll(mYearUnits.size > 1)
        mDpvMonth?.setCanScroll(mMonthUnits.size > 1)
        mDpvDay?.setCanScroll(mDayUnits.size > 1)
        mDpvHour?.setCanScroll(mHourUnits.size > 1 && mScrollUnits and SCROLL_UNIT_HOUR == SCROLL_UNIT_HOUR)
        mDpvMinute?.setCanScroll(mMinuteUnits.size > 1 && mScrollUnits and SCROLL_UNIT_MINUTE == SCROLL_UNIT_MINUTE)
    }

    /**
     * 联动“月”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageMonthUnit(showAnim: Boolean, delay: Long) {
        val minMonth: Int
        val maxMonth: Int
        val selectedYear = mSelectedTime?.get(Calendar.YEAR)
        if (mBeginYear == mEndYear) {
            minMonth = mBeginMonth
            maxMonth = mEndMonth
        } else if (selectedYear == mBeginYear) {
            minMonth = mBeginMonth
            maxMonth = MAX_MONTH_UNIT
        } else if (selectedYear == mEndYear) {
            minMonth = 1
            maxMonth = mEndMonth
        } else {
            minMonth = 1
            maxMonth = MAX_MONTH_UNIT
        }

        // 重新初始化时间单元容器
        mMonthUnits.clear()
        for (i in minMonth..maxMonth) {
            mMonthUnits.add(mDecimalFormat.format(i.toLong()))
        }
        mDpvMonth?.setDataList(mMonthUnits)

        // 确保联动时不会溢出或改变关联选中值
        val selectedMonth = getValueInRange(mSelectedTime?.get(Calendar.MONTH)!! + 1, minMonth, maxMonth)
        mSelectedTime?.set(Calendar.MONTH, selectedMonth - 1)
        mDpvMonth?.setSelected(selectedMonth - minMonth)
        if (showAnim) {
            mDpvMonth?.startAnim()
        }

        // 联动“日”变化
        mDpvMonth?.postDelayed(Runnable { linkageDayUnit(showAnim, delay) }, delay)
    }

    /**
     * 联动“日”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageDayUnit(showAnim: Boolean, delay: Long) {
        val minDay: Int
        val maxDay: Int
        val selectedYear = mSelectedTime?.get(Calendar.YEAR)
        val selectedMonth = mSelectedTime?.get(Calendar.MONTH)!! + 1
        if (mBeginYear == mEndYear && mBeginMonth == mEndMonth) {
            minDay = mBeginDay
            maxDay = mEndDay
        } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth) {
            minDay = mBeginDay
            maxDay = mSelectedTime?.getActualMaximum(Calendar.DAY_OF_MONTH)!!
        } else if (selectedYear == mEndYear && selectedMonth == mEndMonth) {
            minDay = 1
            maxDay = mEndDay
        } else {
            minDay = 1
            maxDay = mSelectedTime?.getActualMaximum(Calendar.DAY_OF_MONTH)!!
        }

        mDayUnits.clear()
        for (i in minDay..maxDay) {
            mDayUnits.add(mDecimalFormat.format(i.toLong()))
        }
        mDpvDay?.setDataList(mDayUnits)

        val selectedDay = getValueInRange(mSelectedTime?.get(Calendar.DAY_OF_MONTH)!!, minDay, maxDay)
        mSelectedTime?.set(Calendar.DAY_OF_MONTH, selectedDay)
        mDpvDay?.setSelected(selectedDay - minDay)
        if (showAnim) {
            mDpvDay?.startAnim()
        }

        mDpvDay?.postDelayed(Runnable { linkageHourUnit(showAnim, delay) }, delay)
    }

    /**
     * 联动“时”变化
     *
     * @param showAnim 是否展示滚动动画
     * @param delay    联动下一级延迟时间
     */
    private fun linkageHourUnit(showAnim: Boolean, delay: Long) {
        if (mScrollUnits and SCROLL_UNIT_HOUR == SCROLL_UNIT_HOUR) {
            val minHour: Int
            val maxHour: Int
            val selectedYear = mSelectedTime?.get(Calendar.YEAR)
            val selectedMonth = mSelectedTime?.get(Calendar.MONTH)!! + 1
            val selectedDay = mSelectedTime?.get(Calendar.DAY_OF_MONTH)
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay) {
                minHour = mBeginHour
                maxHour = mEndHour
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay) {
                minHour = mBeginHour
                maxHour = MAX_HOUR_UNIT
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay) {
                minHour = 0
                maxHour = mEndHour
            } else {
                minHour = 0
                maxHour = MAX_HOUR_UNIT
            }

            mHourUnits.clear()
            for (i in minHour..maxHour) {
                mHourUnits.add(mDecimalFormat.format(i.toLong()))
            }
            mDpvHour?.setDataList(mHourUnits)

            val selectedHour = getValueInRange(mSelectedTime?.get(Calendar.HOUR_OF_DAY)!!, minHour, maxHour)
            mSelectedTime?.set(Calendar.HOUR_OF_DAY, selectedHour)
            mDpvHour?.setSelected(selectedHour - minHour)
            if (showAnim) {
                mDpvHour?.startAnim()
            }
        }

        mDpvHour?.postDelayed(Runnable { linkageMinuteUnit(showAnim) }, delay)
    }

    /**
     * 联动“分”变化
     *
     * @param showAnim 是否展示滚动动画
     */
    private fun linkageMinuteUnit(showAnim: Boolean) {
        if (mScrollUnits and SCROLL_UNIT_MINUTE == SCROLL_UNIT_MINUTE) {
            val minMinute: Int
            val maxMinute: Int
            val selectedYear = mSelectedTime?.get(Calendar.YEAR)
            val selectedMonth = mSelectedTime?.get(Calendar.MONTH)!! + 1
            val selectedDay = mSelectedTime?.get(Calendar.DAY_OF_MONTH)
            val selectedHour = mSelectedTime?.get(Calendar.HOUR_OF_DAY)
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay && mBeginHour == mEndHour) {
                minMinute = mBeginMinute
                maxMinute = mEndMinute
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay && selectedHour == mBeginHour) {
                minMinute = mBeginMinute
                maxMinute = MAX_MINUTE_UNIT
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay && selectedHour == mEndHour) {
                minMinute = 0
                maxMinute = mEndMinute
            } else {
                minMinute = 0
                maxMinute = MAX_MINUTE_UNIT
            }

            mMinuteUnits.clear()
            for (i in minMinute..maxMinute) {
                mMinuteUnits.add(mDecimalFormat.format(i.toLong()))
            }
            mDpvMinute?.setDataList(mMinuteUnits)

            val selectedMinute = getValueInRange(mSelectedTime?.get(Calendar.MINUTE)!!, minMinute, maxMinute)
            mSelectedTime?.set(Calendar.MINUTE, selectedMinute)
            mDpvMinute?.setSelected(selectedMinute - minMinute)
            if (showAnim) {
                mDpvMinute?.startAnim()
            }
        }

        setCanScroll()
    }

    private fun getValueInRange(value: Int, minValue: Int, maxValue: Int): Int {
        return if (value < minValue) {
            minValue
        } else if (value > maxValue) {
            maxValue
        } else {
            value
        }
    }

    /**
     * 展示时间选择器
     *
     * @param dateStr 日期字符串，格式为 yyyy-MM-dd 或 yyyy-MM-dd HH:mm
     */
    fun show(dateStr: String) {
        if (!canShow() || TextUtils.isEmpty(dateStr)) return

        // 弹窗时，考虑用户体验，不展示滚动动画
        if (setSelectedTime(dateStr, false)) {
            mPickerDialog?.show()
        }
    }

    private fun canShow(): Boolean {
        return mCanDialogShow && mPickerDialog != null
    }

    /**
     * 设置日期选择器的选中时间
     *
     * @param dateStr  日期字符串
     * @param showAnim 是否展示动画
     * @return 是否设置成功
     */
    fun setSelectedTime(dateStr: String, showAnim: Boolean): Boolean {
        return (canShow() && !TextUtils.isEmpty(dateStr)
                && setSelectedTime(DateFormatUtils.str2Long(dateStr, mCanShowPreciseTime), showAnim))
    }

    /**
     * 展示时间选择器
     *
     * @param timestamp 时间戳，毫秒级别
     */
    fun show(timestamp: Long) {
        if (!canShow()) return

        if (setSelectedTime(timestamp, false)) {
            mPickerDialog?.show()
        }
    }

    /**
     * 设置日期选择器的选中时间
     *
     * @param timestamp 毫秒级时间戳
     * @param showAnim  是否展示动画
     * @return 是否设置成功
     */
    fun setSelectedTime(timestamp: Long, showAnim: Boolean): Boolean {
        var timestamp = timestamp
        if (!canShow()) return false

        if (timestamp < mBeginTime?.timeInMillis!!) {
            timestamp = mBeginTime?.timeInMillis!!
        } else if (timestamp > mEndTime?.timeInMillis!!) {
            timestamp = mEndTime?.timeInMillis!!
        }
        mSelectedTime?.timeInMillis = timestamp

        mYearUnits.clear()
        for (i in mBeginYear..mEndYear) {
            mYearUnits.add(i.toString())
        }
        mDpvYear?.setDataList(mYearUnits)
        mDpvYear?.setSelected(mSelectedTime?.get(Calendar.YEAR)!! - mBeginYear)
        linkageMonthUnit(showAnim, if (showAnim) LINKAGE_DELAY_DEFAULT else 0)
        return true
    }

    /**
     * 设置是否允许点击屏幕或物理返回键关闭
     */
    fun setCancelable(cancelable: Boolean) {
        if (!canShow()) return

        mPickerDialog?.setCancelable(cancelable)
    }

    /**
     * 设置日期控件是否显示时和分
     */
    fun setCanShowPreciseTime(canShowPreciseTime: Boolean) {
        if (!canShow()) return

        if (canShowPreciseTime) {
            initScrollUnit()
            mDpvHour?.setVisibility(View.VISIBLE)
            mTvHourUnit?.visibility = View.VISIBLE
            mDpvMinute?.setVisibility(View.VISIBLE)
            mTvMinuteUnit?.setVisibility(View.VISIBLE)
        } else {
            initScrollUnit(SCROLL_UNIT_HOUR, SCROLL_UNIT_MINUTE)
            mDpvHour?.setVisibility(View.GONE)
            mTvHourUnit?.visibility = View.GONE
            mDpvMinute?.setVisibility(View.GONE)
            mTvMinuteUnit?.setVisibility(View.GONE)
        }
        mCanShowPreciseTime = canShowPreciseTime
    }

    private fun initScrollUnit(vararg units: Int) {
        if (units == null || units.size == 0) {
            mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE
        } else {
            for (unit in units) {
                mScrollUnits = mScrollUnits xor unit
            }
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    fun setScrollLoop(canLoop: Boolean) {
        if (!canShow()) return

        mDpvYear?.setCanScrollLoop(canLoop)
        mDpvMonth?.setCanScrollLoop(canLoop)
        mDpvDay?.setCanScrollLoop(canLoop)
        mDpvHour?.setCanScrollLoop(canLoop)
        mDpvMinute?.setCanScrollLoop(canLoop)
    }

    /**
     * 设置日期控件是否展示滚动动画
     */
    fun setCanShowAnim(canShowAnim: Boolean) {
        if (!canShow()) return

        mDpvYear?.setCanShowAnim(canShowAnim)
        mDpvMonth?.setCanShowAnim(canShowAnim)
        mDpvDay?.setCanShowAnim(canShowAnim)
        mDpvHour?.setCanShowAnim(canShowAnim)
        mDpvMinute?.setCanShowAnim(canShowAnim)
    }

    /**
     * 销毁弹窗
     */
    fun onDestroy() {
        if (mPickerDialog != null) {
            mPickerDialog?.dismiss()
            mPickerDialog = null

            mDpvYear?.onDestroy()
            mDpvMonth?.onDestroy()
            mDpvDay?.onDestroy()
            mDpvHour?.onDestroy()
            mDpvMinute?.onDestroy()
        }
    }
}