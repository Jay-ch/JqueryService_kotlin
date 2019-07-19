package com.jquery.service.android.widgets.loading

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.jquery.service.android.R
import com.jquery.service.android.utils.DisplayUtils.dip2px

/**
 * @author J.query
 * @date 2019/1/24
 * @email j-query@foxmail.com
 */
class DefaultLoadingLayout : SmartLoadingLayout {
    private val mInflater: LayoutInflater
    private val mContext: Context
    private var rlAddedView: RelativeLayout? = null
    private var mAnyAdded: Boolean = false
    private var mLayoutParams: RelativeLayout.LayoutParams? = null

    private var tvLoadingDescription: TextView? = null
    private var tvEmptyDescription: TextView? = null
    private var tvErrorDescription: TextView? = null
    private var btnErrorHandle: TextView? = null
    private val mLoadingContent: LinearLayout? = null

    constructor(context: Context, contentView: View) : super() {
        this.mContext = context
        this.mContentView = contentView
        this.mInflater = LayoutInflater.from(context)
        run {
            mLoadingView = mInflater.inflate(R.layout.smartloadinglayout_view_on_loading, null)
            mEmptyView = mInflater.inflate(R.layout.smartloadinglayout_view_on_empty, null)
            mErrorView = mInflater.inflate(R.layout.smartloadinglayout_view_on_error, null)
            mLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            mLayoutParams!!.addRule(RelativeLayout.CENTER_IN_PARENT)
        }
    }

    fun setLoadingDescriptionColor(color: Int) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = mLoadingView?.findViewById(R.id.tv_loading_message) as TextView

