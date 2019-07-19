package com.jquery.service.android.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.CompoundButton
import com.jquery.service.android.Base.BaseFullMvpActivity
import com.jquery.service.android.R
import com.jquery.service.android.constant.Constants
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.listener.LoginResultEvent
import com.jquery.service.android.listener.WxLoginEvent
import com.jquery.service.android.ui.MainActivity
import com.jquery.service.android.ui.login.model.LoginContract
import com.jquery.service.android.ui.login.presenter.LoginPresenter
import com.jquery.service.android.utils.StatusBarUtils
import com.jquery.service.android.utils.UserHelper
import com.jquery.service.android.widgets.dialog.ForgetDialog
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class LoginActivity : BaseFullMvpActivity<LoginPresenter>(), LoginContract.LoginView {

    private var mApi: IWXAPI? = null
    val REQUEST_COUNTRY_CODE = 0x11

    override fun createLayout(): Int {

        return R.layout.activity_login_layout
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun initViews() {
        super.initViews()
        //沉侵式状态栏
        StatusBarUtils.setStatusBar(this)
        setLogin()
        setForget()
        setWxLogin()
    }
    private fun setWxLogin() {
        tv_wx_login.setOnClickListener {
            if (mApi?.isWXAppInstalled()!!) {
                val req = SendAuth.Req()
                req.scope = "snsapi_userinfo"
                req.state = "diandi_wx_login"
                mApi?.sendReq(req)
            } else
                showToast(R.string.uninstall_wx)
        }
    }

    private fun setLogin() {
        tv_loginBtn.setOnClickListener {
            val password = tv_password.getText().toString().trim({ it <= ' ' })
            val phone = tv_account.getText().toString().trim({ it <= ' ' })
            /* if (password.isEmpty() || password.length < 6 || password.length > 20) {
                 showToast(R.string.password_tips)
             } else if (phone.isEmpty()) {
                 showToast(R.string.phone_tips)
             } else {
                 mPresenter.loginByPhone(phone, password)
             }*/
            /* finish()
             startActivity(MainActivity::class.java)*/

            finish()

            var intent =  Intent(this@LoginActivity,MainActivity::class.java)

//            if (password.isEmpty() && phone.isEmpty()){
                intent.putExtra("id", 2)
                UserHelper().setTokenToFile(application,"admin")
//            }else{
//                intent.putExtra("id", 1)
//                UserHelper().setTokenToFile(application,"admin")
//            }
//            intent.putExtra("id", 1)
            startActivity(intent)

            /*  val id = getIntent().getIntExtra("id", 0)
              if (id == 1) {
                  supportFragmentManager
                          .beginTransaction()
                          .replace(R.id.fl_content, HomeFragment())
                          .addToBackStack(null)
                          .commit()
              }*/
        }

    }


    private fun setForget() {
        tv_forget_password.setOnClickListener {
            var mForgetDialog = ForgetDialog(this, true)
            mForgetDialog.setCanceledOnTouchOutside(false)
            mForgetDialog.setCancelable(true)
            mForgetDialog.show()
        }
    }

    override fun initData() {
        super.initData()
        EventBus.getDefault().register(this)
        box_eye.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tv_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            } else {
                tv_password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            }
        })
        mApi = WXAPIFactory.createWXAPI(this, Constants().APP_ID, false)
        mApi?.registerApp(Constants().APP_ID)
    }


    override fun loginSuccess(data: UserDetailEntity, token: String) {
        UserHelper().saveUserInfo(this, data, token)
        EventBus.getDefault().post(LoginResultEvent(data))
        finish()
    }

    override fun loginFail(e: String) {
        showToast(e)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == REQUEST_COUNTRY_CODE && resultCode == RESULT_OK) {
            tv_country_name.setText(data.getStringExtra("countryName"))
            tv_country_number.setText(data.getStringExtra("countryNumber"))
        }*/
    }

    /**
     * 微信登录回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxEvent(event: WxLoginEvent?) {
        if (event != null) {
            val resp = event?.getBaseResp()
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    val sendResp = resp as SendAuth.Resp
                    mPresenter.getWxToken(sendResp.code)
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> showToast(resources.getString(R.string.login_cancel))
                BaseResp.ErrCode.ERR_AUTH_DENIED -> showToast(resources.getString(R.string.login_refuse))
            }
        }
    }

    override fun wxLoginSuccess(data: UserInfoResult) {
        UserHelper().saveUserInfo(this, data.user, data.token)
        val bundle = Bundle()
        bundle.putInt("type", Constants().BIND)
        if (data.user.mobile_binded === 0) {
            //startActivity(RegisterActivity::class.java, bundle)
        } else {
            if (data.user.is_completed === 0) {
                // startActivity(RegisterActivity::class.java, bundle)
            } else {
                //loginSuccess(data.user, data.token)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}