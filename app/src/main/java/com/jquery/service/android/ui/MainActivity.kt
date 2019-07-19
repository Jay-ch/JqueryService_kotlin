package com.jquery.service.android.ui


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.pgyersdk.update.DownloadFileListener
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import com.jquery.service.android.Base.BaseFragment
import com.jquery.service.android.Base.BaseFullMvpActivity
import com.jquery.service.android.R
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.permission.PermissionUtil
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import com.jquery.service.android.ui.home.view.FaultActivity
import com.jquery.service.android.ui.home.view.HomeFragment
import com.jquery.service.android.ui.home.view.MineHomeFragment
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.UpgradeProgressDialog
import com.jquery.service.android.widgets.dialog.SelectDialog
import com.jquery.service.utils.FileUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


/**
 * Main主页
 * @author J.query
 * @date 2019/3/7
 * @email j-query@foxmail.com
 */
class MainActivity : BaseFullMvpActivity<HomePresenter>(), HomeContract.HomeView, View.OnClickListener {
    var TAG: String? = "MainActivity"

    var mHomeFragment: BaseFragment? = null
    var mFaultFragment: BaseFragment? = null
    var mMineFragment: BaseFragment? = null
    var mExitTime: Long = 0
    var mToast: Toast? = null

    lateinit var searchFragment: SearchFragment
    private val LOCATION_PERMISSION = 202
    private var mColor: Int = 0
    private var mAlpha: Int = 0
    private var mSelectDialog: SelectDialog? = null
    private var mUpgradeProgressDialog: UpgradeProgressDialog? = null

