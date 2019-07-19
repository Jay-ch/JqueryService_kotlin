package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author J.query
 * @date 2019/5/7
 * @email j-query@foxmail.com
 */
class CommSignCalendar : View {

    private val weekTitles = arrayOf("日", "一", "二", "三", "四", "五", "六")
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    private val calendar = Calendar.getInstance()
    //今天所在的年月日信息
    private var currentYear: Int = 0
    private var currentMonth: Int = 0
    private var currentDay: Int = 0
    private val date = GregorianCalendar()
    private var mConfig: CommSignCalendarView.Config? = null
    private var signRecords: HashSet<String>? = null
    private val paint = Paint()
    private var lunarHelper: LunarHelper? = null
    private var itemWidth: Int = 0
    private var itemHeight: Int = 0
    private var solarTextHeight: Float = 0.toFloat()
    private var currentPosition: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    internal fun init(config: CommSignCalendarView.Config) {
        this.mConfig = config
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = sp2px(0.6f)
        currentPosition = (currentYear - 1970) * 12 + currentMonth + 1
        isClickable = true
        if (config.isShowLunar) lunarHelper = LunarHelper()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        itemWidth = width / 7
        itemHeight = (height - mConfig!!.weekHeight as Int) / 6
        paint.textSize = mConfig!!.calendarTextSize
        solarTextHeight = getTextHeight()
        if (mConfig!!.signSize === 0f) mConfig!!.signSize = Math.min(itemHeight, itemWidth).toFloat()
    }

    fun selectDate(position: Int) {
        currentPosition = position - 1
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = Color.LTGRAY
        canvas.drawLine(0f, mConfig!!.weekHeight, 0f, height - mConfig!!.weekHeight, paint)
        //画日历顶部周的标题
        paint.color = mConfig!!.weekBackgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), mConfig!!.weekHeight, paint)
        paint.textSize = mConfig!!.weekTextSize
        paint.color = mConfig!!.weekTextColor
        var delay = getTextHeight() / 4
        for (i in 0..6) {
            canvas.drawText(weekTitles[i], itemWidth * (i + 0.5f), mConfig!!.weekHeight / 2 + delay, paint)
        }
        //画日历
        val year = 1970 + currentPosition / 12
        val month = currentPosition % 12
        calendar.set(year, month, 1)
        val firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val selectMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        delay = solarTextHeight / 4
        if (mConfig!!.isShowLunar) delay = 0f
        for (i in 1..selectMonthMaxDay) {
            val position = i - 1 + firstDay
            val x = position % 7 * itemWidth + itemWidth / 2
            val y = position / 7 * itemHeight + itemHeight / 2 + mConfig!!.weekHeight as Int + delay.toInt()
//签到背景
            var isSign = false
            if (signRecords != null) {
                date.set(year, month, i)
                val dateStr = format.format(date.time)
                if (signRecords!!.contains(dateStr)) {
                    paint.color = mConfig!!.signColor
                    canvas.drawCircle(x.toFloat(), y.toFloat(), mConfig!!.signSize / 2, paint)
                    isSign = true
                }
            }
            //农历
            if (mConfig!!.isShowLunar) {
                paint.color = if (isSign) mConfig!!.signTextColor else mConfig!!.lunarTextColor
                val lunar = lunarHelper!!.SolarToLunarString(year, month + 1, i)
                paint.textSize = mConfig!!.lunarTextSize
                canvas.drawText(lunar, x.toFloat(), y + solarTextHeight * 2 / 3, paint)
            }
            //阳历
            if (isSign) {
                paint.color = mConfig!!.signTextColor
            } else if (year == currentYear && month == currentMonth && i == currentDay) {//今天
                paint.color = mConfig!!.todayTextColor
            } else {//其他天
                paint.color = mConfig!!.calendarTextColor
            }
            paint.textSize = mConfig!!.calendarTextSize
            canvas.drawText(i.toString(), x.toFloat(), y.toFloat(), paint)
        }
    }

    fun setSignRecords(signRecords: HashSet<String>) {
        this.signRecords = signRecords
    }

    private fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    private fun getTextHeight(): Float {
        return paint.descent() - paint.ascent()
    }
}