package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.jquery.service.android.R

/**
 * 右侧的字母索引View
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class SideBar:View {
    //触摸事件
    private var onTouchingLetterChangedListener: OnTouchingLetterChangedListener? = null

    // 26个字母
    var b = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")
    //选中
    private var choose = -1

    private val paint = Paint()

    private var mTextDialog: TextView? = null

    /**
     * 为SideBar显示字母的TextView
     *
     * @param mTextDialog
     */
    fun setTextView(mTextDialog: TextView) {
        this.mTextDialog = mTextDialog
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):  super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet):super(context, attrs)

    constructor(context: Context): super(context)

    /**
     *
     * 重写的onDraw的方法
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val height = height//获取对应的高度
        val width = width//获取对应的宽度
        val singleHeight = height / b.size//获取每一个字母的高度
        for (i in b.indices) {
            paint.color = Color.rgb(0, 0, 0)  // 所有字母的默认颜色 目前为黑
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = 20f  //默认字体的大小
            //选中的状态
            if (i == choose) {
                paint.color = Color.parseColor("#FFFFFF") //选中字母的颜色 目前为白
                paint.isFakeBoldText = true//设置是否为粗体文字
            }
            //x坐标等于=中间-字符串宽度的一般
            val xPos = width / 2 - paint.measureText(b[i]) / 2
            val yPos = (singleHeight * i + singleHeight).toFloat()
            canvas.drawText(b[i], xPos, yPos, paint)
            paint.reset()//重置画笔
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val action = event.action
        val y = event.y//点击y坐标
        val oldChoose = choose

        val listener = onTouchingLetterChangedListener

        val c = (y / height * b.size).toInt()//点击y坐标所占高度的比例*b数组的长度就等于点击b中的个数

        when (action) {
            MotionEvent.ACTION_UP -> {
                setBackgroundDrawable(ColorDrawable(0x00000000))//设置背景颜色
                choose = -1
                invalidate()
                if (mTextDialog != null) {
                    mTextDialog!!.visibility = View.INVISIBLE
                }
            }

            else -> {
                setBackgroundResource(R.drawable.rp_sidebar_background) // 点击字母条的背景颜色
                if (oldChoose != c) {
                    if (c >= 0 && c < b.size) {
                        listener?.onTouchingLetterChanged(b[c])
                        if (mTextDialog != null) {
                            mTextDialog!!.text = b[c]
                            mTextDialog!!.visibility = View.VISIBLE
                        }
                        choose = c
                        invalidate()
                    }
                }
            }
        }




        return true
    }

    /**
     * 向外松开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    fun setOnTouchingLetterChangedListener(
            onTouchingLetterChangedListener: OnTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener
    }

    /**
     *
     * 接口
     *
     * @author
     */
    interface OnTouchingLetterChangedListener {
        fun onTouchingLetterChanged(s: String)
    }
}