    override fun getUserInfoSuccess(result: UserInfoResult) {
    }


    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRadioButton()
        initToolbar()
        initFragment(savedInstanceState)
    }

    private fun initToolbar() {
        top_title.setLeftVisible(false)
    }

    override fun createLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        super.initViews()
        setHomeFragmentStatusBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions()
        }
    }

    override fun initData() {
        super.initData()
        loadUpgradeInfo()
    }

    private fun loadUpgradeInfo() {
        /** 新版本  */
        PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk
                .setUpdateManagerListener(object : UpdateManagerListener {
                    override fun onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.e(TAG, "loadUpgradeInfo: 无升级信息")
                        //showToast(this@MainActivity, getString(R.string.no_version))

                    }

                    override fun onUpdateAvailable(appBean: AppBean) {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.versionCode)

                        UpgradeProgressDialog(appBean.releaseNote, appBean.downloadURL,appBean.versionName)
                    }

                    override fun checkUpdateFailed(e: Exception) {
                        //更新检测失败回调
                        Log.e("pgyer", "check update failed ", e)
                        if (e.toString().contains("net work")) {
                            showToast(this@MainActivity, getString(R.string.network_error))
                        } else {
                            showToast(this@MainActivity, getString(R.string.check_update_failed))
                        }
                    }
                })
                .setDownloadFileListener(object : DownloadFileListener {
                    override fun onProgressUpdate(vararg integers: Int?) {
                        Log.e("pgyer", "update download apk progress : " + integers[0])
                        mUpgradeProgressDialog?.setProgress(integers[0])
                        Log.e(TAG, "onChange:进度条变化 " + integers[0])
                        if (integers[0] == 100) {
                            mUpgradeProgressDialog?.dismiss()
                        }
                    }

                    override fun downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed")
                        mUpgradeProgressDialog?.disMissDialog()

                    }

                    override fun downloadSuccessful(uri: Uri) {
                        Log.e("pgyer", "download apk failed")
                        PgyUpdateManager.installApk(uri)  // 使用蒲公英提供的安装方法提示用户 安装apk
                    }
                })
                .register()
    }

    private fun UpgradeProgressDialog(tag: String?, downloadURL: String?, ver: String?) {
        mUpgradeProgressDialog = UpgradeProgressDialog(this)
        mUpgradeProgressDialog?.showDialog(getString(R.string.new_update_hint), 0)
        mUpgradeProgressDialog?.showDialogTag(tag)
        mUpgradeProgressDialog?.showDialogUpdateVer("Ver:"+ver)
        mUpgradeProgressDialog?.setOnUserCancelListener(object : UpgradeProgressDialog.CancelListener {
            override fun onSure() {
                PgyUpdateManager.downLoadApk(downloadURL)
            }

            override fun onCancel() {
                mUpgradeProgressDialog?.disMissDialog()
            }
        })

    }

    private fun checkPermissions() {
        val rxPermissions = RxPermissions(this)

        PermissionUtil().requestMultPerssions(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                //TODO 永久拒绝权限 弹出提示框
                showToast(resources.getString(R.string.permission_denied_unusable))
                showBtnDialog(
                        resources.getString(R.string.permission_denied_forever),
                        resources.getString(R.string.sure),
                        "",
                        false)

            }

            override fun onRequestPermissionSuccess() {
                val hdd = File(FileUtil.filePath)
                if (!hdd.exists()) {
                    hdd.mkdirs()
                }
            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showToast(resources.getString(R.string.permission_denied_unusable))
//                activity?.finish()
                PermissionUtil().gotoAppDetailIntent(this@MainActivity)
            }
        }, rxPermissions)

    }


    /**
     * 选择提示框
     */
    private fun showBtnDialog(title: String, left: String, right: String, showRight: Boolean) {
        if (mSelectDialog == null) {
            mSelectDialog = SelectDialog(this, showRight)
        }
        mSelectDialog?.showDialog()
        mSelectDialog?.setCanceledOnTouchOutside(false)
        mSelectDialog?.setCancelable(false)
        mSelectDialog?.setTitle(title)
        mSelectDialog?.setLeftAndRightText(left, right)
        mSelectDialog?.setListener(object : SelectDialogListener {
            override fun rightClick() {
            }

            override fun leftClick() {
                PermissionUtil().gotoAppDetailIntent(this@MainActivity)
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mSelectDialog != null && mSelectDialog!!.isShowing) {
            mSelectDialog!!.cancel()
            mSelectDialog = null
        }
    }


    private fun getToday(): String {
        val list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val data: Date = Date()
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    /**
     * 可放到onCreate或者BaseActivity
     */
    private fun setStatusBar() {
        CommonsStatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
    }

    private fun setHomeFragmentStatusBar() {
        top_title.visibility = View.GONE
        setStatusBar()
        top_title.setTitleBackground(resources.getColor(R.color.c_2a2d))
        top_title.setTitleTextColor(resources.getColor(R.color.c_ff))
        rb_home.isChecked = true
        rb_home.setTextColor(ContextCompat.getColor(this, R.color.c_ff42))
    }

    private fun setMineFragmentStatusBar() {
        top_title.setTitleBackground(resources.getColor(R.color.c_ff42))
        top_title.setTitleTextColor(resources.getColor(R.color.c_ff))
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.visibility = View.GONE
        rb_mine.isChecked = true
        rb_mine.setTextColor(ContextCompat.getColor(this, R.color.c_ff42))
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val mFragments: List<Fragment> = supportFragmentManager.fragments
            for (item in mFragments) {
                if (item is HomeFragment) {
                    mHomeFragment = item
                }
                if (item is MineHomeFragment) {
                    mMineFragment = item
                }
            }
        } else {
            mHomeFragment = HomeFragment()
            mMineFragment = MineHomeFragment()
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, mHomeFragment)
            fragmentTrans.add(R.id.fl_content, mMineFragment)
            fragmentTrans.commit()
        }
        supportFragmentManager.beginTransaction().show(mHomeFragment)
                .hide(mMineFragment)
                .commit()
    }

    private fun setRadioButton() {
        rb_home.isChecked = true
        rb_home.setTextColor(ContextCompat.getColor(this, R.color.c_ff42))
        rb_home.setOnClickListener(this)
        rb_find.setOnClickListener(this)
        rb_hot.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clearState()
        when (v?.id) {
            R.id.rb_find -> {
                rb_find.isChecked = true
                rb_find.setTextColor(ContextCompat.getColor(this, R.color.c_ff42))
                startActivity(FaultActivity::class.java)
                rb_mine.setTextColor(ContextCompat.getColor(this, R.color.c_33))
                supportFragmentManager.beginTransaction().hide(mMineFragment)
                        .commit()
                rb_home.setTextColor(ContextCompat.getColor(this, R.color.c_33))
                supportFragmentManager.beginTransaction().hide(mHomeFragment)
                        .commit()
            }
            R.id.rb_home -> {
                setHomeFragmentStatusBar()
                supportFragmentManager.beginTransaction().show(mHomeFragment)
                        .hide(mMineFragment)
                        .commit()
                top_title.setTitle(getToday())
                top_title.setLeftVisible(false)

            }
            R.id.rb_mine -> {
                setMineFragmentStatusBar()
                supportFragmentManager.beginTransaction().show(mMineFragment)
                        .hide(mHomeFragment)
                        .commit()
                top_title.setTitle(this.getString(R.string.mine))
                top_title.setLeftVisible(false)

            }
        }

    }

    private fun clearState() {
        rg_root.clearCheck()
        rb_home.setTextColor(ContextCompat.getColor(this, R.color.c_33))
        rb_mine.setTextColor(ContextCompat.getColor(this, R.color.c_33))
        rb_hot.setTextColor(ContextCompat.getColor(this, R.color.c_33))
        rb_find.setTextColor(ContextCompat.getColor(this, R.color.c_33))
    }

    override fun onPause() {
        super.onPause()
        mToast?.let {
            mToast!!.cancel()
        }
    }

    override fun loginSuccess(data: UserDetailEntity?, token: String?) {
    }

    override fun loginFail(e: String?) {
        showToast(e.toString())
    }

    override fun wxLoginSuccess(data: UserInfoResult?) {
    }

    override fun WeatherTestSuccess(data: WeatherEntity?) {

    }

    override fun getTokenSuccess(token: TokenEntity) {
    }

    override fun getRegisterTokenSuccess(token: TokenEntity) {
    }

    override fun getTokenFail(s: String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        clearState()
        val bundle = intent?.getExtras()
        if (bundle != null) {
            if (bundle.containsKey("id")) {
                val id = bundle.getString("id")
                if (id != null && id.equals("1")) {
                    setHomeFragmentStatusBar()
                    supportFragmentManager.beginTransaction().show(mHomeFragment)
                            .hide(mMineFragment)
                            .commit()
                    top_title.setTitle(getToday())
                    top_title.setLeftVisible(false)
                }
            }
        }

        setIntent(intent)
    }

    /**
     * 退出App
     */
    fun exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次退出APP")
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}