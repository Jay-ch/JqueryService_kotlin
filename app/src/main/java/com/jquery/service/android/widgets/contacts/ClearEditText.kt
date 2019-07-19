package com.jquery.service.android.widgets.contacts

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import com.jquery.service.android.R

/**
 * 输入框清除
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
class ClearEditText : EditText, View.OnFocusChangeListener, TextWatcher {

    private var mClearDrawable: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = resources
                    .getDrawable(R.drawable.ic_search_clear)
        }
        mClearDrawable!!.setBounds(0, 0, mClearDrawable!!.intrinsicWidth, mClearDrawable!!.intrinsicHeight)
        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            if (event.action == MotionEvent.ACTION_UP) {
                val touchable = event.x > (width
                        - paddingRight - mClearDrawable!!.intrinsicWidth) && event.x < width - paddingRight
                if (touchable) {
                    this.setText("")
                }
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(text.length > 0)
        } else {
            setClearIconVisible(false)
        }
    }


    protected fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1], right, compoundDrawables[3])
    }


    override fun onTextChanged(s: CharSequence, start: Int, count: Int,
                               after: Int) {
        setClearIconVisible(s.length > 0)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }


    fun setShakeAnimation() {
        this.animation = shakeAnimation(5)
    }


    fun shakeAnimation(counts: Int): Animation {
        val translateAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
        translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
        translateAnimation.duration = 1000
        return translateAnimation
    }
}