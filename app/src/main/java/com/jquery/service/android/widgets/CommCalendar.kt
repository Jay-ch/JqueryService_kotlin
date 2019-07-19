package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.*
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author J.query
 * @date 2019/5/7
 * @email j-query@foxmail.com
 */
class CommCalendar: View {
    private val weekTitles = arrayOf("日", "一", "二", "三", "四", "五", "六")
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    private val calendar = Calendar.getInstance()
    //今天所在的年月日信息
    private var currentYear: Int = 0
    private var currentMonth:Int = 0
    private var currentDay:Int = 0
    //点击选中的年月日信息
    private var clickYear: Int = 0
    private var clickMonth:Int = 0
    private var clickDay:Int = 0
    //当前选择的年月信息
    private var selectYear: Int = 0
    private var selectMonth:Int = 0
    private val date = GregorianCalendar()
    private var signRecords: HashMap<String, Boolean>? = null
    private var detectorCompat: GestureDetectorCompat? = null
    private var signSuccess: Bitmap? = null
    private var signError:Bitmap? = null
    private var mConfig: CommCalendarView.Config? = null
    private val paint = Paint()
    private var lunarHelper: LunarHelper? = null
    private var itemWidth: Int = 0
    private var itemHeight:Int = 0
    private var solarTextHeight: Float = 0.toFloat()
    private var currentPosition: Int = 0
    private var signDelay: Float = 0.toFloat()


