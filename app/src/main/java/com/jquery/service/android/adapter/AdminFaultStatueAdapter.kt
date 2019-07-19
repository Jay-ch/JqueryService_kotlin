package com.jquery.service.android.adapter

import android.content.Context
import com.jquery.service.android.R

/**
 * created by caiQiang on 2019/5/13
 * e-mail:cq807077540@foxmail.com
 *
 * description: 管理员 管理统计界面 故障情况
 */
class AdminFaultStatueAdapter : CommRecyclerAdapter<String> {

    private var context: Context

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: String, position: Int): Int {
        return R.layout.item_admin_fault_statue_layout
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: String, position: Int) {
        helper.setText(R.id.tv_item_epe_fault_name, item)
    }
}