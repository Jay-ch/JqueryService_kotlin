package com.jquery.service.android.Base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.jquery.service.android.R
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.utils.DisplayUtils.getScreenHeight
import com.jquery.service.android.widgets.dialog.LoadingDialog
import com.jquery.service.android.widgets.dialog.SelectDialog
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import com.umeng.analytics.MobclickAgent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*

/**
 * @author J.query
 * @date 2018/9/12
 * @email j-query@foxmail.com
 */
abstract class BaseFragmentActivity : RxFragmentActivity(), BaseFunImp {

    private val mLoadingDialog: LoadingDialog? = null
    private val TAG = "BaseActivity"
    private var bind: Unbinder? = null
    protected var mloadingDialog: LoadingDialog? = null
    private var toast: Toast? = null
    private var toast_txt_id: Int = 0
    private lateinit var mSelectDialog: SelectDialog
    private var mToast: Toast? = null
    private var mTvToast: TextView? = null
    private val STATUS: Boolean = false
    private val KEY = "jQwkrom0662="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (createLayout() != 0)
            setContentView(createLayout())
        bind = ButterKnife.bind(this)
    }

    fun setTitle(title: String) {
        val tvTitle = findViewById(R.id.include_tv) as TextView
        tvTitle.text = title
        val tvBack = findViewById(R.id.tv_back) as TextView
        tvBack.setOnClickListener {
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
        if (toast != null) {
            toast?.cancel()
        }
    }

    override fun onDestroy() {
        if (bind != null)
            bind?.unbind()
        if (toast != null) {
            toast?.cancel()
        }
        if (mloadingDialog != null) {
            mloadingDialog?.dismiss()
        }
        super.onDestroy()
    }

    @Optional
    @OnClick(R.id.tv_back)
    fun finishActivity() {
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
    fun showToast(ctx: Context, content: String) {
        if (mToast == null) {
            mToast = Toast(ctx)
            mToast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFragmentActivity) / 4)//设置toast显示的位置，居中
            mToast?.duration = Toast.LENGTH_SHORT//设置toast显示的时长
            val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
            mTvToast = root.findViewById(R.id.tvCustomToast)
            mToast?.view = root
        }
        mTvToast?.text = content
        mToast?.show()
    }

    fun showToast(ctx: Context, stringId: Int) {
        showToast(ctx, ctx.getString(stringId))
    }

    /**
     * 取消吐司
     */
    fun cancelToast() {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
            mTvToast = null
        }
    }

    /**
     * 只弹一次吐司
     *
     * @param s
     */
    fun showToast(s: CharSequence) {
        if (toast == null) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                toast = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
                toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFragmentActivity) / 4)
                if (toast_txt_id == 0) {
                    toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
                }
                (toast?.view?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
                toast?.show()
            }
        } else {
            toast?.setText(s)
        }
    }

    fun showToast(i: Int) {
        showToast(getString(i))
    }


    fun showLongToast(s: CharSequence) {
        if (toast == null) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                toast = Toast.makeText(applicationContext, s, Toast.LENGTH_LONG)
                toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFragmentActivity) / 4)
                if (toast_txt_id == 0) {
                    toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
                }
                (toast?.view?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
                toast?.show()
            }
        } else {
            toast?.setText(s)
        }
    }

    /*  // 显示操作返回的信息
    public void showMessage(final String mess) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (messageType) {
                    case MessageTag.NORMAL:
                        message = "<font color='black'>" + mess + "</font>";
                        break;
                    case MessageTag.ERROR:
                        message = "<font color='red'>" + mess + "</font>";
                        break;
                    case MessageTag.TIP:
                        message = "<font color='green'>" + mess + "</font>";
                        break;
                    case MessageTag.DATA:
                        message = "<font color='blue'>" + mess + "</font>";
                        break;
                    case MessageTag.WARN:
                        message = "<u><font color='red'>" + mess + "</font></u>";
                        break;
                    default:
                        break;
                }
                newMessage = message + "<br>" + newMessage;
                tvOperationMessage.setText(Html.fromHtml(newMessage, getImageGetter(), null));
            }
        });
    }*/

    // 打印土司
    fun showPrintMessage(ctx: Context, content: String) {
        runOnUiThread {
            if (mToast == null) {
                mToast = Toast(ctx)
                mToast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFragmentActivity) / 4)//设置toast显示的位置，居中
                mToast?.duration = Toast.LENGTH_SHORT//设置toast显示的时长
                val root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null)//自定义样式，自定义布局文件
                mTvToast = root.findViewById(R.id.tvCustomToast)
                mToast?.view = root
            }
            mTvToast?.text = content
            mToast?.show()
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
        if (toast == null) {
            runOnUiThread {
                toast = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
                toast?.setGravity(Gravity.CENTER, 0, getScreenHeight(this@BaseFragmentActivity) / 4)
                if (toast_txt_id == 0) {
                    toast_txt_id = Resources.getSystem().getIdentifier("message", "id", "android")
                }
                (toast?.view?.findViewById<View>(toast_txt_id) as TextView).gravity = Gravity.CENTER
                toast?.show()
            }
        } else {
            toast?.setText(s)
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
        if (s != null && !s.isEmpty())
            mloadingDialog?.setMessage(s)
        mloadingDialog?.show()
    }

    fun hideBaseLoading() {
        if (mloadingDialog != null && mloadingDialog?.isShowing()!!) {
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
    protected fun showSelectDialog(title: String, left: String, right: String) {

        mSelectDialog = SelectDialog(this, true)
        mSelectDialog.setListener(object : SelectDialogListener {
            override fun leftClick() {}

            override fun rightClick() {

            }
        })
        mSelectDialog.setCanceledOnTouchOutside(false)
        mSelectDialog.setCancelable(false)
        mSelectDialog.setTitle(title)
        mSelectDialog.setLeftAndRightText(left, right)
        mSelectDialog.showDialog()
    }

    /**
     * 隐藏选择框
     *
     * @param
     */
    fun hideSelectDialog() {
        if (mSelectDialog.isShowing()) {
            mSelectDialog?.dismiss()
        }
    }


    /**
     * 隐藏软键盘
     *
     * @param v
     */
    fun hideSoftKeyBoard(v: View) {
        val mgr = this@BaseFragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                val mgr = this@BaseFragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val result = mgr.showSoftInput(v, InputMethodManager.SHOW_FORCED)
            }
        }
    }
}