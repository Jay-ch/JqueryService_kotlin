package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.jquery.service.android.R

/**
 * @author j.query
 * @date 2018/10/16
 * @email j-query@foxmail.com
 */

class ExitDialog : Dialog {

    internal var context: Context
    lateinit var yesTv: TextView
    lateinit var noTv: TextView

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_ask_exit)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        yesTv = findViewById(R.id.yes)
        noTv = findViewById(R.id.no)
        yesTv.setOnClickListener() {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        noTv.setOnClickListener() {
            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}