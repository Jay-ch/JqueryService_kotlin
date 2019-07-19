package com.jquery.service.android.ui.home.view

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.kotlindemo.jquery.widgets.SignInPopupDialog
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.UntreatedFaultListAdapter
import com.jquery.service.android.entity.UntreatedFaultListEntity
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.listener.OnItemClickListener
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.widgets.CustomeSwipeRefreshLayout
import com.jquery.service.android.widgets.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_untreated_fault_layout.*

/**
 * 未完结故障维修
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
class UntreatedFaultFragment : BaseFragment(), OnItemClickListener, ListSelectDialogListener {

    private var mUntreatedFaultListAdapter: UntreatedFaultListAdapter? = null
    var currentPage = 1
    internal var handler = Handler()
    private var mFooterTextView: TextView? = null
    private var mFooterPbView: ProgressBar? = null
    private var mFooterImageView: ImageView? = null
    private var mPbView: ProgressBar? = null
    private var mImageView: ImageView? = null
    private var mHeadContainer: RelativeLayout? = null
    private var layoutManager: StaggeredGridLayoutManager? = null


    override fun createLayout(): Int {
        return R.layout.fragment_untreated_fault_layout
    }

    override fun initViews() {
        super.initViews()
        mUntreatedFaultListAdapter = UntreatedFaultListAdapter(context!!)
        mUntreatedFaultListAdapter?.setItemClickListener(this)
        untreated_fault_recycler_view.setLayoutManager(LinearLayoutManager(context!!))
        untreated_fault_recycler_view.setAdapter(mUntreatedFaultListAdapter)
        layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        layoutManager?.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE)
        untreated_fault_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                layoutManager?.invalidateSpanAssignments() //防止第一行到顶部有空白区域
            }
        })
        untreated_fault_recycler_view.setPadding(0, 0, 0, 0)
        untreated_fault_recycler_view.addItemDecoration(SpacesItemDecoration(0))
        recyclerView_untreated_fault_frame.setHeaderViewBackgroundColor(-0x1)
        recyclerView_untreated_fault_frame.setHeaderView(createHeaderView())// add headerView
        recyclerView_untreated_fault_frame.setFooterView(createFooterView())
        recyclerView_untreated_fault_frame.setTargetScrollWithLayout(true)
        recyclerView_untreated_fault_frame
                .setOnPullRefreshListener(object : CustomeSwipeRefreshLayout.OnPullRefreshListener {

                    override fun onRefresh() {
                        mImageView?.setVisibility(View.GONE)
                        mPbView?.setVisibility(View.VISIBLE)
                        Handler().postDelayed({
                            //mPresenter.getCategoryList(id, currentPage)
                            recyclerView_untreated_fault_frame.setRefreshing(false)
                            mPbView?.setVisibility(View.GONE)
                        }, 1500)
                    }

                    override fun onPullDistance(distance: Int) {

                    }

                    override fun onPullEnable(enable: Boolean) {
                        mImageView?.setVisibility(View.VISIBLE)
                        mImageView?.setRotation((if (enable) 180 else 0).toFloat())
                    }
                })

        recyclerView_untreated_fault_frame
                .setOnPushLoadMoreListener(object : CustomeSwipeRefreshLayout.OnPushLoadMoreListener {

                    override fun onLoadMore() {
                        mFooterTextView?.setText("正在加载...")
                        mFooterImageView?.setVisibility(View.GONE)
                        mFooterPbView?.setVisibility(View.VISIBLE)
                        Handler().postDelayed({
                            ++currentPage
                            //mPresenter.getCategoryList(id, currentPage)
                            mFooterImageView?.setVisibility(View.VISIBLE)
                            mFooterPbView?.setVisibility(View.GONE)
                            recyclerView_untreated_fault_frame.setLoadMore(false)
                        }, 1000)
                    }

                    override fun onPushEnable(enable: Boolean) {
                        mFooterTextView?.setText(if (enable) "松开加载" else "上拉加载")
                        mFooterImageView?.setVisibility(View.VISIBLE)
                        mFooterImageView?.setRotation((if (enable) 0 else 180).toFloat())
                    }

                    override fun onPushDistance(distance: Int) {

                    }

                })

    }

    override fun onItemClick(position: Int) {
        val item = mUntreatedFaultListAdapter?.getItem(position)
        if (item != null) {
            //mPresenter.addShopCart(item!!.id, 1, "video")
        }
    }

    override fun selectType(s: String) {
    }


    private fun createFooterView(): View? {
        val footerView = LayoutInflater.from(recyclerView_untreated_fault_frame.getContext())
                .inflate(R.layout.layout_footer, null)
        mFooterPbView = footerView
                .findViewById(R.id.footer_pb_view) as ProgressBar
        mFooterImageView = footerView
                .findViewById(R.id.footer_image_view) as ImageView
        mFooterTextView = footerView
                .findViewById(R.id.footer_text_view) as TextView
        mFooterPbView?.setVisibility(View.GONE)
        mFooterImageView?.setVisibility(View.VISIBLE)
        mFooterTextView?.setText("上拉加载更多...")
        return footerView
    }

    private fun createHeaderView(): View? {
        val headerView = LayoutInflater.from(recyclerView_untreated_fault_frame.getContext())
                .inflate(R.layout.layout_head, null)
        mHeadContainer = headerView.findViewById(R.id.head_container) as RelativeLayout
        mPbView = headerView.findViewById(R.id.pb_view) as ProgressBar
        mImageView = headerView.findViewById(R.id.image_view) as ImageView
        mImageView?.setVisibility(View.VISIBLE)
        mImageView?.setImageResource(R.drawable.ptr_rotate_arrow)
        mPbView?.setVisibility(View.GONE)
        return headerView
    }

    override fun initData() {
        super.initData()
        //val map = HashMap<String, Int>(1)
        var map1 = UntreatedFaultListEntity("1", "电池电量低2", "发起时间：2018/09/28  14:23:12", "重庆市渝中区李嘉路202号")
        var map2 = UntreatedFaultListEntity("1", "电池电量低3", "发起时间：2018/09/29  14:23:12", "重庆市渝中区李嘉路200号")
        var map3 = UntreatedFaultListEntity("1", "电池电量低1", "发起时间：2018/09/30  14:23:12", "重庆市渝中区李嘉路201号")
        var list = mutableListOf<UntreatedFaultListEntity>()
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

        var mSignInPopupDialog = activity?.let {
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

    protected fun lazyLoad() {
        initData()
    }

    /**
     * kotlin List移除一个元素，添加一个元素时没有 remove和add函数只有 -= 和 +=
    在kotlin ArrayList中才有remove和add函数 没有 -= 和 +=
     */
    fun initDatas() {

    }

    fun getListSuccess(data: List<UntreatedFaultListEntity>?) {

        /*if (products != null) {
            if (products.size > 0) {
                if (currentPage > 1) {
                    mHandledFaultListAdapter?.addAll(products)
                } else {
                    mHandledFaultListAdapter?.replaceAll(products)
                    recyclerView_untreated_fault_frame.setLoadMoreEnable(false)
                }
            } else if (currentPage > 1 && products.size == 0) {
                recyclerView_untreated_fault_frame.setLoadMoreEnable(false)
                showToast("没有更多了")
            } else if (currentPage == 1 && products.size == 0) {
                recyclerView_untreated_fault_frame.setLoadMoreEnable(false)
                recyclerView_untreated_fault_frame.setVisibility(View.GONE)
                iv_empty_order.setVisibility(View.VISIBLE)
            } else if (currentPage == 1 && products.size > 0) {
                recyclerView_untreated_fault_frame.setLoadMoreEnable(false)
                recyclerView_untreated_fault_frame.setVisibility(View.GONE)
            }
        }*/


        loading.setVisibility(View.GONE)
        if (data != null) {
            if (data.size > 0) {
                iv_no_data.setVisibility(View.GONE)
                if (currentPage > 1) {
                    mUntreatedFaultListAdapter?.addAll(data)
                } else {
                    mUntreatedFaultListAdapter?.replaceAll(data)
                    recyclerView_untreated_fault_frame.setRefreshing(false)
                }
            } else if (currentPage > 1 && data.size == 0) {
                recyclerView_untreated_fault_frame.setLoadMore(false)
                showToast("没有更多了")
            } else if (currentPage == 1 && data.size == 0) {
                recyclerView_untreated_fault_frame.setLoadMore(false)
                iv_no_data.setVisibility(View.VISIBLE)
            } else if (currentPage == 1 && data.size > 0) {
                recyclerView_untreated_fault_frame.setLoadMore(false)
            }
        }
    }
}
