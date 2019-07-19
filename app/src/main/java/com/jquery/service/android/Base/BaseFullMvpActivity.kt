package com.jquery.service.android.Base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import butterknife.Unbinder
import com.google.gson.Gson
import com.gyf.barlibrary.ImmersionBar
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.utils.DisplayUtils.getScreenHeight
import com.jquery.service.android.utils.StatusBarUtils
import com.jquery.service.android.widgets.dialog.LoadingDialog
import com.jquery.service.android.widgets.dialog.SelectDialog
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.umeng.analytics.MobclickAgent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * Mvp Activity
 * @author J.query
 * @date 2019/1/22
 * @email j-query@foxmail.com
 */
abstract class BaseFullMvpActivity<P : BasePresenter<*>> : RxAppCompatActivity(), ImBaseView, BaseFunImp {

    protected val tag = this.javaClass.simpleName

    lateinit var mPresenter: P
    private lateinit var bind: Unbinder
    protected lateinit var mLoadingDialog: LoadingDialog
    private var toast: Toast? = null
    private var toast_txt_id: Int = 0
    private lateinit var mSelectDialog: SelectDialog
    private val mRsltrslt: Int = 0
    internal var psw = ""
    internal var adress = "0"
    internal var dataLen = "512"
    private lateinit var readCardData: String
    private lateinit var mToast: Toast
    private lateinit var mTvToast: TextView
    private val status: Boolean = false
    private val KEY = "jQwkrom0662="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (createLayout() != 0)
            setContentView(createLayout())
        //setTitleBarHight()
        //initStatusBar()
        mPresenter = createPresenter()
        mPresenter.attachView(this)
        bind = ButterKnife.bind(this)
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)//场景类型设置接口。

    }

    /**
     * 设置状态栏高度
     */
    private fun setTitleBarHight() {
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        val window = window
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
    }
    open fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            StatusBarUtils.setStatusBarColor(this, R.color.c_ff)
        }
    }
    /**
     * 获取屏幕宽度
     */
    val screenWidth: Int
        get() {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

    override fun providerContext(): Context {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        //mPresenter.detachView()
        if (mPresenter != null)
            mPresenter.detachView()
    }

    abstract fun createLayout(): Int

    abstract fun createPresenter(): P


    fun setTitle(title: String) {
        val tvTitle = findViewById(R.id.include_tv) as TextView
        tvTitle.text = title
        val tvBack = findViewById(R.id.tv_back) as TextView
        tvBack.setOnClickListener(View.OnClickListener { finish() })
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initViews()
        initData()
    }

    override fun initData() {
    }

    override fun initViews() {
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    /**
     * @param flow
     * @param subscriber
     * @param <T>
    </T> */
    fun <T> addSubscriber(flow: Flowable<T>, subscriber: BaseSubscriber<T>) {
        flow.subscribeOn(Schedulers.io())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber)
    }

    fun createBody(hashMap: HashMap<*, *>): RequestBody {
        val gson = Gson()
        val s = gson.toJson(hashMap)
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), s)
    }

    @Optional
    @OnClick(R.id.tv_back)
    fun finishActivity() {
        this.finish()
    }

    override fun showLoading(s: String?) {
        showBaseLoading(s)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }

    fun startActivity(cls: Class<*>) {
        startActivity(cls, null)
    }

    fun startActivity(cls: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startActivityForResult(cls: Class<*>, bundle: Bundle?, code: Int) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, code)
    }


    /**
     * 吐司
     *
     * @param ctx
     * @param content
     */
    fun showToast(ctx: Context, content: String) {
        mToast = Toast(ctx)
        mToast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFullMvpActivity) / 4)//设置toast显示的位置，居中
        mToast.setDuration(Toast.LENGTH_SHORT)//设置toast显示的时长
        val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
        mTvToast = root.findViewById(R.id.tvCustomToast) as TextView
        mToast.setView(root)

        mTvToast.setText(content)
        mToast.show()
    }

    fun showToast(ctx: Context, stringId: Int) {
        showToast(ctx, ctx.getString(stringId))
    }

    /**
     * 取消吐司
     */
    fun cancelToast() {
        mToast.cancel()
        mToast == null
        mTvToast == null
    }

    /**
     * 只弹一次吐司
     *
     * @param s
     */
    fun showToast(s: CharSequence) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            toast = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
            toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFullMvpActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast?.getView()?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast?.show()
        }

    }

    fun showToast(i: Int) {
        showToast(getString(i))
    }


    fun showLongToast(s: CharSequence) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            toast = Toast.makeText(applicationContext, s, Toast.LENGTH_LONG)
            toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFullMvpActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast?.getView()?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast?.show()
        }

    }

    // 打印土司
    fun showPrintMessage(ctx: Context, content: String) {
        runOnUiThread {
            if (mToast == null) {
                mToast = Toast(ctx)
                mToast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFullMvpActivity) / 4)//设置toast显示的位置，居中
                mToast.setDuration(Toast.LENGTH_SHORT)//设置toast显示的时长
                val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
                mTvToast = root.findViewById(R.id.tvCustomToast) as TextView
                mToast.setView(root)
            }
            mTvToast.setText(content)
            mToast.show()
        }
    }

    fun showPrintMessage(ctx: Context, stringId: Int) {
        showPrintMessage(ctx, ctx.getString(stringId))
    }

    /**
     * 只弹一次吐司
     *
     * @param s
     */
    fun showPrintToast(s: CharSequence) {
        runOnUiThread {
            toast = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
            toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFullMvpActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast?.getView()?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast?.show()
        }
    }

    fun showPrintToast(i: Int) {
        showPrintToast(getString(i))
    }


    /**
     * show loading dialog
     */
    fun showBaseLoading(s: String?) {
        mLoadingDialog = LoadingDialog(this)
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
     * logger
     *
     * @param tag
     * @param s
     */
    fun log(tag: String, s: String) {
        Log.d(tag, s)
    }

    fun log(s: String) {
        Log.d("J.query", s)
    }

    /**
     * 选择提示框
     */
    protected fun showSelectDialog(title: String, left: String, right: String) {
        if (mSelectDialog == null) {
            mSelectDialog = SelectDialog(this)
            mSelectDialog.setListener(object : SelectDialogListener {
                override fun leftClick() {

                }

                override fun rightClick() {

                }
            })

            mSelectDialog.setCanceledOnTouchOutside(false)
            mSelectDialog.setCancelable(false)
            mSelectDialog.setTitle(title)
            mSelectDialog.setLeftAndRightText(left, right)
        }
        mSelectDialog.showDialog()
    }

    /**
     * 隐藏选择框
     *
     * @param
     */
    fun hideSelectDialog() {
        if (mSelectDialog != null) {
            mSelectDialog.dismiss()
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    fun hideSoftKeyBoard(v: View) {
        val mgr = this@BaseFullMvpActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }

    /**
     * 显示软键盘
     *
     * @param v
     */
    fun showSoftKeyBoard(v: EditText) {
        val focus = v.requestFocus()
        if (v.hasFocus()) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val mgr = this@BaseFullMvpActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val result = mgr.showSoftInput(v, InputMethodManager.SHOW_FORCED)
            }
        }
    }

    /*   */
    /**
     * 数据加密
     *
     * @param dataMap
     *//*
    fun getDataEncryption(dataMap: Map<String, String>): String? {
        var data: String? = null
        try {
            var str = Gson().toJson(dataMap)
            str = MACCoder.send(str)
            val sStr = DESUtil.encrypt(str, KEY)
            data = URLEncoder.encode(sStr, "utf-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }*/

    /* */
    /**
     * 数据解码解析
     *//*
    fun getDataDecodingAndParsing(DecodingAndParsingData: String): String {
        var DecodingAndParsingData = DecodingAndParsingData
        try {
            val entity = URLDecoder.decode(DecodingAndParsingData, "utf-8")
            val mStr = DESUtil.decrypt(entity, KEY)
            val mStrEntity = URLDecoder.decode(mStr, "utf-8")
            val ans = MACCoder.dataForCheck(mStrEntity)
            if (!ans.isEmpty()) {
                DecodingAndParsingData = ans
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.e(TAG, "getDataDecodingAndParsing:数据解码解析 $DecodingAndParsingData")
        return DecodingAndParsingData
    }*/

}