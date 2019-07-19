package com.jquery.service.android.widgets.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils
import kotlinx.android.synthetic.main.layout_edittext_dialog.*





/**
 * created by caiQiang on 2019/4/22 0022.
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */
class CommonEditTextDialog (ctx: Context) : Dialog(ctx, R.style.SelectDialog), DialogInterface.OnCancelListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_edittext_dialog)
        setOnCancelListener(this)
        initViews(false)
    }

    private fun initViews(b: Boolean) {
        if (b) {
            tv_common_edit_dialog_right.visibility = View.GONE
        }

        tv_common_edit_dialog_left.setOnClickListener {


            listener.leftClick(getResult())
            hideSoftKeyboard()
            dismiss()
        }
        tv_common_edit_dialog_right.setOnClickListener {

            listener.rightClick(getResult())
            hideSoftKeyboard()
            dismiss()
        }

        val dialogWindow = this.window

        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
            dialogWindow.attributes = lp
        }
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(et_common_edit_dialog_title.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun getResult(): String {
        return et_common_edit_dialog_title.editableText.toString()
    }

    fun setTitle(s: String): CommonEditTextDialog {
        if (tv_common_edit_dialog_title != null) {
            tv_common_edit_dialog_title.text = s
        }
        return this
    }

    fun setLeftAndRightText(left: String, right: String) {
        if (tv_common_edit_dialog_left != null) {
            tv_common_edit_dialog_left.visibility = View.VISIBLE
            tv_common_edit_dialog_left.text = left
        }
        if (tv_common_edit_dialog_right != null) {
            tv_common_edit_dialog_right.visibility = View.VISIBLE
            tv_common_edit_dialog_right.text = right
        }
    }

    fun setEditTextHint(hintText: String) {
        et_common_edit_dialog_title.hint = hintText
    }

    fun setEditMaxLength(hintText: Int) {
        et_common_edit_dialog_title.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(hintText))
    }

    fun setInputType(inputType: Int) {
        et_common_edit_dialog_title.inputType = inputType
    }

    override fun onCancel(dialog: DialogInterface?) {
        hideSoftKeyboard()
        dismiss()
    }

    fun showDialog() {
        if (!isShowing) {
            show()
            et_common_edit_dialog_title.setText("")
        }
    }

    lateinit var listener: EditTextDialogListener

    fun setListener(listener: EditTextDialogListener): CommonEditTextDialog {
        this.listener = listener
        return this
    }

    interface EditTextDialogListener {

        fun leftClick(result: String)

        fun rightClick(result: String)
    }
}