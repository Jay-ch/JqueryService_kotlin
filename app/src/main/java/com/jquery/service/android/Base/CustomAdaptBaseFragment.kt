package com.jquery.service.android.Base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
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
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.internal.CustomAdapt
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * 适配刘海屏的baseFragment基类
 * @author J.query
 * @date 2019/3/15
 * @email j-query@foxmail.com
 */
abstract class CustomAdaptBaseFragment<T : BasePresenter<*>> : RxFragment(), ImBaseView, BaseFunImp, CustomAdapt {
    lateinit var mPresenter: T
    private var bind: Unbinder? = null
    protected lateinit var mLoadingDialog: LoadingDialog
    private lateinit var toast: Toast
    private var toast_txt_id: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        AutoSize.autoConvertDensity(activity!!, 1080f, true)
        return inflater.inflate(createLayout(), container, false)
    }

    protected abstract fun createLayout(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //AutoSizeConfig.getInstance().setCustomFragment(true);
        super.onViewCreated(view, savedInstanceState)
        mPresenter = createPresenter()
        mPresenter.attachView(this)
        bind = ButterKnife.bind(this, view!!)
        initViews()
        initData()
    }

    override fun initData() {

    }

    override fun initViews() {

    }

    protected abstract fun createPresenter(): T

    override fun providerContext(): Context {
        return this.providerContext()
    }

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return `true` 为按照宽度进行适配, `false` 为按照高度进行适配
     */
    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 667f
    }

    /**
     * 获取屏幕宽度
     */
    val screenWidth: Int
        get() {
            val dm = DisplayMetrics()
            /* Display display = getActivity().getWindowManager().getDefaultDisplay();
             Point size = new Point();
             display.getSize(size);
             int widths = size.x;
             int height = size.y;*/
            getActivity()?.getWindowManager()?.defaultDisplay?.getMetrics(dm)
            return dm.widthPixels
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
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, getScreenHeight(context) / 4)
        } else {
            toast.setText(s)
        }
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
    fun showBaseLoading(s: String?) {
        mLoadingDialog = LoadingDialog(this!!.context!!)
        if (s != null && !s.isEmpty())
            mLoadingDialog.setMessage(s)
        mLoadingDialog.show()
    }

    fun hideBaseLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss()
        }
    }

    /**
     * @param flow
     * @param subscriber
     * @param <T>
    </T> */
    fun <T> addSubscriber(flow: Flowable<T>, subscriber: BaseSubscriber<T>) {
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

    override fun showLoading(s: String?) {
        showBaseLoading(s)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bind != null) {
            bind?.unbind()
        }
        if (mPresenter != null)
            mPresenter.detachView()
    }
}