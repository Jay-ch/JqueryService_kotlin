package com.jquery.service.android.Base

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
abstract class BaseLazyFragment<T : BasePresenter<*>> : BaseFragment(), ImBaseView {

    private var isPrepared: Boolean = false
    private var isFirstVisible = true
    private var isFirstInvisible = true
    var mPresenter: T? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = createPresenter()
        if (mPresenter != null)
            mPresenter?.attachView(this)
        initPrepare()
    }

    override fun providerContext(): Context {
        return this.providerContext()
    }

    override fun showLoading(s: String?) {
        showBaseLoading(s)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }

    protected abstract fun createPresenter(): T

    @Synchronized
    fun initPrepare() {
        if (isPrepared) {
            onFirstUserVisible()
        } else {
            isPrepared = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            } else {
                onUserVisible()
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false
                onFirstUserInvisible()
            } else {
                onUserInvisible()
            }
        }
    }

    /**
     * 第一次fragment不可见
     */
    private fun onFirstUserInvisible() {

    }

    /**
     * 第一次fragment可见
     */
    fun onFirstUserVisible() {
        lazyLoad()
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    fun onUserVisible() {}

    /**
     * fragment不可见（切换掉或者onPause）
     */
    fun onUserInvisible() {}

    protected abstract fun lazyLoad()

    /**
     * loading
     *
     * @param title
     */

    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter != null)
            mPresenter?.detachView()
    }

}