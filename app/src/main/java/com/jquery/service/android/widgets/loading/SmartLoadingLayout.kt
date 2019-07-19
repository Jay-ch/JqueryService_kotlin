package com.jquery.service.android.widgets.loading

import android.app.Activity
import android.content.Context
import android.view.View

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
open class SmartLoadingLayout {

    protected fun createCustomLayout(hostActivity: Activity): CustomLoadingLayout {
        return CustomLoadingLayout(hostActivity)
    }

    fun createDefaultLayout(hostActivity: Activity, contentView: View): DefaultLoadingLayout {
        return DefaultLoadingLayout(hostActivity, contentView)
    }

    fun createDefaultLayout(hostActivity: Context, contentView: View): DefaultLoadingLayout {
        return DefaultLoadingLayout(hostActivity, contentView)
    }

    protected lateinit var mLoadingView: View
    protected lateinit var mContentView: View
    protected lateinit var mEmptyView: View
    protected lateinit var mErrorView: View
    protected var mLoadingAdded: Boolean = false
    protected var mEmptyAdded: Boolean = false
    protected var mErrorAdded: Boolean = false

    open fun onLoading() {}

    open fun onDone() {}

    open fun onEmpty() {}

    open fun onError() {}

    protected fun showViewWithStatus(status: LayoutStatus) {
        when (status) {
            SmartLoadingLayout.LayoutStatus.Loading -> {
                if (mLoadingView == null) {
                    throw NullPointerException("The loading view with this layout was not set")
                }
                mLoadingView.visibility = View.VISIBLE
                if (mContentView != null)
                    mContentView.visibility = View.GONE
                if (mEmptyView != null)
                    mEmptyView.visibility = View.GONE
                if (mErrorView != null)
                    mErrorView.visibility = View.GONE
            }
            SmartLoadingLayout.LayoutStatus.Done -> {
                if (mContentView == null) {
                    throw NullPointerException("The loading view with this layout was not set")
                }
                mContentView.visibility = View.VISIBLE
                if (mLoadingView != null)
                    mLoadingView.visibility = View.GONE
                if (mEmptyView != null)
                    mEmptyView.visibility = View.GONE
                if (mErrorView != null)
                    mErrorView.visibility = View.GONE
            }
            SmartLoadingLayout.LayoutStatus.Empty -> {
                if (mEmptyView == null) {
                    throw NullPointerException("The loading view with this layout was not set")
                }
                mEmptyView.visibility = View.VISIBLE
                if (mLoadingView != null)
                    mLoadingView.visibility = View.GONE
                if (mContentView != null)
                    mContentView.visibility = View.GONE
                if (mErrorView != null)
                    mErrorView.visibility = View.GONE
            }
            SmartLoadingLayout.LayoutStatus.Error -> {
                if (mErrorView == null) {
                    throw NullPointerException("The loading view with this layout was not set")
                }
                mErrorView.visibility = View.VISIBLE
                if (mLoadingView != null)
                    mLoadingView.visibility = View.GONE
                if (mContentView != null)
                    mContentView.visibility = View.GONE
                if (mEmptyView != null)
                    mEmptyView.visibility = View.GONE
            }
            else -> {

            }
        }
    }


    protected enum class LayoutStatus {
        Loading, Done, Empty, Error
    }
}