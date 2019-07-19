package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import java.util.*

/**
 * 垂直滚动的文本框(垂直的跑马灯效果)
 * @author J.query
 * @date 2019/5/6
 * @email j-query@foxmail.com
 */
class VerticalScrollTextView : TextSwitcher, ViewSwitcher.ViewFactory {

    private val FLAG_START_AUTO_SCROLL = 0
    private val FLAG_STOP_AUTO_SCROLL = 1
    private val FLAG_START_FIRST_SCROLL = 2//第一次滚动不用间隔,后续滚动有间隔

    private var mTextSize = 15f
    private var mPadding = 5
    private var textColor = Color.BLACK
    private var maxLines = 1//默认最多一行

    /**
     * @param textSize  字号
     * @param padding   内边距
     * @param textColor 字体颜色
     */
    fun setTextStyle(textSize: Float, padding: Int, textColor: Int) {
        mTextSize = textSize
        mPadding = padding
        this.textColor = textColor
    }

    /**
     * @param maxLines 最大行数
     */
    fun setMaxLines(maxLines: Int) {
        this.maxLines = maxLines
    }

    private var itemClickListener: OnItemClickListener? = null
    private var mContext: Context
    private var currentId = -1
    private var textList: ArrayList<String>
    private var mHandler: Handler? = null

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        textList = ArrayList()
    }

    /**
     * 渐进渐出时间间隔
     *
     * @param animDuration
     */
    fun setAnimTime(animDuration: Long) {
        setFactory(this)
        val `in` = TranslateAnimation(0f, 0f, animDuration.toFloat(), 0f)
        `in`.duration = animDuration
        `in`.interpolator = AccelerateInterpolator()
        val out = TranslateAnimation(0f, 0f, 0f, (-animDuration).toFloat())
        out.duration = animDuration
        out.interpolator = AccelerateInterpolator()
        inAnimation = `in`
        outAnimation = out
    }

    /**
     * 设置文本
     *
     * @param time
     */
    fun setTextStillTime(time: Long) {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    FLAG_START_AUTO_SCROLL -> {
                        if (textList.size > 0) {
                            currentId++
                            setText(textList[currentId % textList.size])
                        }
                        mHandler?.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, time)
                    }
                    FLAG_STOP_AUTO_SCROLL -> mHandler?.removeMessages(FLAG_START_AUTO_SCROLL)
                    FLAG_START_FIRST_SCROLL -> mHandler?.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, 0)
                }
            }
        }
    }

    /**
     * 设置数据源
     *
     * @param titles
     */
    fun setTextList(titles: List<String>) {
        textList.clear()
        textList.addAll(titles)
        currentId = -1
    }

    /**
     * 开始滚动
     */
    fun startAutoScroll() {
        mHandler?.sendEmptyMessage(FLAG_START_FIRST_SCROLL)
    }

    /**
     * 停止滚动
     */
    fun stopAutoScroll() {
        mHandler?.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL)
    }

    override fun makeView(): View {
        val t = TextView(mContext)
        t.gravity = Gravity.LEFT
        t.maxLines = maxLines
        t.setPadding(mPadding, mPadding, mPadding, mPadding)
        t.setTextColor(textColor)
        t.textSize = mTextSize
        t.isClickable = true
        t.setOnClickListener {
            if (itemClickListener != null && textList.size > 0 && currentId != -1) {
                itemClickListener!!.onItemClick(currentId % textList.size)
            }
        }
        return t
    }

    /**
     * 设置点击事件监听
     *
     * @param itemClickListener
     */
    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /**
     * 轮播文本点击监听器
     */
    interface OnItemClickListener {
        /**
         * 点击回调
         *
         * @param position 当前点击ID
         */
        fun onItemClick(position: Int)
    }
}