package com.jquery.service.android.Base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author J.query
 * @date 2019/3/22
 * @email j-query@foxmail.com
 */
abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), ImBaseView {
    lateinit var mPresenter: T

    override fun providerContext(): Context {
        return this.providerContext()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        if (mPresenter != null)
            mPresenter.attachView(this)

        super.onViewCreated(view, savedInstanceState)

    }

    protected abstract fun createPresenter(): T

    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter != null)
            mPresenter.detachView()
    }

    /**
     * @param flow
     * @param subscriber
     * @param <T>
    </T> */
    override fun <T> addSubscriber(flow: Flowable<T>, subscriber: BaseSubscriber<T>) {
        flow.subscribeOn(Schedulers.io())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber)
    }

    /**
     * loading
     *
     * @param title
     */
    override fun showLoading(s: String?) {
        showBaseLoading(s)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }
}