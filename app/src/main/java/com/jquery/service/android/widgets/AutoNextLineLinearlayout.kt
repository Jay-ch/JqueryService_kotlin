package com.jquery.service.android.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import java.util.*

/**
 * created by caiQiang on 2019/5/17
 * e-mail:cq807077540@foxmail.com
 *
 * description: 自动换行LinearLayout
 */
class AutoNextLineLinearLayout  : LinearLayout {

     var mLeft: Int = 0
     var mRight:Int = 0
     var mTop:Int = 0
     var mBottom:Int = 0
     var map = Hashtable<View,Position>()


    constructor(context: Context, attrs: AttributeSet):  super(context, attrs)



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val mWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val mCount = childCount
        var mX = 0
        var mY = 0
        mLeft = 0
        mRight = 0
        mTop = 5
        mBottom = 0

        var j = 0

        val lastview: View? = null
        for (i in 0 until mCount) {
            val child = getChildAt(i)

            child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            // 此处增加onlayout中的换行判断，用于计算所需的高度
            val childw = child.measuredWidth
            val childh = child.measuredHeight
            mX += childw // 将每次子控件宽度进行统计叠加，如果大于设定的高度则需要换行，高度即Top坐标也需重新设置

            val position = Position()
            mLeft = getPosition(i - j, i)
            mRight = mLeft + child.measuredWidth
            if (mX >= mWidth) {
                mX = childw
                mY += childh
                j = i
                mLeft = 0
                mRight = mLeft + child.measuredWidth
                mTop = mY + 5
                // PS：如果发现高度还是有问题就得自己再细调了
            }
            mBottom = mTop + child.measuredHeight
            mY = mTop // 每次的高度必须记录 否则控件会叠加到一起
            position.left = mLeft
            position.top = mTop + 3
            position.right = mRight
            position.bottom = mBottom
            map.put(child, position)
        }
        setMeasuredDimension(mWidth, mBottom)
    }

    override fun generateDefaultLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(0, 0) // default of 1px spacing
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val pos = map.get(child) as Position
            if (pos != null) {
                child.layout(pos.left, pos.top, pos.right, pos.bottom)
            } else {
                Log.i("MyLayout", "error")
            }
        }
    }

    inner class Position {
        internal var left: Int = 0
        internal var top: Int = 0
        internal var right: Int = 0
        internal var bottom: Int = 0
    }

    private fun getPosition(IndexInRow: Int, childIndex: Int): Int {
        return if (IndexInRow > 0) {
            getPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).measuredWidth + 8
        } else paddingLeft
    }
}