package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @author J.query
 * @date 2019/5/16
 * @email j-query@foxmail.com
 */
class CircleProgressView : View {
    /**
     * 最大进度
     */
    private val mMaxProgress = 100

    /**
     * 当前进度
     */
    private var mProgress = 0


    /**
     * 圆形进度宽度
     */
    private val mCircleLineStrokeWidth = 60

    /**
     * 画笔
     */
    private var mPaint: Paint ?= null

    /**
     * 灰度进度颜色
     */
    private val mBackgroundColor = -0x192f

    /**
     * 当前颜色数组
     */
    private var mCurrentColors = intArrayOf()


    /**
     * (简约) 0-99 颜色数组
     */
    private val mMinColors_simple = intArrayOf(-0x58e00, -0x4fc1)

    /**
     * (简约) 100% 时颜色数组
     */
    private val mFullColors_simple = intArrayOf(-0x8e00, -0x8e00)

    /**
     * 构造函数
     */
    constructor(context: Context, attrs: AttributeSet):  super(context, attrs) {
        mPaint = Paint()

        // 设置画笔相关属性
        mPaint?.isAntiAlias = true
        mPaint?.strokeWidth = mCircleLineStrokeWidth.toFloat()
        mPaint?.style = Paint.Style.STROKE
        mPaint?.isDither = true // 防抖动

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var width = this.width
        var height = this.height

        if (width != height) {
            val min = Math.min(width, height)
            width = min
            height = min
        }
        val cX = width / 2
        val cY = height / 2

        // 圆弧半径
        val radius = cX - mCircleLineStrokeWidth / 2

        mPaint?.shader = null // 清除上一次的shader
        mPaint?.style = Paint.Style.STROKE // 设置绘制的圆为空心
        mPaint?.color = mBackgroundColor
        canvas.drawCircle(cX.toFloat(), cX.toFloat(), radius.toFloat(), mPaint) // 画底部的空心圆

        val mRectF = RectF((canvas.width / 2 - radius).toFloat(),
                (canvas.height / 2 - radius).toFloat(), (canvas.width - (canvas.width / 2 - radius)).toFloat(), (canvas.height - (canvas.height / 2 - radius)).toFloat())

        //圆形倒角
        //        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mCurrentColors = getCurrentColors()

        // 线性颜色
        //        LinearGradient mLinearGradient = new LinearGradient(cX - radius, cY, // 渐变区域,左到右
        //                cX + radius, cY, mCurrentColors, null, Shader.TileMode.CLAMP);
        //        SweepGradient mSweepGradient = new SweepGradient(cX,cY,mCurrentColors[0],mCurrentColors[1]);
        val mSweepGradient = SweepGradient(cX.toFloat(), cX.toFloat(), mCurrentColors, null)
        val matrix = Matrix()
        matrix.setRotate(-90f, cX.toFloat(), cX.toFloat())
        mSweepGradient.setLocalMatrix(matrix)

        // 把渐变设置到笔刷
        mPaint?.shader = mSweepGradient

        val sweepAngle = mProgress.toFloat() / mMaxProgress * 360
        canvas.drawArc(mRectF, -90f, sweepAngle, false, mPaint)

    }

    private fun getCurrentColors(): IntArray {
        val colors: IntArray

        if (mProgress < 100) {
            colors = mMinColors_simple
        } else {
            colors = mFullColors_simple
        }

        return colors
    }

    /**
     * 设置进度
     */
    fun setProgress(progress: Int) {
        this.mProgress = progress
        this.invalidate()
    }
}