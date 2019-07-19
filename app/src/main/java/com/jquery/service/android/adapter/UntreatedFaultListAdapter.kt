package com.jquery.service.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jquery.service.android.R
import com.jquery.service.android.entity.UntreatedFaultListEntity
import com.jquery.service.android.ui.home.view.FaultDetailsActivity

/**
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class UntreatedFaultListAdapter : CommRecyclerAdapter<UntreatedFaultListEntity> {

    private var context: Context
    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: UntreatedFaultListEntity, position: Int): Int {
        return if (showList) {
            R.layout.item_untreated_fault_list_layout
        } else {
            R.layout.item_untreated_fault_layout
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun onUpdate(helper: BaseAdapterHelper, item: UntreatedFaultListEntity, position: Int) {
       /* if (item.photo != null) {
            helper.setImageUri(R.id.tv_handled_fault_one, item.photo.thumb)
        } else {
            helper.setImageUri(R.id.tv_handled_fault_one, null!!)
        }*/

        /*   val shopTv = helper.getView<TextView>(R.id.tv_shop_price)
           shopTv!!.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
           shopTv.paint.isAntiAlias = true
           shopTv.setText(String.valueOf(item.price))

           helper.setText(R.id.tv_right_title, item.name)
           helper.setText(R.id.tv_sale_count, context.getString(R.string.total_sale_count, item.virtual_sales))
           if (item.suppliers_name != null) {
               helper.setText(R.id.tv_property, item.suppliers_name)
           } else {
               helper.setText(R.id.tv_property, "")
           }

           val priceTv = helper.getView<View>(R.id.tv_price)
           priceTv!!.setPrice(item.current_price)

           helper.setOnClickListener(R.id.tv_add, View.OnClickListener {
               if (itemClickListener != null)
                   itemClickListener.onItemClick(position)
           })*/
        helper.setText(R.id.tv_handled_fault, item.address)
        helper.setText(R.id.tv_handled_fault_type, item.type)
        helper.setText(R.id.tv_creat_time, item.time)
        helper.setOnClickListener(R.id.ll_handled_fault, View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            val intent = Intent(context, FaultDetailsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        })
    }
}