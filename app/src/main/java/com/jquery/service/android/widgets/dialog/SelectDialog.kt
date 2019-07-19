package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.utils.DisplayUtils.getScreenWidth
import kotlinx.android.synthetic.main.layout_dialog_select.*

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
class SelectDialog : Dialog {

    private  var ctx: Context?=null

    constructor(ctx: Context) : super(ctx)

    private var showRight: Boolean = false

    /**
     * @param context
     * @param b       是否隐藏right btn
     */
    constructor(ctx: Context?, b: Boolean) : super(ctx, R.style.SelectDialog) {
        this.ctx = ctx
        this.showRight = b

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_dialog_select)
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(showRight)
    }

    private fun initViews(b: Boolean) {
        if (!b) {
            tv_right.visibility = View.GONE
//            tv_left.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
        }

        tv_left.setOnClickListener {
            dismiss()
                listener.leftClick()
        }
        if (b){
            tv_right.setOnClickListener {
                dismiss()
                listener.rightClick()
            }
        }

        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = getScreenWidth(context) * 5 / 6
            dialogWindow.attributes = lp
        }
    }

    fun setTitle(s: String): SelectDialog {
        if (tv_title != null) {
            tv_title.text = s
        }
        return this
    }

    fun setLeftAndRightButtonText(left: String, right: String) {
        if (tv_left != null) {
            tv_left.text = left
        }
        if (showRight){
          tv_right.text = right
        }


    }

    fun setLeftAndRightText(left: String, right: String) {
        tv_left.text = left
        if (showRight){
            tv_right.text = right
        }
    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    lateinit var listener: SelectDialogListener

    fun setListener(listener: SelectDialogListener): SelectDialog {
        this.listener = listener
        return this
    }
}
