package com.jquery.service.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import com.jquery.service.android.R
import com.jquery.service.android.entity.RecentWorkItem

/**
 * 时间列表适配器
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class TimeLineListCommAdapter : CommRecyclerAdapter<RecentWorkItem> {

    private var context: Context
    private var layoutPosition = -1//保存当前选中的position

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: RecentWorkItem, position: Int): Int {
        /* return if (showList) {
             R.layout.item_recent_work_fault_layout
         } else {
             R.layout.item_recent_work_sign_layout
         }*/

        when (item.display_type) {
            1 -> return R.layout.item_recent_work_fault_layout
            2 -> return R.layout.item_recent_work_sign_layout
            3 -> return R.layout.item_recent_work_fault_layout
        }
        return R.layout.item_recent_work_fault_layout

    }

    @SuppressLint("StringFormatInvalid")
    override fun onUpdate(helper: BaseAdapterHelper, item: RecentWorkItem, position: Int) {
        helper.setText(R.id.tv_list_comm, item.day)
        helper.setText(R.id.tv_title, item.title)
        helper.setText(R.id.tv_address, item.address)
        if (item.day.equals("今日")) {
            if (position != 0) {
                helper.setVisible(R.id.tv_list_comm, true)
                helper.setVisible(R.id.tv_list_comm_perch, true)
                // helper.setImageResource(R.id.iv_list_comm, R.drawable.round_orange_3_bg)
            }
        } else if (item.day.equals("昨天")) {
            helper.setText(R.id.tv_list_comm, item.day)
            if (position != 0) {
                helper.setVisible(R.id.tv_list_comm, true)
                helper.setVisible(R.id.tv_list_comm_perch, true)
                //  helper.setImageResource(R.id.iv_list_comm, R.drawable.round_orange_3_bg)
            }
        } else if (item.day.equals("前天")) {
            helper.setText(R.id.tv_list_comm, item.day)
            if (position != 0) {
                helper.setVisible(R.id.tv_list_comm, true)
                helper.setVisible(R.id.tv_list_comm_perch, true)
                // helper.setImageResource(R.id.iv_list_comm, R.drawable.round_orange_3_bg)
            }
        }
    }
}