package com.xp.sortrecyclerview

/**
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */

  class SortModel {
    private var name: String? = null
    private var letters: String? = null//显示拼音的首字母

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getLetters(): String? {
        return letters
    }

    fun setLetters(letters: String) {
        this.letters = letters
    }
}