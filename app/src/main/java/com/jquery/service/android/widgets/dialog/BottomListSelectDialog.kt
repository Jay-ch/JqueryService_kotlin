package com.jquery.service.android.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import com.jquery.service.android.R
import com.jquery.service.android.adapter.FaultStatisticAdapter
import com.jquery.service.android.listener.ListSelectDialogListener
import kotlinx.android.synthetic.main.layout_list_bottom_dialog_select.*


/**
 * created by caiQiang on 2019/5/20 0020.
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */
class BottomListSelectDialog : Dialog, ListSelectDialogListener {


    private  var ctx: Context
    private  var data: ArrayList<String>
    private  var currentType: String? = null
    private  var mSignAdapter: FaultStatisticAdapter? = null

    constructor(ctx: Context, data: ArrayList<String>,currentType: String?) : super(ctx, R.style.SelectDialog) {
        this.ctx = ctx
        this.data = data
        this.currentType = currentType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_list_bottom_dialog_select)
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        initViews()
    }

    private fun initViews() {

        tv_bottom_list_dialog_back.setOnClickListener {
            this.dismiss()
        }

//        val dialogWindow = this.window
//        if (dialogWindow != null) {
//            val lp = dialogWindow.attributes
//            lp.width = DisplayUtils.getScreenWidth(context) * 5 / 6
//            lp.height = DisplayUtils.getScreenHeight(context) * 1 / 4
//            dialogWindow.attributes = lp
//        }

        mSignAdapter = ctx.let { FaultStatisticAdapter(it) }
        rv_bottom_list_dialog.layoutManager = LinearLayoutManager(context)
        rv_bottom_list_dialog.adapter = mSignAdapter
        mSignAdapter?.addAll(data)
        mSignAdapter?.setItemClickListener(this)
    }

    override fun selectType(s: String) {

    }

    override fun onItemClick(position: Int) {
        listener.onUserSelected(data[position])
    }


    lateinit var listener: UserSelectListener

    fun setOnUserCancelListener(listener: UserSelectListener): BottomListSelectDialog {
        this.listener = listener
        return this
    }

    interface UserSelectListener {

        fun onUserSelected( data: String)
    }
}