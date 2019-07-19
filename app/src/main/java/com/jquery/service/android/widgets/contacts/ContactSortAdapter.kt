package com.jquery.service.android.widgets.contacts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.jquery.service.android.R
import com.xp.sortrecyclerview.SortModel

/**
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
class ContactSortAdapter : RecyclerView.Adapter<ContactSortAdapter.ViewHolder> {
    private var mInflater: LayoutInflater
    private var mData: List<SortModel>? = null
    private val mContext: Context

   constructor(context: Context, data: List<SortModel>): super() {
        mInflater = LayoutInflater.from(context)
        mData = data
        this.mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactSortAdapter.ViewHolder {
        val view = mInflater.inflate(R.layout.contacts_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.tvTag = view.findViewById(R.id.tag)
        viewHolder.tvName = view.findViewById(R.id.name)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ContactSortAdapter.ViewHolder, position: Int) {
        val section = getSectionForPosition(position)
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag?.setVisibility(View.GONE)
            holder.tvTag?.setText(mData!![position].getLetters())
        } else {
            holder.tvTag?.setVisibility(View.GONE)
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, position) })

        }

        holder.tvName?.setText(this.mData!![position].getName())

        holder.tvName?.setOnClickListener(View.OnClickListener { Toast.makeText(mContext, mData!![position].getName(), Toast.LENGTH_SHORT).show() })

    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    //**********************itemClick************************
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }
    //**************************************************************

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvTag: TextView? = null
        internal var tvName: TextView? = null
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    fun updateList(list: List<SortModel>) {
        this.mData = list
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Any {
        return mData!![position]
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    fun getSectionForPosition(position: Int): Int {
        return mData!![position].getLetters()!![0].toInt()
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    fun getPositionForSection(section: Int): Int {
        for (i in 0 until itemCount) {
            val sortStr = mData!![i].getLetters()
            val firstChar = sortStr!!.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }
}