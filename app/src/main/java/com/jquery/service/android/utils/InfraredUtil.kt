package com.jquery.service.android.utils


/**
 * created by caiQiang on 2019/4/10 0010.
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */
class InfraredUtil {

    /**
     * str16进制字符串
     */
     fun hex2bitString(str:String ): String {
        var result = ""
        // 转字符串
        for ( b  in str) {
            var bit = Integer.parseInt(b.toString(), 16).toString(2)
            // 补零
            while (bit.length < 4) {
                bit = '0' + bit
            }
            result += bit
        }
        return result
    }

    fun getIpString(byteReverse: ByteArray): String {
        var idOne = ""
        for (byt in byteReverse) {
            val byt1 = byt.toInt() and 0xff
            //TODO 严谨检查
            if (idOne == ""){
                idOne ="" +byt1
            }else{
                idOne = idOne + "." + byt1
            }
        }
        return idOne
    }
}