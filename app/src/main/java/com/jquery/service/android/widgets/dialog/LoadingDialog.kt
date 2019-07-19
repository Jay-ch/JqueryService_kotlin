package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.TextView
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
open class LoadingDialog : Dialog {
    private var tvMessage: TextView?=null

    constructor(context: Context): super(context, R.style.SelectDialog) {
        this.setCanceledOnTouchOutside(false)
        val view = layoutInflater.inflate(R.layout.dialog_progress, null)
        tvMessage = view.findViewById<View>(R.id.tv_message) as TextView
        setContentView(view)
    }

    fun setMessage(message: String) {
        tvMessage?.text = message
        /*if (tvMessage != null)
            tvMessage?.text = message*/
    }

    internal lateinit var onDialogDismiss: OnDialogDismiss

    fun getOnDialogDismiss(): OnDialogDismiss {
        return onDialogDismiss
    }

    fun setOnDialogDismiss(onDialogDismiss: OnDialogDismiss) {
        this.onDialogDismiss = onDialogDismiss
    }

    interface OnDialogDismiss {
        fun onDismiss()
    }

}