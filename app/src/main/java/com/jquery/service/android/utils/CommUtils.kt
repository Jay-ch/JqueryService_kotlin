package com.jquery.service.android.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import kotlin.experimental.and




/**
 * @author J.query
 * @date 2019/3/11
 * @email j-query@foxmail.com
 */
class CommUtils {
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */


    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */


    /**
     * 将char转换成16进制字符串
     */
    fun Char2String(b: Char): String {
        val hex: String
        if (b.toInt() shr 8 and 0xff == 0)
            hex = String.format("%1$02X ", b.toInt() and 0xff)
        else
            hex = String.format("%1$02X %2$02X ", b.toInt() and 0xff, b.toInt() shr 8 and 0xff)
        return hex
    }

    /**
     * 将char数组转换成16进制字符串
     *
     */
    fun CharsToString(b: CharArray): String {
        var ret = ""
        for (i in b.indices) {
            ret += Char2String(b[i])
        }
        return ret.toUpperCase()
    }

    /**
     * 将16进制字符串转换成一般的字符串
     *
     */
    fun HexString2String(str: String): String {

        val out = StringBuffer()
        val buffer = HexStringToBytes(str)
        try {
            out.append(String(buffer!!, 0, buffer.size, charset("GB2312")))
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return out.toString()
    }

    /**
     * byte数组转换成16进制字符串
     *
     */
    fun BytesToString(b: ByteArray, nlen: Int): String {
        var ret = ""
        for (i in 0 until nlen) {
            var hex = Integer.toHexString((b[i] and 0xFF.toByte()).toInt())
            if (hex.length == 1) {
                hex = "0$hex"
            }
            ret += hex.toUpperCase()
            ret += " "
        }
        return "$ret "
    }

    /**
     * byte数组转换成16进制字符串
     *
     */
    fun BytesToString(b: ByteArray): String {
        var ret = ""
        for (i in b.indices) {
            var hex = Integer.toHexString((b[i] and 0xFF.toByte()).toInt())
            if (hex.length == 1) {
                hex = "0$hex"
            }
            ret += hex.toUpperCase()
            ret += " "
        }
        return "$ret "
    }

    /**
     * 16进制字符串转换成byte数组
     *
     */
    fun HexStringToBytes(hexString: String?): ByteArray? {
        var hexString = hexString
        if (hexString == null || hexString == "") {
            return null
        }
        hexString = hexString.toUpperCase()
        val length = hexString.length / 3
        val hexChars = hexString.toCharArray()
        val d = ByteArray(length + 1)
        for (i in 0 until length) {
            val pos = i * 3
            d[i] = (charToByte(hexChars[pos]) shl 4 or charToByte(hexChars[pos + 1])).toByte()
        }
        d[d.size - 1] = (charToByte(hexChars[hexChars.size - 2]) shl 4 or charToByte(hexChars[hexChars.size - 1])).toByte()
        return d
    }

    /**
     * 字符串转换成byte数组
     *
     */
    fun stringToBytes(hexString: String?): ByteArray? {
        var hexString = hexString
        if (hexString == null || hexString == "") {
            return null
        }
        hexString = hexString.toUpperCase()
        val length = hexString.length / 2
        val hexChars = hexString.toCharArray()
        val d = ByteArray(length)
        for (i in 0 until length) {
            val pos = i * 2
            d[i] = (charToByte(hexChars[pos]) shl 4 or charToByte(hexChars[pos + 1])).toByte()
        }
        return d
    }

    private fun charToByte(c: Char): Int {
        return "0123456789ABCDEF".indexOf(c.toInt().toString())
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param className
     * 界面的类名
     * @return 是否在前台显示
     */
    fun isActivityTop(context: Context, className: String): Boolean {
        if (TextUtils.isEmpty(className))
            return false
        val am = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn.className)
                return true
        }
        return false
    }
}