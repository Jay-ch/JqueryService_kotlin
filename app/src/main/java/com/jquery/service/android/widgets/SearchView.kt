package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.EditText
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class SearchView: EditText {

    private var searchSize:Float = 0f
    private var mTextSize:Float = 0f
    private var mTextColor:Int = -0x1000000
    private var mDrawable: Drawable? = null
    private var paint: Paint? = null

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        InitResource(context, attrs)
        InitPaint()
    }

    private fun InitResource(context: Context, attrs: AttributeSet) {
        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.searchedit)
        val density = context.resources.displayMetrics.density
        searchSize = mTypedArray.getDimension(R.styleable.searchedit_imagewidth, 18 * density + 0.5f)
        mTextColor = mTypedArray.getColor(R.styleable.searchedit_textColor, -0x7b7b7c)
        mTextSize = mTypedArray.getDimension(R.styleable.searchedit_textSize, 14 * density + 0.5f)
        mTypedArray.recycle()
    }

    private fun InitPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.setColor(mTextColor)
        paint?.setTextSize(mTextSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        DrawSearchIcon(canvas)
    }

    private fun DrawSearchIcon(canvas: Canvas?) {
        if (this.text.toString().length == 0) {
            val textWidth = paint?.measureText("输入关键词进行检索")
            val textHeight = getFontLeading(this!!.paint!!)
            val dx = (width.toFloat() - searchSize - textWidth!! - 8f) / 2
            val dy = (height - searchSize) / 2
            canvas?.save()
            canvas?.translate(scrollX + dx, scrollY + dy)
            if (mDrawable != null) {
                mDrawable?.draw(canvas)
            }
            canvas?.drawText("输入关键词进行检索", scrollX.toFloat() + searchSize + 8f, scrollY + (height - (height - textHeight) / 2) - paint?.getFontMetrics()?.bottom!! - dy, paint)
            canvas?.restore()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mDrawable == null) {
            try {
                mDrawable = context.resources.getDrawable(R.drawable.search)
                mDrawable?.setBounds(0, 0, searchSize.toInt(), searchSize.toInt())
            } catch (e: Exception) {
            }

        }
    }

    override fun onDetachedFromWindow() {
        if (mDrawable != null) {
            mDrawable?.setCallback(null)
            mDrawable = null
        }
        super.onDetachedFromWindow()
    }

    fun getFontLeading(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.bottom - fm.top
    }
}