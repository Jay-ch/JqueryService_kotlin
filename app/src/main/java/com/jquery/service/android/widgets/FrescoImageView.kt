package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import com.facebook.drawee.view.SimpleDraweeView

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class FrescoImageView : SimpleDraweeView {
    private var showFilter = false

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (showFilter)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val drawable = drawable
                    drawable?.mutate()?.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    val drawableUp = drawable
                    drawableUp?.mutate()?.clearColorFilter()
                }
            }
        return super.onTouchEvent(event)
    }

    fun setShowFilter(showFilter: Boolean) {
        this.showFilter = showFilter
    }
}