package com.jquery.service.android.adapter

import android.content.Context
import com.jquery.service.android.R

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class FaultConditionAdapter : CommRecyclerAdapter<String> {

    private var context: Context

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: String, position: Int): Int {
        return R.layout.item_fault_condition_layout

    }

    override fun onUpdate(helper: BaseAdapterHelper, item: String, position: Int) {
        helper.setText(R.id.tv_item_condition_name, item)
        helper.setText(R.id.tv_item_condition_num, "" + 10000)
        helper.setProgress(R.id.pb_item_fault_condition, 60, 100)
    }
}