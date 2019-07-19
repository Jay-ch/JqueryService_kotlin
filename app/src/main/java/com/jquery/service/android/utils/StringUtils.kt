package com.jquery.service.android.utils

import java.text.DecimalFormat

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
object StringUtils {
    fun getHostName(urlString: String): String {
        var urlString = urlString
        var head = ""
        var index = urlString.indexOf("://")
        if (index != -1) {
            head = urlString.substring(0, index + 3)
            urlString = urlString.substring(index + 3)
        }
        index = urlString.indexOf("/")
        if (index != -1) {
            urlString = urlString.substring(0, index + 1)
        }
        return head + urlString
    }

    fun getDataSize(var0: Long): String {
        val var2 = DecimalFormat("###.00")
        return if (var0 < 1024L)
            (var0).toString() + "bytes"
        else
            (if (var0 < 1048576L)
                var2.format((var0.toFloat() / 1024.0f).toDouble()) + "KB"
            else
                (if (var0 < 1073741824L)
                    var2.format((var0.toFloat() / 1024.0f / 1024.0f).toDouble()) + "MB"
                else
                    if (var0 < 0L)
                        var2.format((var0.toFloat() / 1024.0f / 1024.0f / 1024.0f).toDouble()) + "GB"
                    else
                        "error"))
    }
}