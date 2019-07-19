package com.jquery.service.android.Base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.utils.DisplayUtils.getScreenHeight
import com.jquery.service.android.widgets.dialog.LoadingDialog
import com.jquery.service.android.widgets.dialog.SelectDialog
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.umeng.analytics.MobclickAgent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.include_title.*
import kotlinx.android.synthetic.main.toast_custom_common.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * Base基类
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
abstract class BaseActivity : RxAppCompatActivity(), BaseFunImp, BaseSetStatusBar {
    private lateinit var bind: Unbinder
    protected var mloadingDialog: LoadingDialog? = null
    private lateinit var toast: Toast
    private var toast_txt_id: Int = 0
    private var mSelectDialog: SelectDialog? = null
    private lateinit var readCardData: String
    private lateinit var mToast: Toast
    private val STATUS: Boolean = false
    private val KEY = "jQwkrom0662="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBottomUIMenu()
        if (createLayout() != 0)
            setContentView(createLayout())
        bind = ButterKnife.bind(this)
        //场景类型设置接口
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)
    }

    /**
     * 隐藏虚拟按键，并且设置成全屏
     */
    protected fun hideBottomUIMenus() {
        if (Build.VERSION.SDK_INT >= 19) {
            var decorView = getWindow().getDecorView();
            var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected fun hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            var decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    /**
     * 白色状态栏
     */
    @SuppressLint("ResourceType")
    fun setStatusBarWhite() {
        fl_title.setBackgroundResource(ContextCompat.getColor(this, R.color.c_ff))
        include_tv.setTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusBar(this)
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
    }

    /**
     * 黑色状态栏
     */
    @SuppressLint("ResourceType")
    private fun setStatusBarBlack() {
        CommonsStatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        fl_title.setBackgroundResource(ContextCompat.getColor(this,R.color.c_ff))
        include_tv.setTextColor(ContextCompat.getColor(this,R.color.c_33))
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

    open fun setTitle(title: String) {
        include_tv.text = title
        tv_back.setOnClickListener {
            finish()
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initViews()
        initData()

    }

    override fun initViews() {
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    protected abstract fun createLayout(): Int

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

    override fun onStop() {
        super.onStop()
        //toast.cancel()

    }

    override fun onDestroy() {
        bind.unbind()
        //toast.cancel()
        hideBaseLoading()
        hideSelectDialog()
        super.onDestroy()
        // 取消监听 SDK 广播
    }

    @Optional
    @OnClick(R.id.tv_back)
    open fun finishActivity() {
        this.finish()
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
    fun showToast(ctx: Context, content: String?) {
        Thread(Runnable {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                mToast = Toast(ctx)
                mToast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseActivity) / 4)//设置toast显示的位置，居中
                mToast.duration = Toast.LENGTH_SHORT//设置toast显示的时长
                val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
                mToast.view = root
                var mTvToast = root.findViewById<View>(R.id.tvCustomToast) as TextView
                mTvToast.text = content
                mToast.show()
            }
        }).start()
    }

    fun showToast(ctx: Context, stringId: Int) {
        showToast(ctx, ctx.getString(stringId))
    }

    /**
     * 取消吐司
     */
    fun cancelToast() {
        mToast.cancel()
        //mToast == null
        //mTvToast == null
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
            toast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast.getView()?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast.show()
        }

    }

    fun showToast(i: Int) {
        showToast(getString(i))
    }

    /**
     * 长吐司
     *
     * @param s
     */

    fun showLongToast(s: CharSequence) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            toast = Toast.makeText(applicationContext, s, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast.getView()?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast.show()
        }

    }

    // 打印土司
    fun showPrintMessage(ctx: Context, content: String) {
        runOnUiThread {
            mToast = Toast(ctx)
            mToast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseActivity) / 4)//设置toast显示的位置，居中
            mToast.duration = Toast.LENGTH_SHORT//设置toast显示的时长
            val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
            mToast.view = root
            tvCustomToast.text = content
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
            toast.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseActivity) / 4)
            if (toast_txt_id == 0) {
                toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
            }
            (toast.view?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
            toast.show()
        }
    }

    fun showPrintToast(i: Int) {
        showPrintToast(getString(i))
    }


    /**
     * show loading dialog
     */
    fun showBaseLoading(s: String?) {

        if (mloadingDialog == null) {
            mloadingDialog = LoadingDialog(this)
        }
        if (s == null || s.isEmpty()) {
            return
        }
        mloadingDialog?.show()
        mloadingDialog?.setMessage(s)
    }

    fun hideBaseLoading() {
        if (mloadingDialog == null) {
            return
        }
        if (mloadingDialog?.isShowing!!) {
            mloadingDialog?.dismiss()
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
    protected fun showSelectDialog(title: String, left: String, right: String, listener: SelectDialogListener) {
        showBtnDialog(title, left, right, true, listener)
    }

    /**
     * 一个按钮提示框
     */
    protected fun showSelectDialogOneBtn(title: String, yes: String, listener: SelectDialogListener) {
        showBtnDialog(title, yes, "", false, listener)
    }

    /**
     * 选择提示框
     */
    private fun showBtnDialog(title: String, left: String, right: String, b: Boolean
                              , listener: SelectDialogListener) {
        if (mSelectDialog == null) {
            mSelectDialog = SelectDialog(this, b)
        }
        mSelectDialog?.showDialog()
        mSelectDialog?.setListener(listener)
        mSelectDialog?.setCanceledOnTouchOutside(false)
        mSelectDialog?.setCancelable(false)
        mSelectDialog?.setTitle(title)
        mSelectDialog?.setLeftAndRightText(left, right)

    }

    /**
     * 隐藏选择框
     *
     * @param
     */
    open fun hideSelectDialog() {
        if (mSelectDialog == null) {
            return
        }
        mSelectDialog?.dismiss()
    }


    /**
     * 隐藏软键盘
     *
     * @param v
     */
    open fun hideSoftKeyBoard(v: View) {
        val mgr = this@BaseActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }

    /**
     * 显示软键盘
     *
     * @param v
     */
    open fun showSoftKeyBoard(v: EditText) {
        val focus = v.requestFocus()
        if (v.hasFocus()) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val mgr = this@BaseActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val result = mgr.showSoftInput(v, InputMethodManager.SHOW_FORCED)
            }
        }
    }

    /***
     * 隐藏虚拟键
     */
    fun hideNavigationBar() {
        var uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                or View.SYSTEM_UI_FLAG_FULLSCREEN) // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags = uiFlags or View.SYSTEM_UI_FLAG_IMMERSIVE//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
        } else {
            uiFlags = uiFlags or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }

        try {
            window.decorView.systemUiVisibility = uiFlags
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }


    /*
    /**
     * 数据加密
     *
     * @param dataMap
     */
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

    /*
    /**
     * 数据解码解析
     */
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