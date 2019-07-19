package com.jquery.service.android.widgets.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.jquery.service.android.R
import com.jquery.service.android.adapter.BaseAdapterHelper
import com.jquery.service.android.adapter.CommRecyclerAdapter
import com.xp.sortrecyclerview.SortModel

/**
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
class SortAdapter : CommRecyclerAdapter<SortModel> {
    private val mInflater: LayoutInflater
    private var mData: MutableList<SortModel>? = null
    //private val mContext: Context ?= null

    private var context: Context
    private var layoutPosition = -1//保存当前选中的position

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context,data:MutableList<SortModel>) : super(context) {
        this.context = context
        mInflater = LayoutInflater.from(context)
        mData = data
        this.mContext = context
    }

    override fun getLayoutResId(item: SortModel, position: Int): Int {
        return if (showList) {
            R.layout.contacts_item
        } else {
            R.layout.contacts_item
        }
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: SortModel, position: Int) {
        val section = getSectionForPosition(position)
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.setVisible(R.id.tag, true)
            helper.setText(R.id.tag, mData?.get(position)?.getLetters())

            //holder.tvTag.setVisibility(View.VISIBLE)
            //holder.tvTag.setText(mData.get(position).getLetters())
        } else {
            helper.setVisible(R.id.tag, false)
        }

      /*  helper.setOnClickListener(R.id.ll_list_comm, View.OnClickListener {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position)
            itemClickListener.selectType(item.token)
            layoutPosition = position
            notifyDataSetChanged()
        })*/

        //itemView 的点击事件
        helper.setOnClickListener(R.id.ll_contacts, View.OnClickListener {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position)
        })

        helper.setText(R.id.name, mData?.get(position)?.getName())
        helper.setOnClickListener(R.id.name, View.OnClickListener {

            Toast.makeText(mContext, mData?.get(position)?.getName(), Toast.LENGTH_SHORT).show()
            /*val bundle = Bundle()
            bundle.putString("good_id", item.date)
            val intent = Intent(context, FaultDetailsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)*/
        })
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    fun getSectionForPosition(position: Int): Int {
        return mData?.get(position)?.getLetters()!![0].toInt()
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    fun getPositionForSection(section: Int): Int {
        for (i in 0 until itemCount) {
            val sortStr = mData?.get(i)?.getLetters()
            val firstChar = sortStr?.toUpperCase()!![0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    fun updateList(list: MutableList<SortModel>) {
        this.mData = list
        notifyDataSetChanged()
    }

    //**********************itemClick************************
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }
}