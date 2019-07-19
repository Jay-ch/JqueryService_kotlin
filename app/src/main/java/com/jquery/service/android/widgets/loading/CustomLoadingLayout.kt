package com.jquery.service.android.widgets.loading

import android.app.Activity
import android.view.View

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
class CustomLoadingLayout: SmartLoadingLayout {
    private var mHostActivity: Activity

    constructor(mHostActivity: Activity) : super() {
        this.mHostActivity = mHostActivity
    }
    fun setLoadingView(viewID: Int) {
        mLoadingView = mHostActivity.findViewById(viewID)
        mLoadingView.setVisibility(View.GONE)
    }

    fun setContentView(viewID: Int) {
        mContentView = mHostActivity.findViewById(viewID)
        mContentView.setVisibility(View.VISIBLE)
    }

    fun setEmptyView(viewID: Int) {
        mEmptyView = mHostActivity.findViewById(viewID)
        mEmptyView.setVisibility(View.GONE)
    }

    fun setErrorView(viewID: Int) {
        mErrorView = mHostActivity.findViewById(viewID)
        mErrorView.setVisibility(View.GONE)
    }

   override fun onLoading() {
        showViewWithStatus(LayoutStatus.Loading)
    }

  override  fun onDone() {
        showViewWithStatus(LayoutStatus.Done)
    }

   override fun onEmpty() {
        showViewWithStatus(LayoutStatus.Empty)
    }

   override fun onError() {
        showViewWithStatus(LayoutStatus.Error)
    }
}