package com.jquery.service.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.entity.SignRecordListEntity
import com.jquery.service.android.ui.home.view.FaultDetailsActivity

/**
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class SignRecordListAdapter : CommRecyclerAdapter<SignRecordListEntity> {

    private var context: Context
    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: SignRecordListEntity, position: Int): Int {
        return if (showList) {
            R.layout.item_sign_record_list_layout
        } else {
            R.layout.item_sign_record_list_layout
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun onUpdate(helper: BaseAdapterHelper, item: SignRecordListEntity, position: Int) {
        helper.setText(R.id.tv_sign_record_date, item.date)
        helper.setText(R.id.tv_sign_record_status, context.getString(R.string.normal, item.status))
        helper.setText(R.id.tv_sign_record_time, item.time)
        helper.setOnClickListener(R.id.ll_tv_sign_record, View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("good_id", item.date)
            val intent = Intent(context, FaultDetailsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        })
    }
}