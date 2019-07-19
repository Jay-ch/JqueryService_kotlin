package com.jquery.service.android.widgets

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
class ItemMarginDecoration : RecyclerView.ItemDecoration {
    private var margin = 0
    private var isShowTop = false

    constructor(paramInt: Float) : super() {
        this.margin = paramInt.toInt()
    }

    constructor(paramInt: Int, b: Boolean) : super() {
        this.margin = paramInt
        this.isShowTop = b
    }

    override fun getItemOffsets(paramRect: Rect, paramInt: Int, recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is GridLayoutManager) {
            if (paramInt < 2) {
                if (paramInt % 2 == 0) {
                    paramRect.set(margin * 2, margin * 2, margin, margin)
                } else {
                    paramRect.set(margin, margin * 2, margin * 2, margin)
                }
            } else {
                if (paramInt % 2 == 0) {
                    paramRect.set(margin * 2, margin, margin, margin)
                } else {
                    paramRect.set(margin, margin, margin * 2, margin)
                }
            }

        } else {
            if (paramInt == 0) {
                if (isShowTop) {
                    paramRect.set(0, margin * 2, 0, margin)
                } else {
                    paramRect.set(0, 0, 0, margin)
                }
            } else {
                paramRect.set(0, margin, 0, margin)
            }
        }
    }
}