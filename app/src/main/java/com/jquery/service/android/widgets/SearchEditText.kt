package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class SearchEditText : EditText, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    private val TAG = "SearchEditText"
    /**
     * 图标是否默认在左边
     */
    private var isIconLeft = false
    /**
     * 是否点击软键盘搜索
     */
    private var pressSearch = false
    /**
     * 软键盘搜索键监听
     */
    private var listener: OnSearchClickListener? = null

    private var drawables: Array<Drawable>? = null // 控件的图片资源
    private var drawableLeft: Drawable? = null
    private var drawableDel: Drawable? = null
    private var drawable: Drawable? = null // 搜索图标和删除按钮图标
    private var eventX: Int = 0
    private var eventY: Int = 0 // 记录点击坐标
    private var rect: Rect? = null // 控件区域

    fun setOnSearchClickListener(listener: OnSearchClickListener) {
        this.listener = listener
    }

    interface OnSearchClickListener {
        fun onSearchClick(view: View, keyword: String)
    }

    constructor(context: Context) : this(context, null) {

        init()
    }


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle) {

        initParams(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initParams(context, attrs)
        init()
    }

    private fun initParams(context: Context?, attrs: AttributeSet?) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.SearchEditText)
        if (typedArray != null) {
            drawable = typedArray.getDrawable(R.styleable.SearchEditText_drawableDel)
            typedArray.recycle()
        }
    }

    private fun init() {
        onFocusChangeListener = this
        setOnKeyListener(this)
        addTextChangedListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        if (isIconLeft) { // 如果是默认样式，直接绘制
            if (length() < 1) {
                drawableDel = null
            }
            this.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableDel, null)
            super.onDraw(canvas)
        } else { // 如果不是默认样式，需要将图标绘制在中间
            if (drawables == null) drawables = compoundDrawables
            if (drawableLeft == null) drawableLeft = drawables?.get(0)
            val textWidth = paint.measureText(hint.toString())
            val drawablePadding = compoundDrawablePadding
            val drawableWidth = drawableLeft?.getIntrinsicWidth()
            val bodyWidth = textWidth + drawableWidth?.toFloat()!! + drawablePadding.toFloat()
            canvas.translate((width.toFloat() - bodyWidth - paddingLeft.toFloat() - paddingRight.toFloat()) / 2, 0f)
            super.onDraw(canvas)
        }
    }


    override fun onFocusChange(v: View, hasFocus: Boolean) {
        // 被点击时，恢复默认样式
        if (!pressSearch && TextUtils.isEmpty(text.toString())) {
            isIconLeft = hasFocus
        }
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        pressSearch = keyCode == KeyEvent.KEYCODE_ENTER
        if (pressSearch && listener != null && event.action == KeyEvent.ACTION_DOWN) {
            /*隐藏软键盘*/
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
            }
            listener?.onSearchClick(v, text.toString())
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 清空edit内容
        if (drawableDel != null && event.action == MotionEvent.ACTION_UP) {
            eventX = event.rawX.toInt()
            eventY = event.rawY.toInt()
            //            Log.i(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            if (rect == null) rect = Rect()
            getGlobalVisibleRect(rect)
            rect?.left = rect?.right?.minus(drawableDel?.getIntrinsicWidth()!!)
            if (rect?.contains(eventX, eventY)!!) {
                setText("")
            }
        }
        return super.onTouchEvent(event)
    }


    override fun afterTextChanged(arg0: Editable) {
        if (this.length() < 1) {
            drawableDel = null
        } else {
            drawableDel = drawable
        }
    }


    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int,
                                   arg3: Int) {
    }

    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int,
                               arg3: Int) {
    }
}