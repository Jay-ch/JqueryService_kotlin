package com.jquery.service.android.Base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.gson.Gson
import com.jquery.service.android.utils.DisplayUtils.getScreenHeight
import com.jquery.service.android.widgets.dialog.LoadingDialog
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * Base Frag 基类
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
abstract class BaseFragment : RxFragment(), BaseFunImp {
    private var bind: Unbinder? = null
    private  var mLoadingDialog: LoadingDialog?=null
    private lateinit var toast: Toast
    private var toast_txt_id: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(createLayout(), container, false)
    }


    protected abstract fun createLayout(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = ButterKnife.bind(this, view)
        initViews()
        initData()
    }

    override fun initViews() {
    }

    override fun initData() {
    }

    fun startActivity(cls: Class<*>) {
        startActivity(cls, null)
    }

    fun startActivity(cls: Class<*>, bundle: Bundle?) {
        val intent = Intent(context, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startActivityForResult(cls: Class<*>, bundle: Bundle?, code: Int) {
        val intent = Intent(context, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, code)
    }

    /**
     * @param s
     */
    fun showToast(s: CharSequence) {
        toast = Toast.makeText(context, s, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, getScreenHeight(context) / 4)
        toast.setText(s)

        if (toast_txt_id == 0)
            toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
        (toast.getView().findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
        toast.show()
    }

    /**
     * loading
     *
     * @param s
     */
    @SuppressLint("NewApi")
    fun showBaseLoading(s: String?) {
        mLoadingDialog = context?.let { LoadingDialog(it) }
        if (s != null) {
            mLoadingDialog?.setMessage(s)
        }
        mLoadingDialog?.show()
    }

    fun hideBaseLoading() {
        if (mLoadingDialog?.isShowing()!!) {
            mLoadingDialog?.dismiss()
        }
    }


    /**
     * @param flow
     * @param subscriber
     * @param <T>
    </T> */
    open fun <T> addSubscriber(flow: Flowable<T>, subscriber: BaseSubscriber<T>) {
        flow.subscribeOn(Schedulers.io())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber)
    }

    fun createBody(hashMap: HashMap<*, *>): RequestBody {
        val gson = Gson()
        val s = gson.toJson(hashMap)
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s)
    }

    /**
     * loading
     *
     * @param title
     */
   open fun mShowLoading(title: String) {
        showBaseLoading(title)
    }

    /**
     * loading
     *
     * @param title
     */
    open fun showLoading(title: String?) {
        showBaseLoading(title)
    }

    open fun hideLoading() {
        hideBaseLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bind != null) {
            bind?.unbind()
        }
    }
}