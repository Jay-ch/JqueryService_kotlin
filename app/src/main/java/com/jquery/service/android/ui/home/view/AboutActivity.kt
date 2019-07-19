package com.jquery.service.android.ui.home.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.pgyersdk.update.DownloadFileListener
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.listener.DownloadListener
import com.jquery.service.android.retrofit.DownloadUtil
import com.jquery.service.android.utils.APKVersionUtils.getLocalVersionName
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.UpgradeProgressDialog
import kotlinx.android.synthetic.main.activity_about_layout.*
import kotlinx.android.synthetic.main.include_title_bar.*
import java.io.File


/**
 * 关于
 * @author J.query
 * @date 2019/3/12
 * @email j-query@foxmail.com
 */
class AboutActivity : BaseActivity() {

    private var listener: DownloadListener? = null
    private var mUpgradeProgressDialog: UpgradeProgressDialog? = null
    private var TAG: String? = "AboutActivity"

    private val permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun createLayout(): Int {
        return R.layout.activity_about_layout
    }

    override fun initViews() {
        super.initViews()
        setStatusBar()
        Logger.init("SSTX").logLevel(LogLevel.FULL)
        tv_version.setText(getLocalVersionName(this))
        //setStatusBarWhite()
    }

    override fun initData() {
        super.initData()
        test_version.setOnClickListener {
            loadUpgradeInfo()
        }
    }


    override fun setStatusBar() {

        //CommonsStatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)

        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(ContextCompat.getColor(this, R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, ContextCompat.getColor(this, R.color.c_ff))
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
                        showToast(this@AboutActivity, getString(R.string.no_version))

                    }

                    override fun onUpdateAvailable(appBean: AppBean) {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.versionCode)

                        UpgradeProgressDialog(appBean.releaseNote, appBean.downloadURL, appBean.versionName)
                    }

                    override fun checkUpdateFailed(e: Exception) {
                        //更新检测失败回调
                        Log.e("pgyer", "check update failed ", e)
                        if (e.toString().contains("net work")) {
                            showToast(this@AboutActivity, getString(R.string.network_error))
                        } else {
                            showToast(this@AboutActivity, getString(R.string.check_update_failed))
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
        mUpgradeProgressDialog?.showDialogUpdateVer(ver)
        mUpgradeProgressDialog?.setOnUserCancelListener(object : UpgradeProgressDialog.CancelListener {
            override fun onSure() {
                PgyUpdateManager.downLoadApk(downloadURL)
            }

            override fun onCancel() {
                mUpgradeProgressDialog?.disMissDialog()
            }
        })

    }

    private fun UpgradeProgressDialog(baseUrl: String?, url: String?, desFilePath: String?, listener: DownloadListener?) {
        var mUpgradeProgressDialog = UpgradeProgressDialog(this)
        mUpgradeProgressDialog.showDialog(getString(R.string.log_export_ing), 0)
        mUpgradeProgressDialog.setOnUserCancelListener(object : UpgradeProgressDialog.CancelListener {
            override fun onSure() {
                test_version.isEnabled = false

                DownloadUtil().getInstance()
                        .downloadFile(baseUrl, url, desFilePath, object : DownloadListener {
                            override fun onFinish(file: File?) {
                                test_version.isEnabled = true
                                //tv_file_location.text = "下载的文件地址为：" + file?.absolutePath
                                installAPK(file, this@AboutActivity)
                            }

                            override fun onProgress(progress: Int?) {
                                //tv_progess.text = String.format("下载进度为：%s", progress) + "%"
                                Log.e("下载进度 onProgress", progress.toString())
                                mUpgradeProgressDialog.setProgress(progress)
                            }

                            override fun onFailed(errMsg: String?) {
                                test_version.isEnabled = true
                                Log.e("下载进度 失敗onFailed", errMsg.toString())
                                //showToast(this@AboutActivity, "下载失敗！请重新下载！")
                            }

                            override fun onPause(errMsg: String?) {
                                Log.e("下载进度 失敗onPause", errMsg.toString())

                            }

                            override fun onCancel(errMsg: String?) {
                                Log.e("下载进度 失敗onCancel", errMsg.toString())

                            }
                        })
            }


            override fun onCancel() {
                //userCancel = true
                //DownloadUtil().getInstance().stopDown()
                mUpgradeProgressDialog.disMissDialog()
            }
        })
    }


    fun installAPK(file: File?, mAct: Activity) {
        val authority = "com.qwkrom.service.android.fileProvider"
        mAct.startActivity(getInstallAppIntent(file, authority, true))
    }

    private fun getInstallAppIntent(file: File?,
                                    authority: String,
                                    isNewTask: Boolean): Intent? {
        if (file == null) return null
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = FileProvider.getUriForFile(this@AboutActivity, authority, file)
        }
        intent.setDataAndType(data, type)
        return getIntent(intent, isNewTask)
    }

    private fun getIntent(intent: Intent, isNewTask: Boolean): Intent {
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }
}
