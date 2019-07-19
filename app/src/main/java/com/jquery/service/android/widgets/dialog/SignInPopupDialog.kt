package com.kotlindemo.jquery.widgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.utils.DisplayUtils.dip2px
import com.jquery.service.android.utils.DisplayUtils.getScreenWidth


/**
 * @author qwkrom
 * @date 2018/9/30
 * @email j-query@foxmail.com
 */
class SignInPopupDialog : Dialog {

    internal lateinit var ctx: Context
    private var titleTv: TextView? = null
    private var rightTv: TextView? = null
    private var leftTv: TextView? = null
    private var line: View? = null
    private var closeTv: TextView? = null

    constructor(ctx: Context) : super(ctx) {
        this.ctx = ctx
    }

    constructor(ctx: Context, b: Boolean) : super(ctx, R.style.SelectDialog) {
        this.ctx = ctx
        initViews(b)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_popup_dialog_select)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(true)

    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    private fun initViews(b: Boolean) {
        titleTv = findViewById(R.id.tv_title)
        line = findViewById(R.id.line)
        closeTv = findViewById(R.id.tv_close)
        rightTv = findViewById(R.id.tv_right)
        leftTv = findViewById(R.id.tv_left)
        if (b) {
            line?.visibility = View.VISIBLE
            rightTv?.visibility = View.VISIBLE
            leftTv?.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
        }
        closeTv?.setOnClickListener { dismiss() }
        leftTv?.setOnClickListener {
            dismiss()
            if (listener != null)
                listener.leftClick()
        }
        rightTv?.setOnClickListener {
            dismiss()
            if (listener != null)
                listener.rightClick()
        }
        setWindowLayoutParam()
    }

    /**
     *  设置Activity不变暗
     */
    private fun setWindowransparentLayoutParam() {
        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = window.attributes
            lp.width = getScreenWidth(context) * 2 / 3
            lp.dimAmount = 0f
            lp.width = getScreenWidth(context) * 2 / 3
            lp.alpha = 0.6f
            window.attributes = lp
            window.setWindowAnimations(R.style.TopPopAnim)
        }
    }

    /**
     *  Activity变暗
     */
    private fun setWindowLayoutParam() {

        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = window.attributes
            lp.width = getScreenWidth(context) * 2 / 3
            lp.dimAmount = 0f
            lp.y = dip2px(ctx, 100f)
            //lp.y = DisplayUtils().getScreenWidth(context) * 1 / 3
            lp.alpha = 0.6f
            window.attributes = lp
            window.setWindowAnimations(R.style.TopPopAnim)
        }
    }

    fun setTitle(s: String): SignInPopupDialog {
        if (titleTv != null)
            titleTv?.text = s
        return this
    }

    fun setLeftAndRightButtonText(left: String, right: String) {
        if (leftTv != null) {
            leftTv?.text = left
        }
        if (rightTv != null) {
            rightTv?.text = right
        }
    }

    fun setLeftAndRightText(left: String, right: String) {
        line?.visibility = View.VISIBLE
        leftTv?.visibility = View.VISIBLE
        leftTv?.text = left
        rightTv?.visibility = View.VISIBLE
        rightTv?.text = right
    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    lateinit var listener: SelectDialogListener

    fun setListener(listener: SelectDialogListener): SignInPopupDialog {
        this.listener = listener
        return this
    }
}



