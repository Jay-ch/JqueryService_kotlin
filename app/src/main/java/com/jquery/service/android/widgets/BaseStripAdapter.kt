package com.jquery.service.android.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.jquery.service.android.entity.StripItem
import java.util.*

/**
 * @author J.query
 * @date 2019/5/16
 * @email j-query@foxmail.com
 */
class BaseStripAdapter: BaseAdapter {
    private var mInflater: LayoutInflater?=null
    private var mFloorList: List<String> = ArrayList()
    private var selectedPos: Int = 0
    private var mContext: Context ?= null

    private inner class NoteViewHolder {

        internal var mFloorTextTV: TextView? = null
    }

    constructor(ctx: Context): super() {
        mInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mContext = ctx
    }

    fun setmFloorList(mFloorList: List<String>) {
        this.mFloorList = mFloorList
    }

    fun getPosition(floor: String): Int {
        var re = 0
        for (i in mFloorList.indices) {
            if (floor == mFloorList[i]) {
                re = i
                break
            }
        }

        return re
    }

    fun setNoteList(floorList: List<String>) {
        mFloorList = floorList
    }

    override fun getCount(): Int {
        return mFloorList.size
    }

    override fun getItem(position: Int): Any {
        return mFloorList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setSelectedPostion(postion: Int) {
        selectedPos = postion
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: NoteViewHolder
        if (convertView == null) {
            convertView = StripItem(mContext)
            holder = NoteViewHolder()
            holder.mFloorTextTV = convertView.getmText()
            convertView.tag = holder
        } else {
            holder = convertView.tag as NoteViewHolder
        }

        val floor = mFloorList[position]
        if (floor != null) {
            // Log.i("indoor", "" + floor);
            holder.mFloorTextTV!!.text = floor
        }
        if (selectedPos == position) {
            refreshViewStyle(holder.mFloorTextTV, true)
        } else {
            refreshViewStyle(holder.mFloorTextTV, false)
        }
        return convertView
    }

    private fun refreshViewStyle(view: TextView?, isSelected: Boolean) {
        if (isSelected) {
            view!!.setBackgroundColor(StripItem.colorSelected)
        } else {
            view!!.setBackgroundColor(StripItem.color)
        }
        view.isSelected = isSelected
    }
}