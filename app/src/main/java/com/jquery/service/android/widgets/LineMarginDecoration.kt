package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
class LineMarginDecoration: RecyclerView.ItemDecoration {
    private val height: Int
    private var dividerPaint: Paint

    constructor(height: Int, context: Context) : super() {
        this.height = height
        dividerPaint = Paint()
        dividerPaint.color = context.resources.getColor(R.color.c_e5)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0, 0, 0, height)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + height).toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint)
        }
    }
}