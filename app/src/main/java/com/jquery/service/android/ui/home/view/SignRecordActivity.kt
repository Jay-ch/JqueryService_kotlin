package com.jquery.service.android.ui.home.view

import android.content.Intent
import android.os.Bundle
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
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.SignRecordListAdapter
import com.jquery.service.android.entity.SignRecordListEntity
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.listener.RefreshListener
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.CustomeSwipeRefreshLayout
import com.jquery.service.android.widgets.SpacesItemDecoration
import com.jquery.service.android.widgets.dialog.SelectDialog
import kotlinx.android.synthetic.main.activity_sign_record.*
import kotlinx.android.synthetic.main.include_title_bar.*


/**
 * 签到记录
 * @author J.query
 * @date 2019/3/12
 * @email j-query@foxmail.com
 */
class SignRecordActivity : BaseActivity(), View.OnClickListener, ListSelectDialogListener, RefreshListener {


    private val TAG = "SignRecordActivity"

    private var mSignRecordListAdapter: SignRecordListAdapter? = null
    //private var mAdapter: RecyclerAdapterWithHF? = null
    var currentPage = 1
    internal var handler = Handler()

    private var mSelectDialog: SelectDialog? = null
    private var mFooterTextView: TextView? = null
    private var mFooterPbView: ProgressBar? = null
    private var mFooterImageView: ImageView? = null
    private var mPbView: ProgressBar? = null
    private var mImageView: ImageView? = null
    private var mHeadContainer: RelativeLayout? = null
    private var layoutManager: StaggeredGridLayoutManager? = null


    override fun createLayout(): Int {
        return R.layout.activity_sign_record
    }

    private fun setStatusWhiteBar() {

    }

    override fun setStatusBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        mSignRecordListAdapter = SignRecordListAdapter(this)
        mSignRecordListAdapter?.setItemClickListener(this)
        /* mAdapter = RecyclerAdapterWithHF(mSignRecordListAdapter)
         sign_record_recycler_view.setLayoutManager(LinearLayoutManager(this))
         sign_record_recycler_view.setAdapter(mAdapter)
         sign_record_recycler_view.postDelayed(Runnable { recyclerView_sign_record_frame.autoRefresh(true) }, 150)

         recyclerView_sign_record_frame.setPtrHandler(object : PtrDefaultHandler() {

             override fun onRefreshBegin(frame: PtrFrameLayout) {
                 handler.postDelayed(Runnable {
                     currentPage = 1
                     initData()
                     mAdapter?.notifyDataSetChanged()
                     recyclerView_sign_record_frame.refreshComplete()
                     recyclerView_sign_record_frame.setLoadMoreEnable(true)
                 }, 1000)
             }
         })
         //api = RetrofitHelper.createApi(LikesServices::class.java)
         recyclerView_sign_record_frame.setOnLoadMoreListener(OnLoadMoreListener {
             handler.postDelayed(Runnable {
                 //++currentPage
                 initData()
                 mAdapter?.notifyDataSetChanged()
                 recyclerView_sign_record_frame.loadMoreComplete(false)
             }, 1000)
         })*/


