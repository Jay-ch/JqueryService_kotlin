package com.jquery.service.android.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.jquery.service.android.R
import java.util.*


/**
 * @author J.query
 * @date 2018/9/12
 * @email j-query@foxmail.com
 */
@SuppressWarnings("unused")
class MultipleStatusView : RelativeLayout {
    private val TAG = "MultipleStatusView"
    private val DEFAULT_LAYOUT_PARAMS = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT)

    val STATUS_CONTENT = 0x00
    val STATUS_LOADING = 0x01
    val STATUS_EMPTY = 0x02
    val STATUS_ERROR = 0x03
    val STATUS_NO_NETWORK = 0x04

    private val NULL_RESOURCE_ID = -1

    private lateinit var mEmptyView: View
    private lateinit var mErrorView: View
    private lateinit var mLoadingView: View
    private lateinit var mNoNetworkView: View
    private lateinit var mContentView: View
    private var mEmptyViewResId: Int = 0
    private var mErrorViewResId: Int = 0
    private var mLoadingViewResId: Int = 0
    private var mNoNetworkViewResId: Int = 0
    private var mContentViewResId: Int = 0

    private var mViewStatus: Int = 0
    private  var mInflater: LayoutInflater?=null
    private lateinit var mOnRetryClickListener: View.OnClickListener

    ///private const val mOtherIds = ArrayList(Int)
    //List<String>lists = new ArrayList<>();
    // private var ArrayList ArrayList()
    private val mOtherIds = ArrayList<Int>()
    //private fun <E> ArrayList<E>.add(element: Int)
    //private val mOtherIds = ArrayList()
    //private final ArrayList<Integer> mOtherIds = new ArrayList<>();

    constructor(context: Context?) : super(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0)
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.empty_view)
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.error_view)
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.loading_view)
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.no_network_view)
        mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID)
        a.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    /*  fun MultipleStatusView(context: Context): ??? {
          this(context, null)
      }

      fun MultipleStatusView(context: Context, attrs: AttributeSet?): ??? {
          this(context, attrs, 0)
      }

      fun MultipleStatusView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): ??? {
          super(context, attrs, defStyleAttr)
          val a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0)
          mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.empty_view)
          mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.error_view)
          mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.loading_view)
          mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.no_network_view)
          mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID)
          a.recycle()
          mInflater = LayoutInflater.from(getContext())
      }*/

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(this.mEmptyView, this.mLoadingView, this.mErrorView, this.mNoNetworkView)
        if (null != mOtherIds) {
            mOtherIds.clear()
        }
        if (null != mOnRetryClickListener) {
            mOnRetryClickListener === null
        }
        mInflater == null
        //mInflater ===null
    }

    /**
     * 获取当前状态
     */
    fun getViewStatus(): Int {
        return mViewStatus
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: View.OnClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 显示空视图
     */
    fun showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showEmpty(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showEmpty(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showEmpty(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Empty view is null!")
        mViewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = view
            val emptyRetryView = mEmptyView.findViewById<View>(R.id.empty_retry_view)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView.id)
            addView(mEmptyView, 0, layoutParams)
        }
        showViewById(mEmptyView.id)
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showError(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showError(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Error view is null!")
        mViewStatus = STATUS_ERROR
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView.findViewById<View>(R.id.error_retry_view)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView.id)
            addView(mErrorView, 0, layoutParams)
        }
        showViewById(mErrorView.id)
    }

    /**
     * 显示加载中视图
     */
    fun showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showLoading(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showLoading(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Loading view is null!")
        mViewStatus = STATUS_LOADING
        if (null == mLoadingView) {
            mLoadingView = view
            mOtherIds.add(mLoadingView.id)
            addView(mLoadingView, 0, layoutParams)
        }
        showViewById(mLoadingView.id)
    }

    /**
     * 显示无网络视图
     */
    fun showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "No network view is null!")
        mViewStatus = STATUS_NO_NETWORK
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView.findViewById<View>(R.id.no_network_retry_view)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView.id)
            addView(mNoNetworkView, 0, layoutParams)
        }
        showViewById(mNoNetworkView.id)
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        mViewStatus = STATUS_CONTENT
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater!!.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (mOtherIds.contains(view.id)) View.GONE else View.VISIBLE
        }
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater!!.inflate(layoutId, null)
    }

    private fun showViewById(viewId: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.visibility = if (view.id == viewId) View.VISIBLE else View.GONE
        }
    }

    private fun checkNull(`object`: Any?, hint: String) {
        if (null == `object`) {
            throw NullPointerException(hint)
        }
    }

    private fun clear(vararg views: View) {
        if (null == views) {
            return
        }
        try {
            for (view in views) {
                if (null != view) {
                    removeView(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

