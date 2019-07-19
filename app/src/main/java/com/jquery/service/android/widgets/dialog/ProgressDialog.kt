package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils
import kotlinx.android.synthetic.main.dialog_update_progress.*

/**
 * created by caiQiang on 2019/3/22 0022.
 * e-mail:cq807077540@foxmail.com
 *
 * description: 进度对话框
 */

class ProgressDialog(context: Context) : Dialog(context, R.style.SelectDialog), View.OnClickListener, DialogInterface.OnCancelListener {

    private val TAG = "ProgressDialog"
    /**
     * 取消按钮
     */
    private val cancelButton: Button

    init {
        setContentView(R.layout.dialog_update_progress)
        cancelButton = findViewById(R.id.btn_update_cancel)
        cancelButton.setOnClickListener(this)
        setOnCancelListener(this)
        setCanceledOnTouchOutside(false)
        val dialogWindow = this.window
        if (dialogWindow != null) {
            val lp = dialogWindow.attributes
            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
            dialogWindow.attributes = lp
        }
    }

    /**
     * 设置进度
     */
    fun setProgress(progress: Int) {
        pb_dialog_progress.progress = progress
        tv_dialog_progress.text = "" + progress + "%"
    }

    /**
     * 显示对话框
     */
    fun showDialog(title: String, progress: Int) {
        tv_remount_update_title.text = title
        pb_remount_update.visibility = View.GONE
        ll_update_dialog_progress.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE
        cancelButton.text = context.getString(R.string.cancel)
        setProgress(progress)
        this.show()
    }

    /**
     * 显示等待设备返回结果
     */
    fun showWaitDialog() {
        tv_remount_update_title.text = context.getString(R.string.update_wait_response)
        pb_remount_update.visibility = View.VISIBLE
        ll_update_dialog_progress.visibility = View.GONE
        cancelButton.visibility = View.GONE
    }

    /**
     * 升级失败
     */
    fun showUpdateFail(responseBody: String) {
        pb_remount_update.visibility = View.GONE
        tv_remount_update_title.text = context.getString(R.string.update_fail) + ":" + responseBody
        cancelButton.visibility = View.VISIBLE
        cancelButton.text = context.getString(R.string.update_sure)
    }

    fun disMissDialog() {
        dismiss()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_update_cancel -> {
                listener.onUserCancel()
            }
        }
        dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        setOnCancelListener(null)
        listener.onUserCancel()
        dismiss()
    }

    lateinit var listener: UserCancelListener

    fun setOnUserCancelListener(listener: UserCancelListener): ProgressDialog {
        this.listener = listener
        return this
    }

    interface UserCancelListener {

        fun onUserCancel()
    }
}