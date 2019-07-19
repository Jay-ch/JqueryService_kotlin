package com.jquery.service.android.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * @author J.query
 * @date 2019/3/13
 * @email j-query@foxmail.com
 */
class MarqueeTextView : TextView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean {
        return true
    }
}