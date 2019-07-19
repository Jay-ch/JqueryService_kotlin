package com.jquery.service.android.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.jquery.service.android.Base.BaseMvpActivity
import com.jquery.service.android.R
import com.jquery.service.android.adapter.SelectFragmentAdapter
import com.jquery.service.android.entity.*
import com.jquery.service.android.listener.ListSelectDialogListener
import com.jquery.service.android.ui.MainActivity
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.widgets.dialog.ListCommDialog
import kotlinx.android.synthetic.main.activity_fault_layout.*
import kotlinx.android.synthetic.main.include_title.*
import java.util.*


/**
 * 故障维修
 * @author J.query
 * @date 2019/4/2
 * @email j-query@foxmail.com
 */
class FaultActivity : BaseMvpActivity<HomePresenter>(), HomeContract.HomeView, View.OnClickListener {


    var index: Int = 0
    private var mFragments: ArrayList<Fragment>? = null
    private var mSelectAdapter: SelectFragmentAdapter? = null
    private var mHandledFaultFragment: HandledFaultFragment? = null
    private var mUntreatedFaultFragment: UntreatedFaultFragment? = null

    private var locationDescribe: String? = null

    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun createLayout(): Int {
        return R.layout.activity_fault_layout
    }

    override fun initViews() {
        super.initViews()
        //hideNavigationBar()
        //setStatusBar()
        setStatusWhiteBar()
        initClick()
    }

    private fun setStatusWhiteBar() {
        CommonsStatusBarUtil.setStatusBar(this)
        top_title.setTitleBackground(resources.getColor(R.color.c_ff))
        top_title.setTitleTextColor(resources.getColor(R.color.c_33))
        CommonsStatusBarUtil.setStatusViewAttr(this, view_status_bar, resources.getColor(R.color.c_ff))
    }

    override fun initData() {
        super.initData()
        mHandledFaultFragment = HandledFaultFragment()
        mUntreatedFaultFragment = UntreatedFaultFragment()
        mFragments = ArrayList<Fragment>()
        mFragments?.add(mUntreatedFaultFragment!!)
        mFragments?.add(mHandledFaultFragment!!)
        mSelectAdapter = SelectFragmentAdapter(getSupportFragmentManager(), mFragments!!)
        view_pager.setAdapter(mSelectAdapter)
        //showPermissionDialog()
    }

    private fun showPermissionDialog() {
        var map1 = ListCommEntity("张三", 1, "张三")
        var map2 = ListCommEntity("李四", 2, "李四")
        var map3 = ListCommEntity("王五", 3, "王五")
        var list = mutableListOf<ListCommEntity>()
        list.add(0, map1)
        list.add(1, map2)
        list.add(2, map3)
        var mListCommDialog = ListCommDialog(this, true)
        if (mListCommDialog !== null) {
            mListCommDialog.setListener(object : ListSelectDialogListener {
                override fun selectType(s: String) {

                }

                override fun onItemClick(position: Int) {
                    var get = list.get(position).token
                    log("===============获取到的值=================" + get)
                }

            })
            mListCommDialog.setCanceledOnTouchOutside(false)
            mListCommDialog.setCancelable(false)
            mListCommDialog.showDialog()
            mListCommDialog.setHint("表具型号")
            mListCommDialog.setLeftAndRightText("好的", "取消")
            mListCommDialog.addData(list)
            //permissionDialog.setLeftText("知道了")
        }
    }

    private fun initClick() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    1 -> {
                        rb_handled.setChecked(true)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_ff)
                    }
                    0 -> {
                        rb_untreated.setChecked(true)
                        rb_handled.setChecked(false)
                        //rb1.setBackgroundResource(R.drawable.screen_radio)
                        //rb2.setBackgroundResource(R.color.c_33)
                        //rb1.setBackgroundResource(R.color.c_ff)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        //rg  点击rg跳转
        rg.setOnCheckedChangeListener({ group, checkedId ->
            when (checkedId) {
                R.id.rb_handled ->
                    index = 1
                R.id.rb_untreated ->
                    index = 0
            }
            if (view_pager.getCurrentItem() != index) {
                view_pager.setCurrentItem(index, false)
            }
        })
        tv_back.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("id", "1")
            startActivity(MainActivity::class.java, bundle)
            finish()

        }
        img_right.setOnClickListener {
            /*   val intent = Intent(this@FaultActivity, SearchActivity::class.java)
               intent.putExtra("id", 1)
               startActivity(intent)*/
            var bundle = Bundle()
            bundle.putString("time_type", "in_fault_year")
            startActivity(SelectionTimeActivity::class.java, bundle)
        }
        bt_new_fault.setOnClickListener {
            val intent = Intent(this@FaultActivity, NewFaultActivity::class.java)
            intent.putExtra("id", 1)
            startActivity(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /* val intent = Intent(this@FaultActivity, MainActivity::class.java)
             intent.putExtra("id", 1)
             startActivity(intent,MainActivity::class.java)*/
            var bundle = Bundle()
            bundle.putString("id", "1")
            startActivity(MainActivity::class.java, bundle)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun loginSuccess(data: UserDetailEntity?, token: String?) {
    }

    override fun loginFail(e: String?) {
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

    override fun getUserInfoSuccess(result: UserInfoResult) {
    }

    override fun onClick(v: View?) {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onRestart() {
        super.onRestart()
        var bundle = getIntent().getExtras()
        var time_type = bundle?.getString("time_type")
        if (time_type != null && time_type.equals("in_fault_year")) {
            var mEndDay = bundle?.getString("in_sign_end_day")
            var mStartDay = bundle?.getString("in_sign_start_day")
            //传值给接口
            Log.e("FaultActivity======", mStartDay + "~" + mEndDay)
        }
        log("FaultActivity" + "-------onRestart------")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("FaultActivity", "=========onDestroy==============")
    }
}