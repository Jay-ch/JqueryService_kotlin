package com.jquery.service.android.widgets

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jquery.service.android.R
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author J.query
 * @date 2019/5/17
 * @email j-query@foxmail.com
 */
class PickerView : View {
    private var mContext: Context? = null

    private var mPaint: Paint? = null
    private var mLightColor: Int = 0
    private var mDarkColor: Int = 0
    private var mHalfWidth: Float = 0.toFloat()
    private var mHalfHeight: Float = 0.toFloat()
    private var mQuarterHeight: Float = 0.toFloat()
    private var mMinTextSize: Float = 0.toFloat()
    private var mTextSizeRange: Float = 0.toFloat()
    private var mTextSpacing: Float = 0.toFloat()
    private var mHalfTextSpacing: Float = 0.toFloat()

    private var mScrollDistance: Float = 0.toFloat()
    private var mLastTouchY: Float = 0.toFloat()
    private var mDataList: MutableList<String> = ArrayList()
    private var mSelectedIndex: Int = 0
    private var mCanScroll = true
    private var mCanScrollLoop = true
    private var mOnSelectListener: OnSelectListener? = null
    private var mScrollAnim: ObjectAnimator? = null
    private var mCanShowAnim = true

    private var mTimer: Timer? = Timer()
    private var mTimerTask: TimerTask? = null
    private val mHandler = ScrollHandler(this)

    /**
     * 自动回滚到中间的速度
     */
    private val AUTO_SCROLL_SPEED = 10f

    /**
     * 透明度：最小 120，最大 255，极差 135
     */
    private val TEXT_ALPHA_MIN = 120
    private val TEXT_ALPHA_RANGE = 135

    /**
     * 选择结果回调接口
     */
    interface OnSelectListener {
        fun onSelect(view: View?, selected: String?)
    }

    private class ScrollTimerTask : TimerTask {
        private var mWeakHandler: WeakReference<Handler>? = null

        constructor(handler: Handler) : super() {
            mWeakHandler = WeakReference(handler)
        }
        /* init {
             mWeakHandler = WeakReference(handler)
         }*/

        override fun run() {
            val handler = mWeakHandler?.get() ?: return

            handler.sendEmptyMessage(0)
        }
    }

    private class ScrollHandler : Handler {
        private var mWeakView: WeakReference<PickerView>

        constructor(view: PickerView) : super() {
            mWeakView = WeakReference(view)
        }

        override fun handleMessage(msg: Message) {
            val view = mWeakView.get() ?: return

            view.keepScrolling()
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.style = Paint.Style.FILL
        mPaint?.textAlign = Paint.Align.CENTER
        mLightColor = ContextCompat.getColor(mContext!!, R.color.c_ff42)
        mDarkColor = ContextCompat.getColor(mContext!!, R.color.date_picker_text_dark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mHalfWidth = measuredWidth / 2f
        val height = measuredHeight
        mHalfHeight = height / 2f
        mQuarterHeight = height / 4f
        val maxTextSize = height / 7f
        mMinTextSize = maxTextSize / 2.2f
        mTextSizeRange = maxTextSize - mMinTextSize
        mTextSpacing = mMinTextSize * 2.8f
        mHalfTextSpacing = mTextSpacing / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mSelectedIndex >= mDataList.size) return

        // 绘制选中的 text
        drawText(canvas, mLightColor, mScrollDistance, mDataList[mSelectedIndex])

        // 绘制选中上方的 text
        for (i in 1..mSelectedIndex) {
            drawText(canvas, mDarkColor, mScrollDistance - i * mTextSpacing,
                    mDataList[mSelectedIndex - i])
        }

        // 绘制选中下方的 text
        val size = mDataList.size - mSelectedIndex
        for (i in 1 until size) {
            drawText(canvas, mDarkColor, mScrollDistance + i * mTextSpacing,
                    mDataList[mSelectedIndex + i])
        }
    }

