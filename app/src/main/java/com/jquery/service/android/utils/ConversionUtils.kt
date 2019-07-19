package com.jquery.service.android.utils

/**
 * 数据转换工具
 * @author J.query
 * @date 2019/3/11
 * @email j-query@foxmail.com
 */
class ConversionUtils {
    /*判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数*/
    fun isOdd(num: Int): Int {
        return num and 0x1
    }

    /*Hex字符串转int*/
    fun HexToInt(inHex: String)
            : Int {
        return Integer.parseInt(inHex, 16)
    }

    /* Hex字符串转byteHex字符串转byte*/
    fun HexToByte(inHex: String)//
            : Byte {
        return Integer.parseInt(inHex, 16).toByte()
    }

    /*1字节转2个Hex字符*/
    fun Byte2Hex(inByte: Byte?)
            : String {
        return String.format("%02x", inByte).toUpperCase()
    }

    /*字节数组转转hex字符串*/
    fun ByteArrToHex(inBytArr: ByteArray)
            : String {
        val strBuilder = StringBuilder()
        val j = inBytArr.size
        for (i in 0 until j) {
            strBuilder.append(Byte2Hex(inBytArr[i]))
            strBuilder.append(" ")
        }
        return strBuilder.toString()
    }

    /*字节数组转转hex字符串，可选长度*/
    fun ByteArrToHex(inBytArr: ByteArray, offset: Int, byteCount: Int)
            : String {
        val strBuilder = StringBuilder()
        for (i in offset until byteCount) {
            strBuilder.append(Byte2Hex(inBytArr[i]))
        }
        return strBuilder.toString()
    }

    //-------------------------------------------------------
    /*转hex字符串转字节数组*/
    fun HexToByteArr(inHex: String)
            : ByteArray {
        var inHex = inHex
        var hexlen = inHex.length
        val result: ByteArray
        if (isOdd(hexlen) == 1) {//奇数
            hexlen++
            result = ByteArray(hexlen / 2)
            inHex = "0$inHex"
        } else {//偶数
            result = ByteArray(hexlen / 2)
        }
        var j = 0
        var i = 0
        while (i < hexlen) {
            result[j] = HexToByte(inHex.substring(i, i + 2))
            j++
            i += 2
        }
        return result
    }
}