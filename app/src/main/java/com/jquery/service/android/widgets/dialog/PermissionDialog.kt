package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import kotlinx.android.synthetic.main.layout_permission_dialog_select.*

/**
 * 自定义Dialog
 * @author J.query
 * @date 2019/3/27
 * @email j-query@foxmail.com
 */
class PermissionDialog : Dialog {
    internal var context: Context
    lateinit var listener: SelectDialogListener


    constructor(context: Context, b: Boolean) : super(context, R.style.NoTitleDialog) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_permission_dialog_select)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    /**
     * 初始化
     */
    private fun initViews() {
        left.setOnClickListener() {
            dismiss()
        }
        right.setOnClickListener {
            dismiss()
        }
        close.setOnClickListener {
            dismiss()
        }
        title.setText("")
    }

    /**
     * 设置提示内容
     */
    fun setHint(s: String) {
        close.setText(s)
    }

    /**
     * 设置提示内容
     */
    fun setTitle(s: String) {
        title.setText(s)
    }

    /**
     * 设置按键值
     */
    fun setLeftAndRightText(lefts: String, rights: String) {
        line.setVisibility(View.VISIBLE)
        title.setVisibility(View.VISIBLE)
        left.setText(lefts)
        right.setVisibility(View.VISIBLE)
        right.setText(rights)

    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    fun setListener(listener: SelectDialogListener): PermissionDialog {
        this.listener = listener
        return this
    }
}