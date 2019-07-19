package com.jquery.service.android.utils

import java.util.*

/**
 * @author J.query
 * @date 2019/5/24
 * @email j-query@foxmail.com
 */
object TimesUtils {
    /**
     * 计算今年和去年的时间
     * @param year
     * @param month
     * @return
     */
   /* fun getTime(year: String, month: String): List<String> {
        val startTime: Date
        val endTime: Date
        val startTime3: Date
        val endTime3: Date
        var endY = Integer.parseInt(year)
        // 去年
        var endY1 = endY - 1
        var endM = Integer.parseInt(month)
        if (endM == 12) {
            endY++
            endY1++
            endM = 1
        } else {
            endM++
        }
        startTime = DateUtils.parseDate(year + "-" + month + "-01")
        endTime = DateUtils.parseDate(endY.toString() + "-" + endM + "-01")
        val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val startTime2 = df.format(startTime)
        val endTime2 = df.format(endTime)
        //计算去年的一个月
        startTime3 = DateUtils.parseDate((Integer.parseInt(year) - 1).toString() + "-" + month + "-01")
        endTime3 = DateUtils.parseDate(endY1.toString() + "-" + endM + "-01")
        val startTime4 = df.format(startTime3)
        val endTime4 = df.format(endTime3)
        val li = ArrayList<String>()
        li.add(startTime2)
        li.add(endTime2)
        li.add(startTime4)
        li.add(endTime4)
        return li
    }*/

    /**
     * 计算两个日期时间之间的间隔
     */
    fun getDatePoor(endDate: Date, nowDate: Date): String {

        val nd = (1000 * 24 * 60 * 60).toLong()
        val nh = (1000 * 60 * 60).toLong()
        val nm = (1000 * 60).toLong()
        val ns: Long = 1000
        // 获得两个时间的毫秒时间差异
        val diff = endDate.time - nowDate.time
        // 计算差多少天
        val day = diff / nd
        // 计算差多少小时
        val hour = diff % nd / nh
        // 计算差多少分钟
        val min = diff % nd % nh / nm
        // 计算差多少秒//输出结果
        val sec = diff % nd % nh % nm / ns
        return day.toString() + "天" + hour + "小时" + min + "分钟" + sec + "秒"
    }
}