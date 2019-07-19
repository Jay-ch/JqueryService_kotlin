package com.jquery.service.android.ui

import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.ui.login.view.LoginActivity
import com.jquery.service.android.utils.APKVersionUtils.getLocalVersionName
import com.jquery.service.android.utils.UserHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

/**
 * 引导页
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class SplashActivity : BaseActivity() {
    private var disposable: Disposable? = null
    var MESSAGE_PROGRESS = "message_progress"

    override fun createLayout(): Int {
        return R.layout.activity_splash
    }

    override fun setStatusBar() {

    }

    override fun initViews() {
        super.initViews()
        tv_version_hint.setText("版本：" + getLocalVersionName(this))
    }

    override fun initData() {
        super.initData()
        disposable = Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Long> {
                    override fun accept(t: Long) {
//                        if (checkLogin()) {
                        startActivity(LoginActivity::class.java)
                        finish()
//                        } else {
//                            var token = UserHelper().getToken(application)
//                            if (token.equals("admin")) {
//                                startActivity(AdminMainActivity::class.java)
//                            } else {
//                                startActivity(MainActivity::class.java)
//                            }
//                            finish()
//                        }

                    }
                })


    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    /**判断登录状态
     * @return
     */
    private fun checkLogin(): Boolean {
        if (UserHelper().getToken(this) != null) {
            startActivity(LoginActivity::class.java)
            return false
        } else {
            return true
        }
    }

}