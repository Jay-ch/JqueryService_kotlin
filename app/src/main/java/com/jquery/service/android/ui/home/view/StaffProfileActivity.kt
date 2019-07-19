package com.jquery.service.android.ui.home.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.SearchView
import com.jquery.service.android.widgets.contacts.ContactSortAdapter
import com.jquery.service.android.widgets.contacts.SideBar
import com.xp.sortrecyclerview.PinyinComparator
import com.xp.sortrecyclerview.PinyinUtils
import com.xp.sortrecyclerview.SortModel
import kotlinx.android.synthetic.main.activity_staff_profile.*
import kotlinx.android.synthetic.main.include_title_bar.*
import java.util.*

/**
 * 人员情况
 * @author J.query
 * @date 2019/6/11
 * @email j-query@foxmail.com
 */
class StaffProfileActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var adapter: ContactSortAdapter? = null
    private var mClearEditText: SearchView? = null
    internal var manager: LinearLayoutManager? = null

    //private var SourceDateList: List<SortModel>? = null
    private var SourceDateList: MutableList<SortModel> = ArrayList()

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private var pinyinComparator: PinyinComparator? = null

    override fun createLayout(): Int {
        return R.layout.activity_staff_profile
    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }



    override fun initViews() {
        setStatusBar()
        pinyinComparator = PinyinComparator()
        //sideBar = findViewById<View>(R.id.sideBar)
        //dialog = findViewById<View>(R.id.dialog)
        sideBar.setTextView(dialog)

        //设置右侧SideBar触摸监听
        sideBar?.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {

            override fun onTouchingLetterChanged(s: String?) {

                //该字母首次出现的位置
                val position = s?.get(0)?.toInt()?.let { adapter?.getPositionForSection(it) }
                if (position != -1) {
                    position?.let { manager?.scrollToPositionWithOffset(it, 0) }
                }

            }
        })
        SourceDateList = filledData(resources.getStringArray(R.array.date).toMutableList())
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator)
        //RecyclerView设置manager
        manager = LinearLayoutManager(this)
        manager?.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(manager)
        adapter = ContactSortAdapter(this, SourceDateList)
        recyclerView.setAdapter(adapter)
        tv_num.setText("总人数：" + SourceDateList.size + "人")
        //adapter?.addAll(SourceDateList!!)
        //item点击事件
        /*adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((SortModel)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        //根据输入框输入值的改变来过滤搜索
        filter_edit.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {

            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */

    private fun filledData(date: MutableList<String>): MutableList<SortModel> {
        val mSortList = ArrayList<SortModel>()

        for (i in date.indices) {
            val sortModel = SortModel()
            sortModel.setName(date[i])
            //汉字转换成拼音
            val pinyin = PinyinUtils.getPingYin(date[i])
            val sortString = pinyin.substring(0, 1).toUpperCase()

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]".toRegex())) {
                sortModel.setLetters(sortString.toUpperCase())
            } else {
                sortModel.setLetters("#")
            }

            mSortList.add(sortModel)
        }
        return mSortList

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private fun filterData(filterStr: String) {
        // var filterDateList: MutableList<SortModel> = ArrayList()
        var filterDateList: MutableList<SortModel> = ArrayList<SortModel>()

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList
        } else {
            filterDateList.clear()
            for (sortModel in SourceDateList) {
                val name = sortModel.getName()
                if (name!!.indexOf(filterStr) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr)
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr)
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr)) {
                    filterDateList.add(sortModel)
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator)
        adapter?.updateList(filterDateList)
    }
}