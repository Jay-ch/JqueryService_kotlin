package com.jquery.service.android.ui.home.view

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.kotlindemo.jquery.widgets.SignInPopupDialog
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.HandledFaultListAdapter
import com.jquery.service.android.entity.HandledFaultListEntity
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.listener.OnItemClickListener
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.widgets.CustomeSwipeRefreshLayout
import com.jquery.service.android.widgets.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_handled_fault_layout.*
import kotlinx.android.synthetic.main.layout_footer.*
import kotlinx.android.synthetic.main.layout_head.*

/**
 * 已完结故障维修
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class HandledFaultFragment : BaseFragment(), OnItemClickListener, ListSelectDialogListener {

    private var mHandledFaultListAdapter: HandledFaultListAdapter? = null
    var currentPage = 1
    internal var handler = Handler()

//    private var mPbView: ProgressBar? = null
//    private var mImageView: ImageView? = null
//    private var mHeadContainer: RelativeLayout? = null
    private var layoutManager: StaggeredGridLayoutManager? = null



    override fun createLayout(): Int {
        return R.layout.fragment_handled_fault_layout
    }

    private lateinit var mSignInPopupDialog: SignInPopupDialog

    override fun initViews() {
        super.initViews()
        mHandledFaultListAdapter = HandledFaultListAdapter(context!!)
        mHandledFaultListAdapter?.setItemClickListener(this)
        handled_fault_recycler_view.layoutManager = LinearLayoutManager(context!!)
        handled_fault_recycler_view.adapter = mHandledFaultListAdapter
        layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        layoutManager?.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        handled_fault_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                layoutManager?.invalidateSpanAssignments() //防止第一行到顶部有空白区域
            }
        })
        handled_fault_recycler_view.setPadding(0, 0, 0, 0)
        handled_fault_recycler_view.addItemDecoration(SpacesItemDecoration(0))
        recyclerView_handled_fault_frame.setHeaderViewBackgroundColor(-0x1)
        recyclerView_handled_fault_frame.setHeaderView(createHeaderView())// add headerView
        recyclerView_handled_fault_frame.setFooterView(createFooterView())
        recyclerView_handled_fault_frame.setTargetScrollWithLayout(true)
        recyclerView_handled_fault_frame
                .setOnPullRefreshListener(object : CustomeSwipeRefreshLayout.OnPullRefreshListener {

                    override fun onRefresh() {
                        image_view?.visibility = View.GONE
                        pb_view?.visibility = View.VISIBLE
                        Handler().postDelayed({
                            //mPresenter.getCategoryList(id, currentPage)
                            recyclerView_handled_fault_frame.setRefreshing(false)
                            pb_view?.visibility = View.GONE
                        }, 1500)
                    }

                    override fun onPullDistance(distance: Int) {

                    }

                    override fun onPullEnable(enable: Boolean) {
                        image_view.visibility = View.VISIBLE
                        image_view.rotation = (if (enable) 180 else 0).toFloat()
                    }
                })

        recyclerView_handled_fault_frame
                .setOnPushLoadMoreListener(object : CustomeSwipeRefreshLayout.OnPushLoadMoreListener {

                    override fun onLoadMore() {
                        footer_text_view?.text = "正在加载..."
                        footer_image_view?.visibility = View.GONE
                        footer_pb_view?.visibility = View.VISIBLE
                        Handler().postDelayed({
                            ++currentPage
                            //mPresenter.getCategoryList(id, currentPage)
                            footer_image_view?.visibility = View.VISIBLE
                            footer_pb_view?.visibility = View.GONE
                            recyclerView_handled_fault_frame.setLoadMore(false)
                        }, 1000)
                    }

                    override fun onPushEnable(enable: Boolean) {
                        footer_text_view?.text = if (enable) "松开加载" else "上拉加载"
                        footer_image_view?.visibility = View.VISIBLE
                        footer_image_view?.rotation = (if (enable) 0 else 180).toFloat()
                    }

                    override fun onPushDistance(distance: Int) {

                    }

                })

    }
    override fun onItemClick(position: Int) {
        val item = mHandledFaultListAdapter?.getItem(position)
        if (item != null) {
            //mPresenter.addShopCart(item!!.id, 1, "video")
        }
    }

    override fun selectType(s: String) {
    }
    private fun createFooterView(): View? {
        val footerView = LayoutInflater.from(recyclerView_handled_fault_frame.getContext())
                .inflate(R.layout.layout_footer, null)

        footer_pb_view?.visibility = View.GONE
        footer_image_view?.visibility = View.VISIBLE
        footer_text_view?.text = "上拉加载更多..."
        return footerView
    }

    private fun createHeaderView(): View? {
        val headerView = LayoutInflater.from(recyclerView_handled_fault_frame.getContext())
                .inflate(R.layout.layout_head, null)

        image_view?.visibility = View.VISIBLE
        image_view?.setImageResource(R.drawable.ptr_rotate_arrow)
        pb_view?.visibility = View.GONE
        return headerView
    }

    override fun initData() {
        super.initData()
        //val map = HashMap<String, Int>(1)
        val map1 = HandledFaultListEntity("1","电池电量低", "发起时间：2018/09/28  14:23:12", "重庆市渝北区李嘉路202号")
        val map2 = HandledFaultListEntity("1","电池电量低", "发起时间：2018/09/29  14:23:12", "重庆市渝北区李嘉路200号")
        val map3 = HandledFaultListEntity("1","电池电量低", "发起时间：2018/09/30  14:23:12", "重庆市渝北区李嘉路201号")
        val list = mutableListOf<HandledFaultListEntity>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        getListSuccess(list)

        /**
         * 接口调用示例
         */
        /* val map = HashMap<String, Int>(1)
         map["page"] = currentPage
         addSubscriber(api.getLikeList(createBody(map)),
                 object : BaseSubscriber<HttpResult<GoodsListResult>>() {
                     protected fun onSuccess(result: HttpResult<GoodsListResult>) {
                         getListSuccess(result.data.products)
                     }

                     protected fun onFail(s: String) {
                         showToast(s)
                     }
                 })*/
    }


    fun ShowDialog() {

        val mSignInPopupDialog = activity?.let {
            SignInPopupDialog(it)
        }
        mSignInPopupDialog?.setListener(object : SelectDialogListener {
            override fun leftClick() {
                startActivity(UserInfoActivity::class.java)
            }

            override fun rightClick() {

            }
        })
        mSignInPopupDialog?.setCanceledOnTouchOutside(false)
        mSignInPopupDialog?.setCancelable(true)
        mSignInPopupDialog?.setTitle("提示")
        mSignInPopupDialog?.setLeftAndRightText("确定----", "取消------")
        mSignInPopupDialog?.showDialog()
    }

    private fun lazyLoad() {
        initData()
    }

    /**
     * kotlin List移除一个元素，添加一个元素时没有 remove和add函数只有 -= 和 +=
    在kotlin ArrayList中才有remove和add函数 没有 -= 和 +=
     */
    fun initDatas() {

    }

    private fun getListSuccess(data: List<HandledFaultListEntity>?) {

        /*if (products != null) {
            if (products.size > 0) {
                if (currentPage > 1) {
                    mHandledFaultListAdapter?.addAll(products)
                } else {
                    mHandledFaultListAdapter?.replaceAll(products)
                    recyclerView_handled_fault_frame.setLoadMoreEnable(false)
                }
            } else if (currentPage > 1 && products.size == 0) {
                recyclerView_handled_fault_frame.setLoadMoreEnable(false)
                showToast("没有更多了")
            } else if (currentPage == 1 && products.size == 0) {
                recyclerView_handled_fault_frame.setLoadMoreEnable(false)
                recyclerView_handled_fault_frame.setVisibility(View.GONE)
                iv_empty_order.setVisibility(View.VISIBLE)
            } else if (currentPage == 1 && products.size > 0) {
                recyclerView_handled_fault_frame.setLoadMoreEnable(false)
                recyclerView_handled_fault_frame.setVisibility(View.GONE)
            }
        }*/


        loading.visibility = View.GONE
        if (data != null) {
            if (data.isNotEmpty()) {
                iv_no_data.visibility = View.GONE
                if (currentPage > 1) {
                    mHandledFaultListAdapter?.addAll(data)
                } else {
                    mHandledFaultListAdapter?.replaceAll(data)
                    recyclerView_handled_fault_frame.setRefreshing(false)
                }
            } else if (currentPage > 1 && data.isEmpty()) {
                recyclerView_handled_fault_frame.setLoadMore(false)
                showToast("没有更多了")
            } else if (currentPage == 1 && data.isEmpty()) {
                recyclerView_handled_fault_frame.setLoadMore(false)
                iv_no_data.visibility = View.VISIBLE
            } else if (currentPage == 1 && data.isNotEmpty()) {
                recyclerView_handled_fault_frame.setLoadMore(false)
            }
        }
    }
}