package com.jquery.service.android.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.jquery.service.android.adapter.HorizontalListAdapter
import com.jquery.service.android.entity.MenuItemEntity
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.listener.OnItemClickListener
import java.util.*

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class HorizontalListView : RecyclerView, OnItemClickListener, ListSelectDialogListener {


    private lateinit var mContext: Context
    private var adapter: HorizontalListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var viewPager: ViewPager? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        initViews()
    }

    private fun initViews() {
        adapter = HorizontalListAdapter(context)
        layoutManager = LinearLayoutManager(context)
        layoutManager?.setOrientation(RecyclerView.HORIZONTAL)
        setLayoutManager(layoutManager)
        adapter?.setItemClickListener(this)
        setAdapter(adapter)
    }

    override fun selectType(s: String) {
    }

    override fun onItemClick(position: Int) {
    }

    fun initMenus(menuItems: List<MenuItemEntity>) {
        if (adapter != null) {
            adapter?.addAll(menuItems)
        }
    }

    fun setupWithViewPager(vp: ViewPager?) {
        if (vp != null) {
            viewPager = vp
            vp.addOnPageChangeListener(PagerChangeListener())
        }
    }


    fun setDeuceScreen() {
        adapter?.setDeuceScreen()
    }

    fun initMenus(goodTitles: Array<String>) {
        val menuItems: MutableList<MenuItemEntity> = ArrayList()
        //val mList: List<Int> = listOf()//不可变集合

        //val menuItems = ArrayList()
        var menuItem: MenuItemEntity
        for (s in goodTitles) {
            menuItem = MenuItemEntity()
            menuItem.title = s
            menuItems.add(menuItem)
        }
        if (adapter != null)
            adapter?.replaceAll(menuItems)
    }

    /**
     * 切换
     *
     * @param position
     */
    fun selectIndex(position: Int) {
        if (adapter != null) {
            adapter?.setCurrentSelect(position)
            adapter?.notifyDataSetChanged()
        }
        smoothScrollToPosition(position)
    }

    private inner class PagerChangeListener : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            selectIndex(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
}