    private fun drawText(canvas: Canvas, textColor: Int, offsetY: Float, text: String) {
        if (TextUtils.isEmpty(text)) return

        var scale = 1 - Math.pow((offsetY / mQuarterHeight).toDouble(), 2.0).toFloat()
        scale = if (scale < 0) 0F else scale
        mPaint?.textSize = mMinTextSize + mTextSizeRange * scale
        mPaint?.color = textColor
        mPaint?.alpha = TEXT_ALPHA_MIN + (TEXT_ALPHA_RANGE * scale).toInt()

        // text 居中绘制，mHalfHeight + offsetY 是 text 的中心坐标
        val fm = mPaint?.fontMetrics
        val baseline = mHalfHeight + offsetY - (fm?.top!! + fm?.bottom!!) / 2f
        canvas.drawText(text, mHalfWidth, baseline, mPaint!!)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return mCanScroll && super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                cancelTimerTask()
                mLastTouchY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetY = event.y
                mScrollDistance += offsetY - mLastTouchY
                if (mScrollDistance > mHalfTextSpacing) {
                    if (!mCanScrollLoop) {
                        if (mSelectedIndex == 0) {
                            mLastTouchY = offsetY
                            invalidate()
                            //break
                        } else {
                            mSelectedIndex--
                        }
                    } else {
                        // 往下滑超过离开距离，将末尾元素移到首位
                        moveTailToHead()
                    }
                    mScrollDistance -= mTextSpacing
                } else if (mScrollDistance < -mHalfTextSpacing) {
                    if (!mCanScrollLoop) {
                        if (mSelectedIndex == mDataList.size - 1) {
                            mLastTouchY = offsetY
                            invalidate()
                            // break
                        } else {
                            mSelectedIndex++
                        }
                    } else {
                        // 往上滑超过离开距离，将首位元素移到末尾
                        moveHeadToTail()
                    }
                    mScrollDistance += mTextSpacing
                }
                mLastTouchY = offsetY
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                // 抬起手后 mSelectedIndex 由当前位置滚动到中间选中位置
                if (Math.abs(mScrollDistance) < 0.01) {
                    mScrollDistance = 0f
                    //break
                }
                cancelTimerTask()
                mTimerTask = ScrollTimerTask(mHandler)
                mTimer?.schedule(mTimerTask, 0, 10)
            }
        }
        return true
    }

    private fun cancelTimerTask() {
        if (mTimerTask != null) {
            mTimerTask?.cancel()
            mTimerTask = null
        }
        if (mTimer != null) {
            mTimer?.purge()
        }
    }

    private fun moveTailToHead() {
        if (!mCanScrollLoop || mDataList.isEmpty()) return

        val tail = mDataList[mDataList.size - 1]
        mDataList.removeAt(mDataList.size - 1)
        mDataList.add(0, tail)
    }

    private fun moveHeadToTail() {
        if (!mCanScrollLoop || mDataList.isEmpty()) return

        val head = mDataList[0]
        mDataList.removeAt(0)
        mDataList.add(head)
    }

    private fun keepScrolling() {
        if (Math.abs(mScrollDistance) < AUTO_SCROLL_SPEED) {
            mScrollDistance = 0f
            if (mTimerTask != null) {
                cancelTimerTask()

                if (mOnSelectListener != null && mSelectedIndex < mDataList.size) {
                    mOnSelectListener?.onSelect(this, mDataList[mSelectedIndex])
                }
            }
        } else if (mScrollDistance > 0) {
            // 向下滚动
            mScrollDistance -= AUTO_SCROLL_SPEED
        } else {
            // 向上滚动
            mScrollDistance += AUTO_SCROLL_SPEED
        }
        invalidate()
    }

    /**
     * 设置数据
     */
    fun setDataList(list: MutableList<String>?) {
        if (list == null || list.isEmpty()) return

        mDataList = list
        // 重置 mSelectedIndex，防止溢出
        mSelectedIndex = 0
        invalidate()
    }

    /**
     * 选择选中项
     */
    fun setSelected(index: Int) {
        if (index >= mDataList.size) return

        mSelectedIndex = index
        if (mCanScrollLoop) {
            // 可循环滚动时，mSelectedIndex 值固定为 mDataList / 2
            val position = mDataList.size / 2 - mSelectedIndex
            if (position < 0) {
                for (i in 0 until -position) {
                    moveHeadToTail()
                    mSelectedIndex--
                }
            } else if (position > 0) {
                for (i in 0 until position) {
                    moveTailToHead()
                    mSelectedIndex++
                }
            }
        }
        invalidate()
    }

    /**
     * 设置选择结果监听
     */
    fun setOnSelectListener(listener: OnSelectListener) {
        mOnSelectListener = listener
    }

    /**
     * 是否允许滚动
     */
    fun setCanScroll(canScroll: Boolean) {
        mCanScroll = canScroll
    }

    /**
     * 是否允许循环滚动
     */
    fun setCanScrollLoop(canLoop: Boolean) {
        mCanScrollLoop = canLoop
    }

    /**
     * 执行滚动动画
     */
    fun startAnim() {
        if (!mCanShowAnim) return

        if (mScrollAnim == null) {
            val alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f)
            val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f)
            mScrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, alpha, scaleX, scaleY).setDuration(200)
        }

        if (!mScrollAnim?.isRunning!!) {
            mScrollAnim?.start()
        }
    }

    /**
     * 是否允许滚动动画
     */
    fun setCanShowAnim(canShowAnim: Boolean) {
        mCanShowAnim = canShowAnim
    }

    /**
     * 销毁资源
     */
    fun onDestroy() {
        mOnSelectListener = null
        mHandler.removeCallbacksAndMessages(null)
        if (mScrollAnim != null && mScrollAnim?.isRunning!!) {
            mScrollAnim?.cancel()
        }
        cancelTimerTask()
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }
    }
}