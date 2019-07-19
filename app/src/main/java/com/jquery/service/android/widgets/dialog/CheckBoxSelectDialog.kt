package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils
import com.jquery.service.android.utils.ToastUtil
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_dialog_checkbox_select.*

/**
 * created by caiQiang on 2019/3/22 0022.
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */

class CheckBoxSelectDialog : Dialog {

    private  var ctx: Context

    private var showRight: Boolean = false

    /**
     * @param context
     * @param b       是否隐藏right btn
     */
    constructor(ctx: Context, b: Boolean) : super(ctx, R.style.SelectDialog) {
        this.ctx = ctx
        this.showRight = b
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_dialog_checkbox_select)
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(showRight)
    }

    private fun initViews(b: Boolean) {
        if (!b) {
            btn_csd_right.visibility = View.GONE
        }

        btn_csd_left.setOnClickListener {
            dismiss()
            rb_csd_prepay.isChecked = false
            rb_csd_postpaid.isChecked = false
            listener.cancel()
        }
        if (b){
            btn_csd_right.setOnClickListener {

                when {
                    rb_csd_prepay.isChecked -> {
                        listener.sure(1,rb_csd_prepay.text)
                        dismiss()
                        rb_csd_prepay.isChecked = false
                    }
                    rb_csd_postpaid.isChecked -> {
                        listener.sure(2,rb_csd_postpaid.text)
                        rb_csd_postpaid.isChecked = false
                        dismiss()
                    }
                    else -> {
                        ToastUtil.show("")
                        Toasty.info(ctx, "请选择计费性质").show()
                    }
                }

            }
        }

        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
            dialogWindow.attributes = lp
        }

        rg_csd.setOnCheckedChangeListener { _, _ ->
            btn_csd_right.isEnabled = rb_csd_prepay.isChecked || rb_csd_postpaid.isChecked
        }
    }

    fun setTitle(s: String): CheckBoxSelectDialog {
        if (tv_csd_title != null) {
            tv_csd_title.text = s
        }
        return this
    }

    fun setLeftAndRightText(left: String, right: String) {
        btn_csd_left.text = left
        btn_csd_left.text = left
        if (showRight){
            btn_csd_right.text = right
        }
    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    lateinit var listener: CBSelectDialogListener

    interface CBSelectDialogListener {

        fun cancel()

        fun sure(checkIndex: Int, checkText: CharSequence)
    }

    fun setListener(listener: CBSelectDialogListener): CheckBoxSelectDialog {
        this.listener = listener
        return this
    }
}