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
class CommSignCalendarView : FrameLayout {

    private val mDestroyViews: ArraySet<CommSignCalendar> = ArraySet()
    private val mViewSet: ArraySet<CommSignCalendar> = ArraySet()
    private var signRecords: HashSet<String>? = null
    private var PAGER_SIZE = 1200
    private var pager: ViewPager? = null
    private var config: Config? = null
    private var currentPosition: Int = 0

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
        val array = context.obtainStyledAttributes(attrs, R.styleable.CommSignCalendarView, def, 0)
        config!!.weekHeight = array.getDimension(R.styleable.CommSignCalendarView_weekHeight, dip2px(30f).toFloat())
        config!!.weekBackgroundColor = array.getColor(R.styleable.CommSignCalendarView_weekBackgroundColor, Color.WHITE)
        config!!.weekTextColor = array.getColor(R.styleable.CommSignCalendarView_weekTextColor, Color.GRAY)
        config!!.weekTextSize = array.getDimension(R.styleable.CommSignCalendarView_weekTextSize, sp2px(14f))
        config!!.calendarTextColor = array.getColor(R.styleable.CommSignCalendarView_calendarTextColor, Color.BLACK)
        config!!.calendarTextSize = array.getDimension(R.styleable.CommSignCalendarView_calendarTextSize, sp2px(14f))
        config!!.isShowLunar = array.getBoolean(R.styleable.CommSignCalendarView_isShowLunar, false)
        if (config!!.isShowLunar) {
            config!!.lunarTextColor = array.getColor(R.styleable.CommSignCalendarView_lunarTextColor, Color.LTGRAY)
            config!!.lunarTextSize = array.getDimension(R.styleable.CommSignCalendarView_lunarTextSize, sp2px(11f))
        }
        config!!.todayTextColor = array.getColor(R.styleable.CommSignCalendarView_todayTextColor, Color.BLUE)
        config!!.signSize = array.getDimension(R.styleable.CommSignCalendarView_signSize, 0f)
        config!!.signTextColor = array.getColor(R.styleable.CommSignCalendarView_signTextColor, Color.parseColor("#BA7436"))
        config!!.limitFutureMonth = array.getBoolean(R.styleable.CommSignCalendarView_limitFutureMonth, false)
        config!!.signColor = array.getColor(R.styleable.CommSignCalendarView_signColor, Color.BLUE)
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
        pager!!.offscreenPageLimit = 1
        pager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                var position = position
                if (listener == null) return
                position -= 1
                val year = 1970 + position / 12
                val month = position % 12
                listener!!.change(year, month + 1)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        //选择当前日期
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        currentPosition = getPosition(year, month)
        if (config!!.limitFutureMonth) PAGER_SIZE = currentPosition + 1
        pager!!.adapter = CVAdapter()
        pager!!.setCurrentItem(currentPosition, false)
        post { if (listener != null) listener!!.change(year, month + 1) }
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
            mDestroyViews.add(`object` as CommSignCalendar)
            container.removeView(`object` as View)
        }
    }

    private fun getContent(position: Int): CommSignCalendar {
        val calendarView: CommSignCalendar?
        if (mDestroyViews.size != 0) {
            calendarView = mDestroyViews.valueAt(0)
            mDestroyViews.remove(calendarView)
        } else {
            calendarView = CommSignCalendar(context)
            config?.let { calendarView!!.init(it) }
            mViewSet.add(calendarView)
        }
        signRecords?.let { calendarView!!.setSignRecords(it) }
        calendarView!!.selectDate(position)
        calendarView!!.setTag(position)
        return calendarView
    }

    internal class Config {
        var weekHeight: Float = 0.toFloat()
        var weekTextSize: Float = 0.toFloat()
        var weekBackgroundColor: Int = 0
        var weekTextColor: Int = 0
        var calendarTextSize: Float = 0.toFloat()
        var calendarTextColor: Int = 0
        var isShowLunar: Boolean = false
        var lunarTextColor: Int = 0
        var lunarTextSize: Float = 0.toFloat()
        var todayTextColor: Int = 0
        var signColor: Int = 0
        var signSize: Float = 0.toFloat()
        var signTextColor: Int = 0
        var limitFutureMonth: Boolean = false
    }

    /*---------------------------------------对外方法-----------------------------------*/

    interface DateListener {
        fun change(year: Int, month: Int)
    }

    private var listener: DateListener? = null

    fun setDateListener(listener: DateListener) {
        this.listener = listener
    }


    fun selectMonth(year: Int, month: Int) {
        if (pager == null || year < 1970 || month < 1 || month > 12) return
        val position = getPosition(year, month)
        pager!!.setCurrentItem(position, false)
    }

    /**
     * 设置签到记录
     *
     * @param signRecords 为日期格式像 "2017-08-25"
     */
    fun setSignRecords(signRecords: HashSet<String>?) {
        if (signRecords == null) return
        this.signRecords = signRecords
        for (view in mViewSet) {
            view.setSignRecords(signRecords)
            view.invalidate()
        }
    }

    fun showNextMonth() {
        if (pager != null) {
            val index = pager!!.currentItem + 1
            pager!!.setCurrentItem(index, true)
        }
    }

    fun showPreviousMonth() {
        if (pager != null) {
            val index = pager!!.currentItem - 1
            pager!!.setCurrentItem(index, true)
        }
    }

    fun backCurrentMonth() {
        if (pager != null) {
            pager!!.setCurrentItem(currentPosition, false)
        }
    }
}