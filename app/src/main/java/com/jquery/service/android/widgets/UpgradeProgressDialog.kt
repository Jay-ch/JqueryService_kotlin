package com.jquery.service.android.widgets

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils
import kotlinx.android.synthetic.main.dialog_upgrade_progress.*

/**
 * 自定义升级Dialog
 * @author J.query
 * @date 2019/3/27
 * @email j-query@foxmail.com
 */

class UpgradeProgressDialog : Dialog, View.OnClickListener, DialogInterface.OnCancelListener {

    private val TAG = "UpgradeProgressDialog"
    internal var context: Context
    private var btn_update_sure: Button? = null
    private var btn_update_cancel: Button? = null
    private var tv_remount_update_title: TextView? = null
    private var pb_remount_update: ProgressBar? = null
    private var ll_update_dialog_progress: LinearLayout? = null
    private var tv_dialog_progress: TextView? = null
    private var pb_dialog_progress: ProgressBar? = null

    constructor(context: Context) : super(context, R.style.SelectDialog) {
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_upgrade_progress)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    private fun initViews() {
        tv_dialog_progress = findViewById(R.id.tv_dialog_progress)
        pb_dialog_progress = findViewById(R.id.pb_dialog_progress)
        tv_remount_update_title = findViewById(R.id.tv_remount_update_title)
        pb_remount_update = findViewById(R.id.pb_remount_update)
        ll_update_dialog_progress = findViewById(R.id.ll_update_dialog_progress)
        btn_update_cancel = findViewById(R.id.btn_update_cancel)
        btn_update_sure = findViewById(R.id.btn_update_sure)
        btn_update_sure?.setOnClickListener(this)
        tv_remount_update_title?.setOnClickListener(this)
        btn_update_cancel?.setOnClickListener(this)
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
    fun setProgress(progress: Int?) {
        pb_dialog_progress?.progress = progress!!
        tv_dialog_progress?.text = "" + progress + "%"
    }

    /**
     * 显示对话框
     */
    fun showDialog(title: String?, progress: Int?) {
        tv_remount_update_title?.text = title
        pb_remount_update?.visibility = View.GONE
        ll_update_dialog_progress?.visibility = View.VISIBLE
        btn_update_cancel?.visibility = View.VISIBLE
        btn_update_cancel?.text = context.getString(R.string.cancel)
        setProgress(progress)
        this.show()
    }

    /**
     * 显示对话框
     */
    fun showDialogTag(tag: String?) {
        tv_upgrade_info.setText(tag)
    }

    /**
     * 显示对话框
     */
    fun showDialogUpdateVer(tag: String?) {
        tv_remount_update_ver.setText(tag)
    }


    /**
     * 显示等待设备返回结果
     */
    fun showWaitDialog() {
        tv_remount_update_title?.text = context.getString(R.string.update_wait_response)
        pb_remount_update?.visibility = View.VISIBLE
        ll_update_dialog_progress?.visibility = View.GONE
        btn_update_cancel?.visibility = View.GONE
    }

    /**
     * 升级失败
     */
    fun showUpdateFail(responseBody: String) {
        pb_remount_update?.visibility = View.GONE
        tv_remount_update_title?.text = context.getString(R.string.update_fail) + ":" + responseBody
        btn_update_cancel?.visibility = View.VISIBLE
        btn_update_cancel?.text = context.getString(R.string.update_sure)
    }

    fun disMissDialog() {
        dismiss()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_update_cancel -> {
                listener.onCancel()

                dismiss()
            }
            R.id.btn_update_sure -> {
                btn_update_cancel?.visibility = View.GONE
                btn_update_sure?.visibility = View.GONE
                listener.onSure()
            }
            R.id.tv_remount_update_title -> {
                listener.onCancel()
                dismiss()
            }
        }

    }

    override fun onCancel(dialog: DialogInterface) {
        setOnCancelListener(null)
        listener.onCancel()
        dismiss()
    }

    lateinit var listener: CancelListener

    fun setOnUserCancelListener(listener: CancelListener): UpgradeProgressDialog {
        this.listener = listener
        return this
    }

    interface CancelListener {

        fun onSure()

        fun onCancel()
    }
}