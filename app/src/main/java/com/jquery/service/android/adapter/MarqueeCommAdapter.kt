package com.jquery.service.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import com.jquery.service.android.R
import com.jquery.service.android.entity.MarqueeItem

/**
 * 跑马灯列表适配器
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
class MarqueeCommAdapter : CommRecyclerAdapter<MarqueeItem> {

    private lateinit var context: Context
    private var layoutPosition = -1//保存当前选中的position
    private var datas: List<String>? = null
    private var newPosition: Int? = null

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, data: List<String>) : super(context) {
        this.datas = data
    }

    override fun getLayoutResId(item: MarqueeItem, position: Int): Int {
        return if (showList) {
            R.layout.item_marquee_layout
        } else {
            R.layout.item_marquee_layout
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun onUpdate(helper: BaseAdapterHelper, item: MarqueeItem, position: Int) {
        helper.setText(R.id.mTv, item.name)
        // helper.setText(R.id.tv_title, item.letters)
        //helper?.setText(R.id.mTv, item)
    }

}