        //layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        sign_record_recycler_view.setLayoutManager(LinearLayoutManager(this))
        sign_record_recycler_view.setAdapter(mSignRecordListAdapter)
        layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        layoutManager?.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE)
        sign_record_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                layoutManager?.invalidateSpanAssignments() //防止第一行到顶部有空白区域
            }
        })
        sign_record_recycler_view.setPadding(0, 0, 0, 0)
        sign_record_recycler_view.addItemDecoration(SpacesItemDecoration(0))
        recyclerView_sign_record_frame.setHeaderViewBackgroundColor(-0x1)
        recyclerView_sign_record_frame.setHeaderView(createHeaderView())// add headerView
        recyclerView_sign_record_frame.setFooterView(createFooterView())
        recyclerView_sign_record_frame.setTargetScrollWithLayout(true)
        recyclerView_sign_record_frame
                .setOnPullRefreshListener(object : CustomeSwipeRefreshLayout.OnPullRefreshListener {

                    override fun onRefresh() {
                        mImageView?.setVisibility(View.GONE)
                        mPbView?.setVisibility(View.VISIBLE)
                        Handler().postDelayed({
                            //mPresenter.getCategoryList(id, currentPage)
                            recyclerView_sign_record_frame.setRefreshing(false)
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

        recyclerView_sign_record_frame
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
                            recyclerView_sign_record_frame.setLoadMore(false)
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

    override fun initData() {
        super.initData()
        //val map = HashMap<String, Int>(1)
        var map1 = SignRecordListEntity("2019-4-08", "正常", "10:10:03")
        var map2 = SignRecordListEntity("2019-4-09", "正常", "10:09:20")
        var map3 = SignRecordListEntity("2019-4-10", "正常", "10:03:24")
        var map4 = SignRecordListEntity("2019-4-11", "正常", "10:02:04")

        var list = mutableListOf<SignRecordListEntity>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        list.add(3, map4)
        getListSuccess(list)
        getCategorySuccess(list)
        ll_date_change.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("time_type", "in_sign_month")
            startActivity(SelectionTimeActivity::class.java, bundle)
        }
    }

    private fun getListSuccess(signList: MutableList<SignRecordListEntity>) {
        /* if (signList != null) {
             if (signList.size > 0) {
                 if (currentPage > 1) {
                     mSignRecordListAdapter?.addAll(signList)
                 } else {
                     mSignRecordListAdapter?.replaceAll(signList)
                     recyclerView_sign_record_frame.setLoadMoreEnable(false)
                 }
             } else if (currentPage > 1 && signList.size == 0) {
                 recyclerView_sign_record_frame.setLoadMoreEnable(false)
                 showToast(this, "没有更多了")
             } else if (currentPage == 1 && signList.size == 0) {
                 recyclerView_sign_record_frame.setLoadMoreEnable(false)
                 recyclerView_sign_record_frame.setVisibility(View.GONE)
                 iv_empty_order.setVisibility(View.VISIBLE)
             } else if (currentPage == 1 && signList.size > 0) {
                 recyclerView_sign_record_frame.setLoadMoreEnable(false)
                 recyclerView_sign_record_frame.setVisibility(View.GONE)
             }
         }*/
    }

    fun getCategorySuccess(data: MutableList<SignRecordListEntity>) {
        loading.setVisibility(View.GONE)
        if (data != null) {
            if (data.size > 0) {
                iv_no_data.setVisibility(View.GONE)
                if (currentPage > 1) {
                    mSignRecordListAdapter?.addAll(data)
                } else {
                    mSignRecordListAdapter?.replaceAll(data)
                    recyclerView_sign_record_frame.setRefreshing(false)
                }
            } else if (currentPage > 1 && data.size == 0) {
                recyclerView_sign_record_frame.setLoadMore(false)
                showToast("没有更多了")
            } else if (currentPage == 1 && data.size == 0) {
                recyclerView_sign_record_frame.setLoadMore(false)
                iv_no_data.setVisibility(View.VISIBLE)
            } else if (currentPage == 1 && data.size > 0) {
                recyclerView_sign_record_frame.setLoadMore(false)
            }
        }


    }

    private fun createFooterView(): View {
        val footerView = LayoutInflater.from(recyclerView_sign_record_frame.getContext())
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

    private fun createHeaderView(): View {
        val headerView = LayoutInflater.from(recyclerView_sign_record_frame.getContext())
                .inflate(R.layout.layout_head, null)
        mHeadContainer = headerView.findViewById(R.id.head_container) as RelativeLayout
        mPbView = headerView.findViewById(R.id.pb_view) as ProgressBar
        mImageView = headerView.findViewById(R.id.image_view) as ImageView
        mImageView?.setVisibility(View.VISIBLE)
        mImageView?.setImageResource(R.drawable.ptr_rotate_arrow)
        mPbView?.setVisibility(View.GONE)
        return headerView
    }

    override fun onRefresh() {
    }

    override fun onLoadMore() {
    }

    override fun selectType(s: String) {
    }

    override fun onItemClick(position: Int) {
    }

    override fun onClick(view: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        log("-------onDestroy------")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onRestart() {
        super.onRestart()
        val bundle = getIntent().extras
        if (bundle != null) {
            if (bundle.containsKey("time_type")) {
                var time_type = bundle?.getString("time_type")
                if (time_type != null && time_type.equals("in_sign_month")) {
                    var mEndDay = bundle?.getString("in_sign_end_day")
                    var mStartDay = bundle?.getString("in_sign_start_day")
                    tv_date_change.setText(mStartDay + "~" + mEndDay)
                }
            }
        }
        log(TAG + "-------onResume------")
    }
}