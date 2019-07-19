package com.jquery.service.android.ui.home.view

import android.content.ContentResolver
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.FriendAdapter
import com.jquery.service.android.entity.FriendEntity
import com.jquery.service.android.entity.PinyinComparator
import com.jquery.service.android.ui.MainActivity
import com.jquery.service.android.utils.CharacterParser
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.SearchView
import com.jquery.service.android.widgets.SideBar
import kotlinx.android.synthetic.main.activity_address_book.*
import kotlinx.android.synthetic.main.include_title_bar.*
import java.util.*

/**
 * 通讯录
 * @author J.query
 * @date 2019/3/12
 * @email j-query@foxmail.com
 */
class AddressBookActivity : BaseActivity(), View.OnClickListener {

    private val TAG = "InvitationAddressBookFragment"
    private val cr: ContentResolver? = null
    private val ls_contacts: ListView? = null
    private val mp = ArrayList<Map<String, String>>()
    private var rootView: View? = null
    private var mActivity: MainActivity? = null
    private var listview: ListView? = null
    private var queryContscts: List<FriendEntity>? = null
    private var mSearch: SearchView? = null

    private var mListView: ListView? = null

    private var dataLsit: MutableList<FriendEntity>? = ArrayList()

    private var sourceDataList: MutableList<FriendEntity> = ArrayList()

    /**
     * 好友列表的 adapter
     */
    private var adapter: FriendAdapter? = null
    /**
     * 右侧好友指示 Bar
     */
    private var mSidBar: SideBar? = null
    /**
     * 中部展示的字母提示
     */
    private var dialog: TextView? = null

    /**
     * 汉字转换成拼音的类
     */
    private var characterParser: CharacterParser? = null
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private var pinyinComparator: PinyinComparator? = null

    private val infalter: LayoutInflater? = null
    private var timeStart: Long = 0

    override fun createLayout(): Int {
        return R.layout.activity_address_book
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        top_title.setTvBackDrawableLeft(this,R.drawable.back_arrow)
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance()
        pinyinComparator = PinyinComparator.getInstance()
        //设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {

            override fun onTouchingLetterChanged(s: String) {
                //该字母首次出现的位置
                val position = adapter?.getPositionForSection(s[0].toInt())
                if (position != -1) {
                    mListView?.setSelection(position!!)
                }
            }
        })
    }

    override fun initData() {
        super.initData()
        initPermisionData()
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }



    override fun onClick(v: View?) {
        when (v?.getId()) {
        }
    }

    private fun initPermisionData() {
        showBaseLoading("加载中...")
        if (queryContscts != null && queryContscts?.size!! > 0) {
            sourceDataList = filledData(dataLsit!!) //过滤数据为有字母的字段  现在有字母 别的数据没有
        }

        //还原除了带字母字段的其他数据
        for (i in dataLsit?.indices!!) {
            sourceDataList[i].name = dataLsit?.get(i)?.name!!
            sourceDataList[i].phone = dataLsit?.get(i)?.phone!!
        }
        dataLsit = null //释放资源

        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator)

        val bundle = Bundle()
        val wx_url = bundle.getString("wx_url")
        adapter = FriendAdapter(this, sourceDataList, wx_url)
        //mListView.addHeaderView(headView);
        recyclerView_contacts?.setAdapter(adapter)
        hideBaseLoading()
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private fun filterData(filterStr: String) {
        showBaseLoading("加载中")
        var filterDateList: MutableList<FriendEntity> = ArrayList<FriendEntity>()
        if (TextUtils.isEmpty(filterStr)) {
            hideBaseLoading()
            filterDateList = sourceDataList

        } else {
            filterDateList.clear()
            for (friendModel in sourceDataList) {
                val name = friendModel.name
                if (name.indexOf(filterStr) != -1 || characterParser?.getSelling(name)!!.startsWith(filterStr)) {
                    hideBaseLoading()
                    filterDateList.add(friendModel)
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator)
        adapter?.addAll(filterDateList)
        hideBaseLoading()
    }


    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    private fun filledData(lsit: MutableList<FriendEntity>): MutableList<FriendEntity> {

        val mFriendList = ArrayList<FriendEntity>()

        for (i in lsit.indices) {
            val friendModel = FriendEntity("张三", "重庆", "1", "1888888888")
            friendModel.name = lsit[i].name
            //汉字转换成拼音
            val pinyin = characterParser?.getSelling(lsit[i].name)
            val sortString = pinyin?.substring(0, 1)?.toUpperCase()

            // 正则表达式，判断首字母是否是英文字母
            if (sortString?.matches("[A-Z]".toRegex())!!) {
                friendModel.letters = sortString?.toUpperCase()
            } else {
                friendModel.letters = "#"
            }
            mFriendList.add(friendModel)
        }
        return mFriendList

    }
}