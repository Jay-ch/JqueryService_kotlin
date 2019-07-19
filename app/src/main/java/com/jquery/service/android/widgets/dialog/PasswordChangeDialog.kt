package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.listener.EditDialogListener
import kotlinx.android.synthetic.main.layout_password_change_dialog_select.*

/**
 * 密码更改提示框
 * @author J.query
 * @date 2019/4/3
 * @email j-query@foxmail.com
 */
class PasswordChangeDialog : Dialog {
    internal var context: Context
    lateinit var listener: EditDialogListener
    var mEdtPassword: String? = null
    var mEdtAgainPassword: String? = null

    constructor(context: Context, b: Boolean) : super(context, R.style.NoTitleDialog) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_password_change_dialog_select)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    /**
     * 初始化
     */
    private fun initViews() {
        edt_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listener.firstTime(s.toString())
                mEdtPassword = s.toString()
            }
        })
        edt_again_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listener.secondTime(s.toString())
                mEdtAgainPassword = s.toString()
            }
        })
        initData()

    }

    fun initData() {
        mEdtPassword = edt_password.text.toString()
        mEdtAgainPassword = edt_again_password.text.toString()
        save.setOnClickListener() {
            if (listener != null) {
                if (mEdtPassword!!.length > 0 && mEdtAgainPassword!!.length > 0) {
                    if (mEdtPassword.equals(mEdtAgainPassword)) {
                        listener.firstTime(mEdtPassword!!)
                        listener.secondTime(mEdtAgainPassword!!)
                        listener.saveClick()
                        dismiss()
                    } else {
                        listener.firstTime(mEdtPassword!!)
                        listener.secondTime(mEdtAgainPassword!!)
                        listener.saveClick()
                    }
                }
            }

        }
    }

    /**
     * 设置提示内容
     */
    fun getEdtPassword(): String {
        mEdtPassword = edt_password.text.toString()
        return mEdtPassword.toString()
    }

    fun getEdtAgainPassword(): String {
        mEdtAgainPassword = edt_again_password.text.toString()
        return mEdtAgainPassword.toString()
    }

    /**
     * 设置提示内容
     */
    fun setHint(s: String) {
        close.setText(s)
    }


    /**
     * 设置按键值
     */
    fun setLeftAndRightText(lefts: String, rights: String) {
        line.setVisibility(View.VISIBLE)
        save.setText(lefts)
    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    fun setListener(listener: EditDialogListener): PasswordChangeDialog {
        this.listener = listener
        return this
    }
}