package com.xp.sortrecyclerview

/**
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
class PinyinComparator :Comparator<SortModel>{
    override fun compare(o1: SortModel, o2: SortModel): Int {
        return if (o1.getLetters() == "@" || o2.getLetters() == "#") {
            -1
        } else if (o1.getLetters() == "#" || o2.getLetters() == "@") {
            1
        } else {
            o1.getLetters()!!.compareTo(o2.getLetters()!!)
        }
    }
}