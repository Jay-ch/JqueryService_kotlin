package com.jquery.service.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.entity.ListCommEntity

/**
 * 列表适配器
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class ListCommAdapter : CommRecyclerAdapter<ListCommEntity> {

    private var context: Context
    private var layoutPosition = -1//保存当前选中的position

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: ListCommEntity, position: Int): Int {
        return if (showList) {
            R.layout.item_list_comm_layout
        } else {
            R.layout.item_list_comm_layout
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun onUpdate(helper: BaseAdapterHelper, item: ListCommEntity, position: Int) {
        helper.setText(R.id.tv_list_comm, item.token)
        //helper.setText(R.id.tv_list_comm, context.getString(R.string.fault_condition, item.token))
        helper.setOnClickListener(R.id.ll_list_comm, View.OnClickListener {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position)
            itemClickListener.selectType(item.token)
            layoutPosition = position
            notifyDataSetChanged()
        })
        //更新单选之后的效果
        if (position == layoutPosition) {
            helper.setText(R.id.tv_list_comm, item.token)
            helper.setImageResource(R.id.iv_check, R.drawable.check_icon)
            helper.setVisible(R.id.iv_check, true)
        } else {
            helper.setImageResource(R.id.iv_check, R.drawable.bg_white_2_bg)
            helper.setVisible(R.id.iv_check, false)
            helper.setVisible(R.id.iv_check, true)
        }
    }
}