package com.jquery.service.android.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author J.query
 * @date 2019/4/11
 * @email j-query@foxmail.com
 */
class SpacesItemDecoration : RecyclerView.ItemDecoration {
    private val space: Int

    constructor(space: Int) : super() {
        this.space = space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        //注释这两行是为了上下间距相同
        //        if(parent.getChildAdapterPosition(view)==0){
        outRect.top = space
        //        }

    }
}