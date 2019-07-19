package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils

/**
 * 通用dialog
 * @author j.query
 * @date 2018/10/16
 * @email j-query@foxmail.com
 */
class ForgetDialog : Dialog {
    internal var context: Context
    private var tv_right: TextView? = null
    private var tv_left: TextView? = null
    private var tv_close: TextView? = null

    private var mTVTitle: TextView? = null


    constructor(context: Context, b: Boolean) : super(context,R.style.NoTitleDialog) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_forget_dialog)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(true)
    }

    private fun initViews(b: Boolean) {
        mTVTitle = findViewById(R.id.tv_title)
        tv_right = findViewById(R.id.tv_right)
        tv_left = findViewById(R.id.tv_left)
        tv_close = findViewById(R.id.tv_close)
        tv_left?.setOnClickListener() {
            Toast.makeText(context, context.getString(R.string.sure), Toast.LENGTH_SHORT).show()
            dismiss()
        }
        tv_right?.setOnClickListener {
            Toast.makeText(context, context.getString(R.string.cancel), Toast.LENGTH_SHORT).show()
            dismiss()
        }
        tv_close?.setOnClickListener {
            dismiss()
        }
        mTVTitle?.setText("忘记密码请联系相关管理人员!")
        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
            lp.height = DisplayUtils.getScreenHeight(context) * 1 / 4
            dialogWindow.attributes = lp
        }
    }

    fun setTitle(s: String): ForgetDialog {
        //tv_title.setText(s)
        if (mTVTitle != null) {
            mTVTitle?.text = s
        } else {
            mTVTitle?.text = s
        }
        return this
    }
}