        tvLoadingDescription!!.setTextColor(color)
    }

    fun setLoadingDescriptionTextSize(size: Float) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = mLoadingView?.findViewById(R.id.tv_loading_message) as TextView

        tvLoadingDescription!!.textSize = size
    }

    fun setLoadingDescription(loadingDescription: String) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = mLoadingView?.findViewById(R.id.tv_loading_message) as TextView

        tvLoadingDescription!!.text = loadingDescription
    }

    fun setLoadingDescription(resID: Int) {
        if (tvLoadingDescription == null)
            tvLoadingDescription = mLoadingView?.findViewById(R.id.tv_loading_message) as TextView

        tvLoadingDescription!!.setText(resID)
    }

    fun replaceLoadingProgress(view: View) {
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.CENTER_IN_PARENT)
        (mLoadingView as RelativeLayout).addView(view, lp)
        (mLoadingView as RelativeLayout).removeView(mLoadingContent)
    }

    fun setEmptyDescriptionColor(color: Int) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        tvEmptyDescription!!.setTextColor(color)
    }

    fun setEmptyDescriptionTextSize(size: Float) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        tvEmptyDescription!!.textSize = size
    }

    fun setEmptyDescription(emptyDescription: String) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        tvEmptyDescription!!.text = emptyDescription
    }

    fun setEmptyDescription(resID: Int) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        tvEmptyDescription!!.setText(resID)
    }

    fun replaceEmptyIcon(newIcon: Drawable) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        newIcon.setBounds(0, 0, newIcon.minimumWidth, newIcon.minimumHeight)
        tvEmptyDescription!!.setCompoundDrawables(null, newIcon, null, null)
    }

    fun replaceEmptyIcon(resId: Int) {
        if (tvEmptyDescription == null)
            tvEmptyDescription = mEmptyView?.findViewById(R.id.tv_empty_message) as TextView

        val newIcon = mContext.resources.getDrawable(resId)
        newIcon.setBounds(0, 0, newIcon.minimumWidth, newIcon.minimumHeight)
        tvEmptyDescription!!.setCompoundDrawables(null, newIcon, null, null)
    }

    fun setErrorDescriptionColor(color: Int) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView?.findViewById(R.id.tv_error_message) as TextView

        tvErrorDescription!!.setTextColor(color)
    }

    fun setErrorDescriptionTextSize(size: Float) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView.findViewById(R.id.tv_error_message) as TextView

        tvErrorDescription!!.textSize = size
    }

    fun setErrorDescription(errorDescription: String) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView?.findViewById(R.id.tv_error_message) as TextView

        tvErrorDescription!!.text = errorDescription
    }

    fun setErrorDescription(resID: Int) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView?.findViewById(R.id.tv_error_message) as TextView

        tvErrorDescription!!.setText(resID)
    }

    fun setErrorButtonListener(listener: View.OnClickListener) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.setOnClickListener(listener)
    }


    fun setErrorButtonBackground(background: Drawable) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btnErrorHandle!!.background = background
        } else {

            btnErrorHandle!!.setBackgroundDrawable(background)
        }
    }

    fun setErrorButtonBackground(resID: Int) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.setBackgroundResource(resID)

    }

    fun setErrorButtonTextColor(color: Int) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.setTextColor(color)
    }

    fun setErrorButtonText(text: String) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.text = text
    }

    fun setErrorButtonText(resID: Int) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.setText(resID)
    }

    fun hideErrorButton() {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        btnErrorHandle!!.visibility = View.GONE
    }

    fun replaceErrorButton(newButton: Button) {
        if (btnErrorHandle == null)
            btnErrorHandle = mErrorView?.findViewById(R.id.btn_error) as TextView

        (mErrorView as RelativeLayout).removeView(btnErrorHandle)
        btnErrorHandle = newButton
        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL)
        lp.addRule(RelativeLayout.BELOW, R.id.tv_error_message)
        lp.topMargin = dip2px(mContext, 10F)
        (mErrorView as RelativeLayout).addView(btnErrorHandle, lp)
    }

    fun replaceErrorIcon(newIcon: Drawable) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView?.findViewById(R.id.tv_error_message) as TextView

        newIcon.setBounds(0, 0, newIcon.minimumWidth, newIcon.minimumHeight)
        tvErrorDescription!!.setCompoundDrawables(null, newIcon, null, null)
    }


    fun replaceErrorIcon(resId: Int) {
        if (tvErrorDescription == null)
            tvErrorDescription = mErrorView?.findViewById(R.id.tv_error_message) as TextView

        val newIcon = mContext.resources.getDrawable(resId)
        newIcon.setBounds(0, 0, newIcon.minimumWidth, newIcon.minimumHeight)
        tvErrorDescription!!.setCompoundDrawables(null, newIcon, null, null)
    }

    fun setLayoutBackgroundColor(color: Int) {
        initAddedLayout()
        rlAddedView!!.setBackgroundColor(color)
    }

    fun setLayoutBackground(resID: Int) {
        initAddedLayout()
        rlAddedView!!.setBackgroundResource(resID)
    }

    override fun onLoading() {
        checkContentView()
        if (!mLoadingAdded) {
            initAddedLayout()

            if (mLoadingView != null) {
                rlAddedView!!.addView(mLoadingView, mLayoutParams)
                mLoadingAdded = true
            }
        }
        showViewWithStatus(LayoutStatus.Loading)
    }

    override fun onDone() {
        checkContentView()
        showViewWithStatus(LayoutStatus.Done)
    }

    override fun onEmpty() {
        checkContentView()
        if (!mEmptyAdded) {
            initAddedLayout()

            if (mEmptyView != null) {
                rlAddedView!!.addView(mEmptyView, mLayoutParams)
                mEmptyAdded = true
            }
        }
        showViewWithStatus(LayoutStatus.Empty)
    }

    override fun onError() {
        checkContentView()
        if (!mErrorAdded) {
            initAddedLayout()
            if (mErrorView != null) {
                rlAddedView!!.addView(mErrorView, mLayoutParams)
                mErrorAdded = true
            }
        }
        showViewWithStatus(LayoutStatus.Error)
    }

    private fun checkContentView() {
        if (mContentView == null)
            throw NullPointerException("The content view not set..")
    }

    private fun initAddedLayout() {
        if (!mAnyAdded) {
            rlAddedView = RelativeLayout(mContext)
            rlAddedView!!.layoutParams = mLayoutParams
            val parent = mContentView?.getParent() as ViewGroup
            parent.addView(rlAddedView)
            mAnyAdded = true
        }
    }
}