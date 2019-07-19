package com.jquery.service.android.utils

import kotlin.experimental.and

/**
 * 基于Modbus CRC16的校验算法工具类
 * @author J.query
 * @date 2019/3/19
 * @email j-query@foxmail.com
 */
class Crc16Util {


    /**
     * 获取源数据和验证码的组合byte数组
     * @param strings 可变长度的十六进制字符串
     * @return
     */
    fun getData(vararg strings: String): ByteArray {
        var data = byteArrayOf()
        for (i in strings.indices) {
            val x = Integer.parseInt(strings[i], 16)
            val n = x.toByte()
            val buffer = ByteArray(data.size + 1)
            val aa = byteArrayOf(n)
            System.arraycopy(data, 0, buffer, 0, data.size)
            System.arraycopy(aa, 0, buffer, data.size, aa.size)
            data = buffer
        }
        return getData(data)
    }

    /**
     * 获取源数据和验证码的组合byte数组
     * @param aa 字节数组
     * @return
     */
    private fun getData(aa: ByteArray): ByteArray {
        val bb = getCrc16(aa)
        val cc = ByteArray(aa.size + bb.size)
        System.arraycopy(aa, 0, cc, 0, aa.size)
        System.arraycopy(bb, 0, cc, aa.size, bb.size)
        return cc
    }

    /**
     * 获取验证码byte数组，基于Modbus CRC16的校验算法
     */
    private fun getCrc16(arr_buff: ByteArray): ByteArray {
        val len = arr_buff.size

        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        var crc = 0xFFFF
        var i: Int
        var j: Int
        i = 0
        while (i < len) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = crc and 0xFF00 or (crc and 0x00FF xor ((arr_buff[i] and 0xFF.toByte()).toInt()))
            j = 0
            while (j < 8) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if (crc and 0x0001 > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc shr 1
                    crc = crc xor 0xA001
                } else
                // 如果移出位为 0,再次右移一位
                    crc = crc shr 1
                j++
            }
            i++
        }
        return intToBytes(crc)
    }

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    private fun intToBytes(value: Int): ByteArray {
        val src = ByteArray(2)
        src[1] = (value shr 8 and 0xFF).toByte()
        src[0] = (value and 0xFF).toByte()
        return src
    }

    /**
     * 将字节数组转换成十六进制字符串
     */
    fun byteTo16String(data: ByteArray): String {
        val buffer = StringBuffer()
        for (b in data) {
            buffer.append(byteTo16String(b))
        }
        return buffer.toString()
    }

    /**
     * 将字节转换成十六进制字符串
     * int转byte对照表
     * [128,255],0,[1,128)
     * [-128,-1],0,[1,128)
     */
    fun byteTo16String(b: Byte): String {
        val buffer = StringBuffer()
        val aa = b.toInt()
        if (aa < 0) {
            buffer.append(Integer.toString(aa + 256, 16) + " ")
        } else if (aa == 0) {
            buffer.append("00 ")
        } else if (aa > 0 && aa <= 15) {
            buffer.append("0" + Integer.toString(aa, 16) + " ")
        } else if (aa > 15) {
            buffer.append(Integer.toString(aa, 16) + " ")
        }
        return buffer.toString()
    }
}