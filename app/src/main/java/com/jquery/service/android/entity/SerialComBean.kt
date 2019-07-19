package com.jquery.service.android.entity

import java.text.SimpleDateFormat

/**
 * 串口数据
 * @author J.query
 * @date 2019/3/11
 * @email j-query@foxmail.com
 */
class SerialComBean {
    var bRec: ByteArray? = null
    var sRecTime = ""
    var sComPort = ""
    constructor(sPort: String, buffer: ByteArray, size: Int) {
        sComPort = sPort
        bRec = ByteArray(size)
        for (i in 0 until size) {
            bRec!![i] = buffer[i]
        }
        val sDateFormat = SimpleDateFormat("hh:mm:ss")
        sRecTime = sDateFormat.format(java.util.Date())
    }
}