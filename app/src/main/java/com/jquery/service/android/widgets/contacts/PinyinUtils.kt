package com.xp.sortrecyclerview

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

/**
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
object PinyinUtils {
    /**
     * 获取拼音
     *
     * @param inputString
     * @return
     */
    fun getPingYin(inputString: String?): String {
        val format = HanyuPinyinOutputFormat()
        format.caseType = HanyuPinyinCaseType.LOWERCASE
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        format.vCharType = HanyuPinyinVCharType.WITH_V

        var input = inputString?.trim { it <= ' ' }?.toCharArray()
        var output = ""

        try {
            for (curChar in input!!) {
                if (Character.toString(curChar).matches("[\\u4E00-\\u9FA5]+".toRegex())) {
                    val temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format)
                    output += temp[0]
                } else
                    output += Character.toString(curChar)
            }
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            e.printStackTrace()
        }

        return output
    }

    /**
     * 获取第一个字的拼音首字母
     * @param chinese
     * @return
     */
    fun getFirstSpell(chinese: String?): String {
        val pinYinBF = StringBuffer()
        var arr = chinese?.toCharArray()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        for (curChar in arr!!) {
            if (curChar.toInt() > 128) {
                try {
                    val temp = PinyinHelper.toHanyuPinyinStringArray(curChar, defaultFormat)
                    if (temp != null) {
                        pinYinBF.append(temp[0][0])
                    }
                } catch (e: BadHanyuPinyinOutputFormatCombination) {
                    e.printStackTrace()
                }

            } else {
                pinYinBF.append(curChar)
            }
        }
        return pinYinBF.toString().replace("\\W".toRegex(), "").trim({ it <= ' ' })
    }
}