package com.jquery.service.android.entity

/**
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class PinyinComparator : Comparator<FriendEntity> {

    private var instance: PinyinComparator? = null

    companion object {
        fun getInstance(): PinyinComparator {
            var instance = PinyinComparator()
            if (instance == null) {
                 instance = PinyinComparator()
            }
            return instance
        }
    }

    override fun compare(o1: FriendEntity, o2: FriendEntity): Int {
        return if (o1.letters.equals("@") || o2.letters.equals("#")) {
            -1
        } else if (o1.letters.equals("#") || o2.letters.equals("@")) {
            1
        } else {
            o1.letters.compareTo(o2.letters)
        }
    }
}