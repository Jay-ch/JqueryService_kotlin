package com.jquery.service.android.widgets

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.jquery.service.android.R
import kotlinx.android.synthetic.main.include_title.view.*

/**
 * @author J.query
 * @date 2018/9/12
 * @email j-query@foxmail.com
 */
class TopTitleBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.include_title, this, true)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.Top_title)

        val showLeft = a.getBoolean(R.styleable.Top_title_showLeft, true)
        if (!showLeft) {
            tv_back.visibility = View.GONE
        }

        val rightStr = a.getString(R.styleable.Top_title_rightTitle)
        if (rightStr != null) {
            tv_right.text = rightStr
        }

        val title = a.getString(R.styleable.Top_title_title)
        if (title != null) {
            include_tv.text = title
        }

        val resourceId = a.getResourceId(R.styleable.Top_title_rightRes, -1)
        if (resourceId != -1) {
            img_right.visibility = View.VISIBLE
            img_right.setImageResource(resourceId)
        }

        a.recycle()
    }

    fun setLeftVisible(b: Boolean) {
        if (tv_back != null)
            tv_back.visibility = if (b) View.VISIBLE else View.GONE
    }

    fun setRightTvVisible(b: Boolean) {
        if (tv_right != null)
            tv_right.visibility = if (b) View.VISIBLE else View.GONE
    }

    fun setRightVisible(b: Boolean) {
        if (img_right != null)
            img_right.visibility = if (b) View.VISIBLE else View.GONE
    }

    fun setViewLineVisible(b: Boolean) {
        if (view_line != null)
            view_line.visibility = if (b) View.VISIBLE else View.GONE
    }


    fun setTitle(s: String) {
        if (include_tv != null)
            include_tv.text = s
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        include_tv.setTextColor(color)
    }

    fun setTitleBackground(@ColorInt color: Int) {
        fl_title.setBackgroundColor(color)
    }

    /**
     * 标题背景更改
     */
    fun setTitleBackgroundDrawableRes(@DrawableRes drawableRes: Int) {
        fl_title.setBackgroundDrawable(getResources().getDrawable(drawableRes))
    }

    /**
     * 返回键图标更改
     */
    fun setTvBackDrawableLeft(context: Context?, @DrawableRes drawableLeft: Int) {
        var drawable = context?.resources?.getDrawable(drawableLeft)
        drawable?.setBounds(0, 0, drawable?.getMinimumWidth(), drawable?.getMinimumHeight())
        tv_back.setCompoundDrawables(drawable, null, null, null)
        //隐藏Drawables
        // tv_back.setCompoundDrawables(null,null,null,null)
    }

}