package com.jquery.service.android.utils

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author J.query
 * @date 2019/5/24
 * @email j-query@foxmail.com
 */
object DateUtils {

    private val DEFAULT_PATTERN = "yyyyMMddhhmmss"

    fun getSystemDate(): String {
        return formatDate(getSysDateTime(), DEFAULT_PATTERN)
    }

    fun formatDate(date: Date, pattern: String): String {
        val df = SimpleDateFormat(pattern)
        return df.format(date)
    }

    fun parseDate(dateStr: String, pattern: String): Date {
        val df = SimpleDateFormat(pattern)
        try {
            return df.parse(dateStr)
        } catch (e: ParseException) {
            throw RuntimeException("Error while parse date", e)
        }

    }

    fun getSysDateTime(): Date {
        return Date()
    }

    fun getSysDate(): Date? {
        return getDate(getSysDateTime())
    }

    fun getDate(date: Date?): Date? {
        if (date == null) return null
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun add(date: Date?, dateField: Int, mount: Int): Date? {
        if (date == null) return null
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(dateField, mount)

        return cal.time
    }

    fun getYear(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.YEAR)
    }

    fun getMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MONTH) + 1
    }

    fun getDayOfMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * Sunday=1
     * SATURDAY=7
     *
     * @param date
     * @return
     */
    fun getDayOfWeek(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getDayOfYear(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_YEAR)

    }

    fun getHour(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinute(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MINUTE)
    }

    fun getSecond(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.SECOND)
    }

    fun getMillSecond(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.MILLISECOND)
    }

    fun getTimestamp(date: Date): Timestamp {
        return Timestamp(date.time)
    }

    fun getSqlDate(date: Date): java.sql.Date {
        return java.sql.Date(date.time)
    }

    fun getDate(year: Int, month: Int, day: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun getDate(year: Int, month: Int, day: Int, hour: Int, min: Int, second: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, min)
        cal.set(Calendar.SECOND, second)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    fun getFirstDateOfMonth(year: Int, month: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun getLastDateOfMonth(year: Int, month: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.DAY_OF_MONTH, 1)//设置该月1号
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        cal.add(Calendar.DATE, -1)//使用add减一天

        return cal.time
    }


    fun getFirstDateOfYear(year: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun getLastDateOfYear(year: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR))
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun getPreviousDate(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1)
        return cal.time
    }

    fun getNextDate(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)
        return cal.time
    }

    fun getFirstDateOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return getFirstDateOfMonth(year, month)
    }

    fun getLastDateOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        return getLastDateOfMonth(year, month)
    }

    /**
     * 获得给定日期当周的WeekDay
     *
     * @param date
     * @param weekOfDay 星期一是1，星期天是7
     * @return
     */
    fun getWeekDate(date: Date, weekOfDay: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        if (weekOfDay < 7) {
            cal.set(Calendar.DAY_OF_WEEK, weekOfDay + 1)
        } else {
            cal.set(Calendar.DAY_OF_WEEK, weekOfDay)//星期六
            cal.add(Calendar.DATE, weekOfDay - 6)//往后推一天到周日
        }
        return cal.time
    }

    /**
     * 两个时间相差毫秒数
     *
     * @param start
     * @param end
     * @return
     */
    fun minusMilliseconds(start: Date, end: Date): Long {
        var start = start
        var end = end
        if (start.after(end)) {//交换
            val tmp = start
            start = end
            end = tmp
        }

        return end.time - start.time
    }

    fun minusSeconds(start: Date, end: Date): Long {

        return minusMilliseconds(start, end) / 1000
    }

    fun minusMinutes(start: Date, end: Date): Long {
        return minusMilliseconds(start, end) / (1000 * 60)
    }

    fun minusHours(start: Date, end: Date): Long {
        return minusMilliseconds(start, end) / (1000 * 60 * 60)
    }

    fun minusDay(start: Date, end: Date): Long {
        return minusMilliseconds(start, end) / (1000 * 60 * 60 * 24)
    }

    /****
     *   获取当前年份 月份,当月第一天
     */

    fun getMonthFirstday(): String {
        var cale: Calendar? = null
        // 获取当月第一天和最后一天
        val format = SimpleDateFormat("yyyy-MM-dd")
        val firstday: String
        // 获取前月的第一天
        cale = Calendar.getInstance()
        cale!!.add(Calendar.MONTH, 0)
        cale.set(Calendar.DAY_OF_MONTH, 1)
        firstday = format.format(cale.time)
        return firstday
    }

    /***
     *  获取最后一天
     */
    fun getMonthLastday(): String {
        var cale: Calendar? = null
        // 获取最后一天
        val format = SimpleDateFormat("yyyy-MM-dd")
        val firstday: String
        val lastday: String
        // 获取前月的最后一天
        cale = Calendar.getInstance()
        cale!!.add(Calendar.MONTH, 1)
        cale.set(Calendar.DAY_OF_MONTH, 0)
        lastday = format.format(cale.time)
        return lastday
    }
}