    constructor(context: Context):  super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):  super(context, attrs, defStyleAttr)

    internal fun init(config: CommCalendarView.Config) {
        this.mConfig = config
        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        selectYear = currentYear
        selectMonth = currentMonth
        clickYear = currentYear
        clickMonth = currentMonth
        clickDay = currentDay
        detectorCompat = GestureDetectorCompat(context, gestureListener)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.strokeWidth = sp2px(0.6f)
        currentPosition = (currentYear - 1970) * 12 + currentMonth + 1
        isClickable = true
        if (config.isShowLunar) lunarHelper = LunarHelper()
        if (config.signIconSuccessId !== 0) {
            signSuccess = BitmapFactory.decodeResource(resources, config.signIconSuccessId)
            signError = BitmapFactory.decodeResource(resources, config.signIconErrorId)
            if (signSuccess != null) {
                val width = signSuccess!!.width
                val height = signSuccess!!.height
                val matrix = Matrix()
                matrix.postScale(config.signIconSize / width, config.signIconSize / height)
                signSuccess = Bitmap.createBitmap(signSuccess!!, 0, 0, width, height, matrix, true)
                signError = Bitmap.createBitmap(signError!!, 0, 0, width, height, matrix, true)
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
       // float a = 1.1L; //定义一个float型变量，变量名为a,值为1.1，L表示是浮点型
      //   Math.round(mConfig?.weekHeight!!)//采用round方式转换为整型


        itemWidth = width / 7
        itemHeight = (height - Math.round(mConfig?.weekHeight!!)) / 6
        paint.textSize = mConfig!!.calendarTextSize
        solarTextHeight = getTextHeight()
        signDelay = getX((Math.min(itemHeight, itemWidth) / 2).toFloat(), -45)
    }

    fun selectDate(position: Int) {
        currentPosition = position - 1
        selectYear = 1970 + currentPosition / 12
        selectMonth = currentPosition % 12
        invalidate()
    }

    fun initSelect(clickYear: Int, clickMonth: Int, clickDay: Int) {
        this.clickYear = clickYear
        this.clickMonth = clickMonth
        this.clickDay = clickDay
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
        //上一个月的最大天数
        calendar.add(Calendar.MONTH, -1)
        val previousMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        delay = solarTextHeight / 4
        if (mConfig!!.isShowLunar) delay = 0f
        for (i in 1..42) {
            val copyI = i - 1
            val x = copyI % 7 * itemWidth + itemWidth / 2

            val y = copyI / 7 * itemHeight + itemHeight / 2 +Math.round( mConfig?.weekHeight!!) + delay.toInt()
            if (i <= firstDay) {//前一月数据
                if (!mConfig!!.isShowOtherMonth) continue
                val day = previousMonthMaxDay - firstDay + i
                paint.color = mConfig!!.otherMonthTextColor
                paint.textSize = mConfig!!.calendarTextSize
                canvas.drawText(day.toString(), x.toFloat(), y.toFloat(), paint)
                drawLunar(canvas, if (month == 0) year - 1 else year, if (month == 0) 11 else month - 1, day, x, y)
            } else if (i > selectMonthMaxDay + firstDay) {//后一月数据
                if (!mConfig!!.isShowOtherMonth) continue
                val day = i - firstDay - selectMonthMaxDay
                paint.color = mConfig!!.otherMonthTextColor
                paint.textSize = mConfig!!.calendarTextSize
                canvas.drawText(day.toString(), x.toFloat(), y.toFloat(), paint)
                drawLunar(canvas, if (month == 11) year + 1 else year, if (month == 11) 0 else month + 1, day, x, y)
            } else {//当前月数据
                val day = i - firstDay
                if (year == currentYear && month == currentMonth && day == currentDay) {//今天
                    paint.color = mConfig!!.todayTextColor
                } else {//其他天
                    paint.color = mConfig!!.calendarTextColor
                }
                if (year == clickYear && month == clickMonth && day == clickDay) {//当前选中的一天
                    paint.color = mConfig!!.selectColor
                    canvas.drawCircle(x.toFloat(), y - delay, (Math.min(itemHeight, itemWidth) / 2).toFloat(), paint)
                    paint.color = mConfig!!.selectTextColor
                }
                paint.textSize = mConfig!!.calendarTextSize
                drawSign(canvas, year, month, day, x, y)
                canvas.drawText(day.toString(), x.toFloat(), y.toFloat(), paint)
                drawLunar(canvas, year, month, day, x, y)
            }
        }
    }


    private fun drawLunar(canvas: Canvas, year: Int, month: Int, day: Int, x: Int, y: Int) {
        if (mConfig!!.isShowLunar) {
            if (year != clickYear || month != clickMonth || day != clickDay) {
                paint.color = mConfig!!.lunarTextColor
            }
            val lunar = lunarHelper!!.SolarToLunarString(year, month + 1, day)
            paint.textSize = mConfig!!.lunarTextSize
            canvas.drawText(lunar, x.toFloat(), y + solarTextHeight * 2 / 3, paint)
        }
    }

    private fun drawSign(canvas: Canvas, year: Int, month: Int, day: Int, x: Int, y: Int) {
        if (signSuccess == null || signRecords == null) return
        date.set(year, month, day)
        val dateStr = format.format(date.time)
        if (signRecords?.containsKey(dateStr)!!) {
            if (year != clickYear || month != clickMonth || day != clickDay) {
                paint.color = mConfig!!.signTextColor
            }
            if (signRecords!![dateStr]!!) {
                canvas.drawBitmap(signSuccess, x + signDelay - mConfig!!.signIconSize / 2,
                        y - signDelay - mConfig!!.signIconSize / 2, paint)
            } else {
                canvas.drawBitmap(signError, x + signDelay - mConfig!!.signIconSize / 2,
                        y - signDelay - mConfig!!.signIconSize / 2, paint)
            }
        }
    }

    fun getCurrentDayInfo(): IntArray {
        return intArrayOf(currentYear, currentMonth, currentDay)
    }

    private fun getX(radius: Float, angle: Int): Float {
        val centerX = 0
        return (centerX + radius * Math.cos(angle * 3.14 / 180)).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detectorCompat!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val position = getPosition(e.x, e.y)
            if (dateClickListener != null) {
                calendar.set(selectYear, selectMonth, 1)
                val firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1
                calendar.set(selectYear, selectMonth, position - firstDay + 1)
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val week = calendar.get(Calendar.DAY_OF_WEEK) - 1
                if (month == selectMonth) {
                    clickYear = year
                    clickMonth = month
                    clickDay = day
                    invalidate()
                    dateClickListener!!.dateSelect(year, month, day, week)
                }
            }
            return super.onSingleTapUp(e)
        }
    }

    private fun getPosition(x: Float, y: Float): Int {
        var y = y
        y -= Math.round(mConfig?.weekHeight!!)
        val y1 = (y / itemHeight).toInt()
        val x1 = (x / itemWidth).toInt()
        return y1 * 7 + x1
    }

    fun setSignRecords(signRecords: HashMap<String, Boolean>) {
        this.signRecords = signRecords
    }

    private fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    private fun getTextHeight(): Float {
        return paint.descent() - paint.ascent()
    }

    internal interface DateSelectListener {
        fun dateSelect(year: Int, month: Int, day: Int, week: Int)
    }

    private var dateClickListener: DateSelectListener? = null

    internal fun setDateSelectListener(clickListener: DateSelectListener) {
        dateClickListener = clickListener
    }
}