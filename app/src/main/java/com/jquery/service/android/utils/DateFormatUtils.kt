package com.jquery.service.android.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间选择器日期格式化
 * @author J.query
 * @date 2019/5/17
 * @email j-query@foxmail.com
 */
object DateFormatUtils {
    private var DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd"
    private var DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm"

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    fun long2Str(timestamp: Long, isPreciseTime: Boolean): String {
        return long2Str(timestamp, getFormatPattern(isPreciseTime))
    }

    private fun long2Str(timestamp: Long, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.CHINA).format(Date(timestamp))
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含时分
     * @return 时间戳
     */
    fun str2Long(dateStr: String, isPreciseTime: Boolean): Long {
        return str2Long(dateStr, getFormatPattern(isPreciseTime))
    }

    private fun str2Long(dateStr: String, pattern: String): Long {
        try {
            return SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).time
        } catch (ignored: Throwable) {
        }

        return 0
    }

    private fun getFormatPattern(showSpecificTime: Boolean): String {
        return if (showSpecificTime) {
            DATE_FORMAT_PATTERN_YMD_HM
        } else {
            DATE_FORMAT_PATTERN_YMD
        }
    }

    /**
     * 时间戳转HH:mm
     */
    fun getTimeString(timestamp: Long): String {
        var timeString = DateFormatUtils.long2Str(timestamp, true).toString().substring(10, 16)
        return timeString
    }

    /**
     * 时间戳转HH:mm
     */
    fun getDayTimeString(times: Long): String {
        var timeString = DateFormatUtils.long2Str(times, true).toString().substring(0, 10)
        return timeString
    }

    /**
     * 获取当前时间
     */
    fun getDaytime(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN_YMD_HM)
        var timeString = dateFormat.format(date)
        println(dateFormat.format(date))
        return timeString
    }
}