package com.jquery.service.android.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

/**
 * 解决ScrollView嵌套ViewPager 不显示ViewPager问题及
 * @author J.query
 * @date 2019/5/16
 * @email j-query@foxmail.com
 */
class AutoHeightViewPager : ViewPager {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // find the current child view
        // and you must cache all the child view
        // use setOffscreenPageLimit(adapter.getCount())
        val view = getChildAt(currentItem)
        view?.measure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, measureHeight(heightMeasureSpec, view))
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureHeight(measureSpec: Int, view: View?): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.measuredHeight
            }
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    /**
     * 单独测量view获取尺寸
     *
     * @param view
     */
    fun measeureView(view: View) {

        val intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        // 重新测量view
        view.measure(intw, inth)

        // 以上3句可简写成下面一句
        //view.measure(0,0);

        // 获取测量后的view尺寸
        val intwidth = view.measuredWidth
        val intheight = view.measuredHeight
    }
}