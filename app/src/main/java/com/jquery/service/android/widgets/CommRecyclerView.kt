package com.jquery.service.android.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.jquery.service.android.R
import com.jquery.service.android.adapter.CommRecyclerAdapter
import com.jquery.service.android.entity.PageEntity
import com.jquery.service.android.listener.RefreshListener
import com.jquery.service.android.utils.DisplayUtils.px2dip
import com.jquery.service.android.widgets.loading.DefaultLoadingLayout
import com.jquery.service.android.widgets.loading.SmartLoadingLayout
import jquery.ptr.PtrClassicFrameLayout
import jquery.ptr.PtrDefaultHandler
import jquery.ptr.PtrFrameLayout
import jquery.ptr.loadmore.OnLoadMoreListener
import jquery.ptr.recyclerview.RecyclerAdapterWithHF

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
class CommRecyclerView: LinearLayout {
    var frameLayout: PtrClassicFrameLayout? = null
    lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommRecyclerAdapter<*>
    private var enableRefresh: Boolean = false

    private var lineMarginDecoration: LineMarginDecoration? = null
    private var itemMarginDecoration: ItemMarginDecoration? = null
    /**
     * 1.LinearLayoutManager
     * 2.GridLayoutManager
     */
    private var itemType: Int = 0
    /**
     * 1.代表线条
     */
    private var itemHeight: Int = 0
    private var emptyIcon: Drawable? = null
    private var loadingLayout: DefaultLoadingLayout? = null
    private var adapterWithHF: RecyclerAdapterWithHF? = null
    private var shopTopLine: Boolean = false

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initViews(context, attrs)
    }
    private fun initViews(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.fragment_common_recycler, this, true)
        frameLayout = findViewById<View>(R.id.flayout_ptr) as PtrClassicFrameLayout
        recyclerView = findViewById<View>(R.id.recycler_content) as RecyclerView
        //loadingLayout = SmartLoadingLayout().createDefaultLayout()
        //val innerClass = outClass.InnerClass()
        loadingLayout = SmartLoadingLayout().createDefaultLayout(getContext(), frameLayout!!)
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.Comm_RecyclerView)
        enableRefresh = a.getBoolean(R.styleable.Comm_RecyclerView_enableRefresh, true)
        itemType = a.getInt(R.styleable.Comm_RecyclerView_itemType, 1)
        itemHeight = a.getInt(R.styleable.Comm_RecyclerView_itemHeight, 1)
        emptyIcon = a.getDrawable(R.styleable.Comm_RecyclerView_emptyIcon)
        shopTopLine = a.getBoolean(R.styleable.Comm_RecyclerView_isShowTop, true)
        val emptyStr = a.getString(R.styleable.Comm_RecyclerView_emptyDes)
        loadingLayout?.setEmptyDescription(emptyStr)

        if (emptyIcon != null)
            loadingLayout?.replaceEmptyIcon(emptyIcon!!)

        initData()
        a.recycle()
    }

    private fun initData() {
        when (itemType) {
            1 -> {
                lineMarginDecoration = LineMarginDecoration(1, context)
                recyclerView.addItemDecoration(lineMarginDecoration)
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
            2 -> {
                itemMarginDecoration = ItemMarginDecoration(itemHeight.toFloat())
                recyclerView.addItemDecoration(itemMarginDecoration)
                recyclerView.layoutManager = GridLayoutManager(context, 3)
            }
            3 -> {
                val height = resources.getDimension(R.dimen.item_margin).toInt()
                itemMarginDecoration = ItemMarginDecoration(px2dip(context, height.toFloat()), shopTopLine)
                recyclerView.addItemDecoration(itemMarginDecoration)
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }

        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        frameLayout?.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout) {
                if (refreshListener != null) {
                    refreshListener!!.onRefresh()
                }
            }

            override fun checkCanDoRefresh(frame: PtrFrameLayout, content: View, header: View): Boolean {
                return if (enableRefresh) {
                    super.checkCanDoRefresh(frame, content, header)
                } else {
                    false
                }
            }
        })
        frameLayout?.setOnLoadMoreListener(OnLoadMoreListener {
            if (refreshListener != null) {
                refreshListener!!.onLoadMore()
            }
        })
    }

    /**
     * @param context
     * @param showList
     */
    fun showList(context: Context, showList: Boolean) {
        if (itemMarginDecoration == null && !showList) {
            itemMarginDecoration = ItemMarginDecoration(10F)
        }
        if (adapter != null) {
            adapter.setType(showList)
            if (showList) {
                recyclerView.removeItemDecoration(itemMarginDecoration)
                recyclerView.addItemDecoration(lineMarginDecoration)
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                recyclerView.removeItemDecoration(lineMarginDecoration)
                recyclerView.addItemDecoration(itemMarginDecoration)
                recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
            adapter.notifyDataSetChanged()
        }
    }


    /**
     * @param adapter
     */
    fun setAdapter(adapter: CommRecyclerAdapter<*>) {
        this.adapter = adapter
        adapterWithHF = RecyclerAdapterWithHF(adapter)
        recyclerView.adapter = adapterWithHF
    }

    /**
     * 添加头部
     *
     * @param headerView
     */
    fun addHeader(headerView: View) {
        if (adapterWithHF != null) {
            adapterWithHF!!.addHeader(headerView)
        }
    }

    fun getHeaderSize(): Int {
        return if (adapterWithHF != null) {
            adapterWithHF?.getHeadSize()!!
        } else 0
    }

    fun removeHeader(headerView: View) {
        if (adapterWithHF != null) {
            adapterWithHF?.removeHeader(headerView)
        }
    }

    fun loadSuccess() {
        showDone()
        refreshComplete()
    }

    /**
     * 加载成功
     */
    fun loadSuccess(list: List<*>?) {
        if (list != null && list.size > 0) {
            showDone()
        } else {
            showEmpty()
        }
        refreshComplete()
    }

    /**
     * @param list
     * @param page
     */
    fun loadSuccess(list: List<*>?, page: PageEntity?) {
        if (list != null && list.size > 0) {
            showDone()
        } else {
            showEmpty()
        }
        refreshComplete()
        if (page != null)
            if (page!!.page > 1) {
                frameLayout?.loadMoreComplete(page!!.more === 1)
            } else {
                frameLayout?.setLoadMoreEnable(page!!.more === 1)
            }
    }

    /**
     * @param listSize
     * @param page
     */
    fun loadSuccess(listSize: Int, page: PageEntity?) {
        if (listSize > 0) {
            showDone()
        } else {
            showEmpty()
        }
        refreshComplete()
        if (page != null)
            if (page!!.page > 1) {
                frameLayout?.loadMoreComplete(page!!.more === 1)
            } else {
                frameLayout?.setLoadMoreEnable(page!!.more === 1)
            }
    }

    /**
     * 使用带header
     */
    fun loadSuccess(page: Int, totalPage: Int) {
        showDone()
        if (page != 1) {
            frameLayout?.loadMoreComplete(page < totalPage)
        } else {
            frameLayout?.setLoadMoreEnable(page < totalPage)
        }
    }

    /**
     * 刷新完成
     */
    fun refreshComplete() {
        if (frameLayout != null && frameLayout!!.isRefreshing()) {
            frameLayout!!.refreshComplete()
        }
    }

    /**
     * 分页加载完成
     *
     * @param b
     */
    fun moreSuccess(b: Boolean) {
        frameLayout?.loadMoreComplete(b)
    }

    fun loadStart() {
        showLoading()
    }

    fun loadError(listener: View.OnClickListener) {
        if (loadingLayout != null) {
            loadingLayout?.setErrorButtonListener(listener)
            loadingLayout?.onError()
        }
    }

    fun showDone() {
        if (loadingLayout != null)
            loadingLayout?.onDone()
    }

    /**
     * no data
     */
    fun showEmpty() {
        if (loadingLayout != null)
            loadingLayout?.onEmpty()
    }

    /**
     * loading
     */
    fun showLoading() {
        if (loadingLayout != null)
            loadingLayout?.onLoading()
    }

    /**
     * 刷新操作接口
     */
    private var refreshListener: RefreshListener? = null

    fun setRefreshListener(refreshListener: RefreshListener) {
        this.refreshListener = refreshListener
    }
}