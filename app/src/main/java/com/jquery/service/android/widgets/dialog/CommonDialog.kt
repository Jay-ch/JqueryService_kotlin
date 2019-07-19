package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener

/**
 * 通用dialog
 * @author j.query
 * @date 2018/10/16
 * @email j-query@foxmail.com
 */
class CommonDialog : Dialog {
    internal var context: Context
    private var tv_right: TextView? = null
    private var tv_left: TextView? = null
    private var tv_close: TextView? = null
    private var line: View? = null
    private var tv_title: TextView? = null
    lateinit var listener: SelectDialogListener


    constructor(context: Context, b: Boolean) : super(context, R.style.NoTitleDialog) {
        this.context = context
        initViews(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_comm_dialog_select)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews(true)
    }

    private fun initViews(b: Boolean) {
        line = findViewById(R.id.line)
        tv_title = findViewById(R.id.tv_title)
        tv_right = findViewById(R.id.tv_right)
        tv_left = findViewById(R.id.tv_left)
        tv_close = findViewById(R.id.tv_close)
        tv_left?.setOnClickListener() {
            //Toast.makeText(context, context.getString(R.string.sure), Toast.LENGTH_SHORT).show()
            dismiss()
        }
        tv_right?.setOnClickListener {
            //Toast.makeText(context, context.getString(R.string.cancel), Toast.LENGTH_SHORT).show()
            dismiss()
        }
        tv_close?.setOnClickListener {
            dismiss()
        }
        tv_title?.setText("")

    }

    fun setTitle(s: String) {
        tv_title?.setText(s)
    }

    /**
     * 设置按键值
     */
    fun setLeftText(lefts: String) {
        line?.visibility = View.GONE
        tv_right?.visibility = View.GONE
        tv_left?.setText(lefts)

    }

    /**
     * 设置按键值
     */
    fun setLeftAndRightText(lefts: String, rights: String) {

        tv_left?.setText(lefts)
        tv_right?.setText(rights)
    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    fun setListener(listener: SelectDialogListener): CommonDialog {
        this.listener = listener
        return this
    }
}