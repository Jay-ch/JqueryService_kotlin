package com.jquery.service.android.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.SectionIndexer
import com.jquery.service.android.R
import com.jquery.service.android.entity.FriendEntity
import java.util.*

/**
 * @author J.query
 * @date 2019/4/28
 * @email j-query@foxmail.com
 */
class FriendAdapter : CommRecyclerAdapter<FriendEntity>, SectionIndexer {


    private var context: Context
    private var layoutPosition = -1//保存当前选中的position
    private var datas: ArrayList<FriendEntity>? = null

    private val TAG = "FriendAdapter"

    private var list: List<FriendEntity>? = null
    private var wx_url: String? = null

    constructor(context: Context, list: List<FriendEntity>?, wx_url: String?) : super(context) {
        this.context = context
        this.list = list
        this.wx_url = wx_url
    }

    override var mContext: Context
        get() = context
        set(value) {}

    constructor(context: Context) : super(context) {
        this.context = context
    }

    override fun getLayoutResId(item: FriendEntity, position: Int): Int {
        return if (showList) {
            R.layout.friend_item
        } else {
            R.layout.friend_item
        }
    }

    override fun onUpdate(helper: BaseAdapterHelper, item: FriendEntity, position: Int) {
        //根据position获取分类的首字母的Char ascii值
        val section = getSectionForPosition(position)
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            helper.setVisible(R.id.catalog, false)
            helper.setText(R.id.catalog, item.letters)
        } else {
            helper.setVisible(R.id.catalog, false)
        }
        helper.setText(R.id.friendname, item.name)
    }


    override fun getSections(): Array<Any?> {
        return arrayOfNulls(0)
    }

    override fun getSectionForPosition(position: Int): Int {
        return list?.get(position)?.letters!![0].toInt()
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        for (i in 0 until list?.size!!) {
            val sortStr = list?.get(i)?.letters
            val firstChar = sortStr?.toUpperCase()?.get(0)
            if (firstChar?.toInt() == sectionIndex) {
                return i
            }
        }

        return -1
    }


    /**
     * 发送短信
     *
     * @param smsBody
     */
    private fun sendSMS(phoneNum: String, smsBody: String) {
        Log.d("phone", "sendSMS(String phoneNum, String smsBody)==$phoneNum")
        val smsToUri = Uri.parse("smsto:$phoneNum")
        val intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        intent.putExtra("sms_body", smsBody)
        context.startActivity(intent)
    }
}