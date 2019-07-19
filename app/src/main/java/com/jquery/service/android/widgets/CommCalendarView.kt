package com.jquery.service.android.widgets

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v4.util.ArraySet
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jquery.service.android.R
import java.util.*

/**
 * 功能：签到日历
 * @author J.query
 * @date 2019/5/7
 * @email j-query@foxmail.com
 */
class CommCalendarView : FrameLayout {

    private val mDestroyViews: ArraySet<CommCalendar> = ArraySet()
    private val mViewSet: ArraySet<CommCalendar> = ArraySet()
    private var selectYear: Int = 0
    private var selectMonth: Int = 0
    private var selectDay: Int = 0
    private var selectWeek: Int = 0
    private var signRecords: HashMap<String, Boolean>? = null
    private var PAGER_SIZE = 1200
    private var calendar: Calendar? = null
    private var pager: ViewPager? = null
    private var config: Config? = null

    constructor(context: Context) : super(context) {

        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, def: Int) {
        config = Config()
        //获取日历的UI配置
        val array = context.obtainStyledAttributes(attrs, R.styleable.CommCalendarView, def, 0)
        config?.weekHeight = array.getDimension(R.styleable.CommCalendarView_weekHeight, dip2px(30f).toFloat())
        config?.weekBackgroundColor = array.getColor(R.styleable.CommCalendarView_weekBackgroundColor, Color.WHITE)
        config?.weekTextColor = array.getColor(R.styleable.CommCalendarView_weekTextColor, Color.GRAY)
        config?.weekTextSize = array.getDimension(R.styleable.CommCalendarView_weekTextSize, sp2px(14f))
        config?.calendarTextColor = array.getColor(R.styleable.CommCalendarView_calendarTextColor, Color.BLACK)
        config?.calendarTextSize = array.getDimension(R.styleable.CommCalendarView_calendarTextSize, sp2px(14f))
        config?.isShowOtherMonth = array.getBoolean(R.styleable.CommCalendarView_isShowOtherMonth, false)
        if (config?.isShowOtherMonth!!) {
            config?.otherMonthTextColor = array.getColor(R.styleable.CommCalendarView_otherMonthTextColor, Color.LTGRAY)
        }
        config?.isShowLunar = array.getBoolean(R.styleable.CommCalendarView_isShowLunar, false)
        if (config?.isShowLunar!!) {
            config?.lunarTextColor = array.getColor(R.styleable.CommCalendarView_lunarTextColor, Color.LTGRAY)
            config?.lunarTextSize = array.getDimension(R.styleable.CommCalendarView_lunarTextSize, sp2px(11f))
        }
        config?.todayTextColor = array.getColor(R.styleable.CommCalendarView_todayTextColor, Color.BLUE)
        config?.selectColor = array.getColor(R.styleable.CommCalendarView_selectColor, Color.BLUE)
        config?.selectTextColor = array.getColor(R.styleable.CommCalendarView_selectTextColor, Color.WHITE)
        config?.signIconSuccessId = array.getResourceId(R.styleable.CommCalendarView_signIconSuccessId, 0)
        config?.signIconErrorId = array.getResourceId(R.styleable.CommCalendarView_signIconErrorId, 0)
        if (config?.signIconSuccessId != 0) {
            config?.signIconSize = array.getDimension(R.styleable.CommCalendarView_signIconSize, dip2px(16f).toFloat())
        }
        config?.signTextColor = array.getColor(R.styleable.CommCalendarView_signTextColor, Color.parseColor("#BA7436"))
        config?.limitFutureMonth = array.getBoolean(R.styleable.CommCalendarView_limitFutureMonth, false)
        array.recycle()
        initPager()
    }

