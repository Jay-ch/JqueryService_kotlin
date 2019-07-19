package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jquery.service.android.R

/**
 * 自定义View实现绘制虚线
 * @author J.query
 * @date 2019/5/21
 * @email j-query@foxmail.com
 */
class DashLineView: View {
    private var mPaint: Paint
    private var mPath: Path

    constructor(context: Context, attrs: AttributeSet):  super(context, attrs) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = resources.getColor(R.color.dash_line)
        // 需要加上这句，否则画不出东西
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 3f
        mPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 5f), 0f)

        mPath = Path()
    }

    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2
        mPath.reset()
        mPath.moveTo(0f, centerY.toFloat())
        mPath.lineTo(width.toFloat(), centerY.toFloat())
        canvas.drawPath(mPath, mPaint)
    }

    // 其它方法还是不变
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 前四个参数没啥好讲的，就是起点和终点而已。
        // color数组的意思是从透明 -> 黑 -> 黑 -> 透明。
        // float数组与color数组对应：
        // 0 -> 0.3 (透明 -> 黑)
        // 0.3 - 0.7 (黑 -> 黑，即不变色)
        // 0.7 -> 1 (黑 -> 透明)
        mPaint.shader = LinearGradient(0f, 0f, width.toFloat(), 0f,
                intArrayOf(Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.TRANSPARENT),
                floatArrayOf(0f, 0.3f, 0.7f, 1f), Shader.TileMode.CLAMP)
    }
}