package com.jquery.service.android.entity

/**
 * 联系人javabean
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class ContactEntity : Comparable<ContactEntity> {

    private var name: String? = null
    private var id: Int = 0
    private var url: String? = null
    private var pinyin: String? = null
    private var firstChar: Char = ' '

    fun getPinyin(): String? {
        return pinyin
    }

    fun setPinyin(pinyin: String) {
        this.pinyin = pinyin
        val first = pinyin.substring(0, 1)
        if (first.matches("[A-Za-z]".toRegex())) {
            firstChar = first.toUpperCase()[0]
        } else {
            firstChar = '#'
        }
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getFirstChar(): Char {
        return firstChar
    }

    override fun compareTo(another: ContactEntity): Int {
        return this.pinyin!!.compareTo(another.getPinyin()!!)
    }

    override fun equals(o: Any?): Boolean {
        return if (o is ContactEntity) {
            this.id == (o as ContactEntity).getId()
        } else {
            super.equals(o)
        }
    }
}