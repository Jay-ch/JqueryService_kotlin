package com.jquery.service.android.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckedTextView
import android.widget.LinearLayout
import com.jquery.service.android.R
import com.jquery.service.android.entity.MenuItemEntity
import com.jquery.service.android.utils.DisplayUtils

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class HorizontalListAdapter : CommRecyclerAdapter<MenuItemEntity> {
    override var mContext: Context
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    private var currentSelect = 0
    //是否平分屏幕
    private var isDeuceScreen = false
    private val MAX_SHOW_TAB_SIZE = 4

    private var mLayoutResId: Int = 0

    /*constructor(context: Context) : super(){

        context, R.layout.item_top_menu
    }*/
    constructor(context: Context) : super(context) {
        this.mContext = context
        this.mLayoutResId = R.layout.item_top_menu
    }

    fun setDeuceScreen() {
        isDeuceScreen = true
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: MenuItemEntity, position: Int) {
        val titleTv = helper.getView<CheckedTextView>(R.id.tv_title)
        val lineTv = helper.getView<CheckedTextView>(R.id.tv_line)
        titleTv!!.text = item.title
        val b = position == currentSelect
        titleTv.isChecked = b
        lineTv!!.isChecked = b
        helper.setOnClickListener(R.id.llayout_root, View.OnClickListener {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position)
        })
        if (isDeuceScreen) {
            val root = helper.getView<LinearLayout>(R.id.llayout_root)
            val params = root!!.layoutParams as RecyclerView.LayoutParams
            params.width = DisplayUtils.getScreenWidth() / MAX_SHOW_TAB_SIZE
            root.layoutParams = params
        }
    }

    fun setCurrentSelect(currentSelect: Int) {
        this.currentSelect = currentSelect
    }
}