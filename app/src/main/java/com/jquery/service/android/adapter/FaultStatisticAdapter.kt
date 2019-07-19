package com.jquery.service.android.adapter

import android.content.Context
import com.jquery.service.android.R

/**
 * created by caiQiang on 2019/5/13
 * e-mail:cq807077540@foxmail.com
 *
 * description:
 */
class FaultStatisticAdapter : CommRecyclerAdapter<String> {

    private var context: Context

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: String, position: Int): Int {
        return R.layout.item_list_text_select

    }

    override fun onUpdate(helper: BaseAdapterHelper, item: String, position: Int) {
        helper.setText(R.id.tv_item_condition_name, item)
        helper.setText(R.id.tv_item_condition_num, "" + 10000)
        helper.setProgress(R.id.pb_item_fault_condition,60,100)
    }
}