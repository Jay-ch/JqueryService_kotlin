package com.jquery.service.android.adapter

import android.content.Context
import com.jquery.service.android.R
import com.jquery.service.android.entity.ContactEntity
import java.util.*

/**
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class ContactAdapter : CommRecyclerAdapter<ContactEntity> {
    private var context: Context
    private var layoutPosition = -1//保存当前选中的position
    private var datas: ArrayList<ContactEntity>? = null

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: ContactEntity, position: Int): Int {
        return if (showList) {
            R.layout.item_list_contact
        } else {
            R.layout.item_list_contact
        }
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: ContactEntity, position: Int) {
       /* helper.setText(R.id.contact_title, item.getName())
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
        }*/


        //holder.setText(R.id.contact_title, item.getName())
        //val headImg = holder.getView(R.id.contact_head)
        helper.setText(R.id.contact_title, item.getName())
       /* if (isScrolling) {
            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect)
        } else {
            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect)
        }*/
        //如果是第0个那么一定显示#号
        if (position == 0) {
            helper.setVisible(R.id.contact_catalog, true)
            helper.setVisible(R.id.contact_line, true)

            helper.setText(R.id.contact_catalog,"#")

        } else {
            //如果和上一个item的首字母不同，则认为是新分类的开始
            val prevData = datas?.get(position - 1)
            if (item.getFirstChar() !== prevData?.getFirstChar()) {
                helper.setVisible(R.id.contact_catalog, true)
                helper.setText(R.id.contact_catalog, "" + item.getFirstChar())
                helper.setVisible(R.id.contact_line, true)
            } else {
                helper.setVisible(R.id.contact_catalog, false)

                helper.setVisible(R.id.contact_line, false)

            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
     fun getSectionForPosition(position: Int): Char? {
        val item = datas?.get(position)
        return item?.getFirstChar()
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
     fun getPositionForSection(section: Int): Int {
        for (i in 0 until datas?.size!!) {
            val firstChar = datas?.get(i)?.getFirstChar()
            if (firstChar?.toInt() == section) {
                return i
            }
        }
        return -1
    }

     fun getSections(): Array<Any>? {
        return null
    }
}