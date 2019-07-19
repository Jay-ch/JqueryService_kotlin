package com.jquery.service.android.Base

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * @author J.query
 * @date 2019/1/22
 * @email j-query@foxmail.com
 */
abstract class BasePresenter<T : ImBaseView> : ImBasePresenter {
    private lateinit var mView: WeakReference<T>
    private var disposable: CompositeDisposable? = CompositeDisposable()

    override fun attachView(view: ImBaseView) {
        mView = WeakReference(view as T)
    }

    override fun detachView() {
        if (mView != null) {
            mView?.clear()
            mView == null
        }
        if (disposable != null && !disposable!!.isDisposed) {
            disposable?.clear()
            disposable = null
        }
    }

    /**
     * 事件源统一管理
     *
     * @param d
     */
    fun addDisposable(d: Disposable?) {
        if (disposable != null && d != null)
            disposable!!.add(d)
    }


    /**
     * 切换线程
     *
     * @param flow
     * @param subscriber
     */
    fun <T> addSubscriber(flow: Flowable<T>, subscriber: BaseSubscriber<T>) {
        addDisposable(flow.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber))
    }

    /**
     * 获取当前view
     *
     * @return
     */
    fun getView(): T? {
        return if (mView != null) mView?.get() else null
    }
}