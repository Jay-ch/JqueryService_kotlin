package com.jquery.service.android.widgets.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.jquery.service.android.R
import com.jquery.service.android.listener.InputDialogListener
import com.jquery.service.android.utils.DisplayUtils
import kotlinx.android.synthetic.main.layout_edittext_dialog_select.*

/**
 * 自定义Dialog
 * @author J.query
 * @date 2019/3/27
 * @email j-query@foxmail.com
 */
class EditTextDialog (ctx: Context) : Dialog(ctx, R.style.SelectDialog), DialogInterface.OnCancelListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_edittext_dialog_select)
        super.onCreate(savedInstanceState)
        setOnCancelListener(this)
        setCanceledOnTouchOutside(false)
        initViews(true)
    }


    private fun initViews(b: Boolean) {
        if (b) {
            tv_input_dialog_right.visibility = View.GONE
        }

        tv_input_dialog_left.setOnClickListener {
            hideSoftKeyboard()
            listener.leftClick(getResultByteArray())
            dismiss()

        }
        tv_input_dialog_right.setOnClickListener {
            hideSoftKeyboard()
            listener.rightClick(getResultByteArray())
            dismiss()
        }

        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
            dialogWindow.attributes = lp
        }
    }

    fun hideSoftKeyboard() {

        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(et_input_dialog_edit_a.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        inputMethodManager.hideSoftInputFromWindow(et_input_dialog_edit_b.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        inputMethodManager.hideSoftInputFromWindow(et_input_dialog_edit_c.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        inputMethodManager.hideSoftInputFromWindow(et_input_dialog_edit_d.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun getResultByteArray(): Array<String> {
        val editA = et_input_dialog_edit_a.editableText.toString()
        val editB = et_input_dialog_edit_b.editableText.toString()
        val editC = et_input_dialog_edit_c.editableText.toString()
        val editD = et_input_dialog_edit_d.editableText.toString()
        return  arrayOf( editA,editB,editC,editD)
    }

    fun setTitle(s: String): EditTextDialog {
        if (tv_input_dialog_title != null) {
            tv_input_dialog_title.text = s
        }
        return this
    }

    fun setLeftAndRightText(left: String, right: String) {
        if (tv_input_dialog_left != null) {
            tv_input_dialog_left.visibility = View.VISIBLE
            tv_input_dialog_left.text =left
        }
        if (tv_input_dialog_right != null) {
            tv_input_dialog_right.visibility = View.VISIBLE
            tv_input_dialog_right.text =right
        }
    }

    override fun onCancel(dialog: DialogInterface?) {

    }

    fun showDialog() {
        if (!isShowing) {
            show()
        }
    }

    lateinit var listener: InputDialogListener

    fun setListener(listener: InputDialogListener): EditTextDialog {
        this.listener = listener
        return this
    }
}