    private fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    private fun dip2px(dipValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)
        val hMode = View.MeasureSpec.getMode(heightMeasureSpec)
        height = if (hMode == View.MeasureSpec.EXACTLY) height else dip2px(220f)
        setMeasuredDimension(widthMeasureSpec, height)
    }

    private fun initPager() {
        pager = ViewPager(context)
        addView(pager)
        pager?.offscreenPageLimit = 1
        pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                var position = position
                if (listener == null) return
                position -= 1
                val year = 1970 + position / 12
                val month = position % 12
                listener?.change(year, month + 1)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        //选择当前日期
        calendar = Calendar.getInstance()
        selectYear = calendar?.get(Calendar.YEAR)!!
        selectMonth = calendar?.get(Calendar.MONTH)!!
        selectDay = calendar?.get(Calendar.DAY_OF_MONTH)!!
        selectWeek = calendar?.get(Calendar.DAY_OF_WEEK)!! - 1
        val currentPosition = getPosition(selectYear, selectMonth)
        if (config?.limitFutureMonth!!) PAGER_SIZE = currentPosition + 1
        pager?.adapter = CVAdapter()
        pager?.setCurrentItem(currentPosition, false)
    }

    private fun getPosition(year: Int, month: Int): Int {
        return (year - 1970) * 12 + month + 1
    }

    private inner class CVAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return PAGER_SIZE
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val calendarView = getContent(position)
            container.addView(calendarView)
            return calendarView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            mDestroyViews.add(`object` as CommCalendar)
            container.removeView(`object` as View)
        }
    }

    private fun getContent(position: Int): CommCalendar {
        val calendarView: CommCalendar?
        if (mDestroyViews.size != 0) {
            calendarView = mDestroyViews.valueAt(0)
            mDestroyViews.remove(calendarView)
        } else {
            calendarView = CommCalendar(context)
            config?.let { calendarView?.init(it) }
            calendarView?.setDateSelectListener(object : CommCalendar.DateSelectListener {
                override fun dateSelect(year: Int, month: Int, day: Int, week: Int) {
                    selectYear = year
                    selectMonth = month
                    selectDay = day
                    for (view in mViewSet) {
                        view.initSelect(selectYear, selectMonth, selectDay)
                    }
                    if (listener != null) {
                        listener?.select(year, month + 1, day, if (week == 0) 7 else week)
                    }
                }
            })
            mViewSet.add(calendarView)
        }
        signRecords?.let { calendarView?.setSignRecords(it) }
        calendarView?.selectDate(position)
        calendarView?.setTag(position)
        return calendarView!!
    }

    internal class Config {
        var weekHeight: Float = 0.toFloat()
        var weekTextSize: Float = 0.toFloat()
        var weekBackgroundColor: Int = 0
        var weekTextColor: Int = 0
        var calendarTextSize: Float = 0.toFloat()
        var calendarTextColor: Int = 0
        var isShowOtherMonth: Boolean = false
        var otherMonthTextColor: Int = 0
        var isShowLunar: Boolean = false
        var lunarTextColor: Int = 0
        var lunarTextSize: Float = 0.toFloat()
        var todayTextColor: Int = 0
        var selectColor: Int = 0
        var selectTextColor: Int = 0
        var signIconSuccessId: Int = 0
        var signIconErrorId: Int = 0
        var signIconSize: Float = 0.toFloat()
        var signTextColor: Int = 0
        var limitFutureMonth: Boolean = false
    }

    /*---------------------------------------对外方法-----------------------------------*/

    interface SelectListener {
        fun change(year: Int, month: Int)

        fun select(year: Int, month: Int, day: Int, week: Int)
    }

    private var listener: SelectListener? = null

    fun setSelectListener(listener: SelectListener) {
        this.listener = listener
        listener.change(selectYear, selectMonth + 1)
        selectWeek = if (selectWeek == 0) 7 else selectWeek
        listener.select(selectYear, selectMonth + 1, selectDay, selectWeek)
    }


    fun selectDate(year: Int, month: Int, day: Int) {
        var month = month
        if (listener == null || year < 1970 || month < 1 || month > 12 || day < 1 || day > 31)
            return
        month -= 1
        calendar?.set(year, month, day)
        val yearTemp = calendar?.get(Calendar.YEAR)
        val monthTemp = calendar?.get(Calendar.MONTH)
        val dayTemp = calendar?.get(Calendar.DAY_OF_MONTH)
        if (yearTemp != year || monthTemp != month || dayTemp != day) return
        selectYear = year
        selectMonth = month
        selectDay = day
        for (view in mViewSet) {
            view.initSelect(selectYear, selectMonth, selectDay)
        }
        val position = getPosition(selectYear, selectMonth)
        pager?.setCurrentItem(position, false)
        val week = calendar?.get(Calendar.DAY_OF_WEEK)!! - 1
        listener?.select(selectYear, selectMonth + 1, selectDay, if (week == 0) 7 else week)
    }

    fun selectMonth(year: Int, month: Int) {
        if (pager == null || year < 1970 || month < 1 || month > 12) return
        val position = getPosition(year, month)
        pager?.setCurrentItem(position, false)
    }

    /**
     * 设置签到记录
     *
     * @param signRecords 为日期格式像 "2017-08-25"
     */
    fun setSignRecords(signRecords: HashMap<String, Boolean>?) {
        if (signRecords == null) return
        this.signRecords = signRecords
        for (view in mViewSet) {
            view.setSignRecords(signRecords)
            view.invalidate()
        }
    }

    fun showNextMonth() {
        if (pager != null) {
            val index = pager?.currentItem!! + 1
            pager?.setCurrentItem(index, true)
        }
    }

    fun showPreviousMonth() {
        if (pager != null) {
            val index = pager?.currentItem!! - 1
            pager?.setCurrentItem(index, true)
        }
    }

    fun backToday() {
        if (pager != null) {
            val item = pager?.findViewWithTag<View>(pager?.currentItem) as CommCalendar
            if (item != null) {
                val info = item?.getCurrentDayInfo()
                selectDate(info[0], info[1] + 1, info[2])
            }
        }
    }
}