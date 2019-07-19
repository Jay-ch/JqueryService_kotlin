package com.jquery.service.android.ui.home.view

import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.jquery.service.android.Base.BaseMvpFragment
import com.jquery.service.android.R
import com.jquery.service.android.adapter.SelectFragmentAdapter
import com.jquery.service.android.adapter.TimeLineListCommAdapter
import com.jquery.service.android.app.App
import com.jquery.service.android.entity.TokenEntity
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.listener.SelectDialogListener
import com.jquery.service.android.logger.LogUtil
import com.jquery.service.android.permission.PermissionUtil
import com.jquery.service.android.ui.home.model.HomeContract
import com.jquery.service.android.ui.home.presenter.HomePresenter
import com.jquery.service.android.utils.CommonsStatusBarUtil
import com.jquery.service.android.utils.LocationHelper
import com.jquery.service.android.utils.UserHelper
import com.jquery.service.android.widgets.dialog.SelectDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_admin_home_layout.*
import java.util.*


/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeContract.HomeView, View.OnClickListener {

    val TAG = "AdminHomeFragment"
    var index: Int = 0
    private var mFragments = ArrayList<Fragment>()
    private var mSelectAdapter: SelectFragmentAdapter? = null
    private var mStatisticsFragment: AdminStatisticsFragment? = null
    private var mMaintenanceFragment: HomeMyStatueFragment? = null
    private lateinit var mContent: Context
    private var mTitle: String? = null
    private var locateHelper: LocationHelper? = null
    private var mLocationListener: LocationHelper.LocationListener? = null
    private var locationDescribe: String? = null

    private var mSelectDialog: SelectDialog? = null
    override fun createLayout(): Int {
        return R.layout.fragment_admin_home_layout
    }


    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }


    override fun initViews() {
        super.initViews()
        mContent = App.mContext
        mPresenter.WeatherTest("重庆")
        if (Build.VERSION.SDK_INT >= 23) {
            initBaiduMap()
        } else {
            initBaiduMap()
        }





        LogUtil.e(TAG, "initViews")
        var buttonDrawable = resources.getDrawable(R.drawable.ic_admin_selected)
        buttonDrawable.setBounds(0, 0, buttonDrawable.minimumWidth, buttonDrawable.minimumHeight)
        var buttonDrawableTrans = resources.getDrawable(R.drawable.shape_transport_view)
        buttonDrawableTrans.setBounds(0, 0, buttonDrawableTrans.minimumWidth, buttonDrawableTrans.minimumHeight)

        admin_home_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                // 切换到当前页面，重置高度
                admin_home_view_pager.requestLayout()
                when (position) {
                    0 -> {
                        admin_home_rb_maintenance.isChecked = true
                        admin_home_rb_statistics.isChecked = false
                        admin_home_rb_maintenance.setCompoundDrawables(null, null, null, buttonDrawable)
                        admin_home_rb_statistics.setCompoundDrawables(null, null, null, buttonDrawableTrans)
                    }
                    1 -> {
                        admin_home_rb_statistics.isChecked = true
                        admin_home_rb_maintenance.isChecked = false
                        admin_home_rb_maintenance.setCompoundDrawables(null, null, null, buttonDrawableTrans)
                        admin_home_rb_statistics.setCompoundDrawables(null, null, null, buttonDrawable)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        //rg  点击rg跳转
        admin_home_rg.setOnCheckedChangeListener({ group, checkedId ->
            when (checkedId) {
                R.id.admin_home_rb_maintenance ->
                    index = 0
                R.id.admin_home_rb_statistics ->
                    index = 1
            }
            if (admin_home_view_pager.currentItem != index) {
                admin_home_view_pager.setCurrentItem(index, false)
            }
        })
    }

    override fun initData() {
        super.initData()
        top_title.setTitleBackground(resources.getColor(R.color.c_2a2d))
        top_title.setTitleTextColor(resources.getColor(R.color.c_ff))
        CommonsStatusBarUtil.setStatusViewAttr(getActivity(), view_status_bar, resources.getColor(R.color.c_2a2d))
        tv_relocation.setOnClickListener(this)
        mMaintenanceFragment = HomeMyStatueFragment()
        mFragments.add(mMaintenanceFragment!!)

        //临时判断是否为管理员
        val token = UserHelper().getTokenFromFile(App.mContext)
        if (token == "admin") {
            mStatisticsFragment = AdminStatisticsFragment()
            mFragments.add(mStatisticsFragment!!)
        } else {
            admin_home_rg.visibility = View.GONE
        }
        mSelectAdapter = SelectFragmentAdapter(activity!!.supportFragmentManager, mFragments)
        admin_home_view_pager.adapter = mSelectAdapter
        // 如果不设置，可能第三个页面以后就显示不出来了，因为offset就是默认值1了
        admin_home_view_pager.offscreenPageLimit = mSelectAdapter?.count!!

        ll_sign.setOnClickListener {
            ll_sign.setBackgroundResource(R.drawable.semicircle_40_round_gray)
            tv_sign.setTextColor(resources.getColor(R.color.c_99))
            var drawable = getResources().getDrawable(R.drawable.home_signed_icon)
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight())
            tv_sign.setCompoundDrawables(drawable, null, null, null);
        }
    }


    private fun initBaiduMap() {
        var direction = null
        locateHelper = LocationHelper.Builder(App.mContext)
                .setScanSpan(0)
                .setIsNeedLocationDescribe(true).build()
        mLocationListener = object : LocationHelper.LocationListener() {
            override fun onReceiveLocation(location: LocationHelper.LocationEntity) {
                if ("成功" in location.toString()) {
                    hideBaseLoading()
                    if ("在" in location.locationDescribe) {
                        locationDescribe = location.locationDescribe.substring(1)
                        tv_home_address.text = location.city + location.district + location.street + locationDescribe
                        tv_relocation.visibility = View.VISIBLE
                        locateHelper?.stop()
                    }
                } else {
                    hideBaseLoading()
                    tv_home_address.text = "定位失败，重新再试"
                    tv_relocation.visibility = View.VISIBLE
                }
                println(" --------------------------location $location")
            }

            override fun onError(e: Throwable) {
                println(" --------------------------throwable $e")
                tv_home_address.text = e.message
                hideBaseLoading()
            }
        }
        locateHelper?.registerLocationListener(mLocationListener as LocationHelper.LocationListener)


        PermissionUtil().requestMultPerssions(object : PermissionUtil.RequestPermission {
            override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
                showToast(resources.getString(R.string.permission_denied_unusable))
                PermissionUtil().gotoAppDetailIntent(activity!!)
            }

            override fun onRequestPermissionSuccess() {
                if (locateHelper?.start() == true) {
                } else {
                    hideBaseLoading()
                    tv_home_address.text = "定位失败，请检查是否开启了定位？"
                }

            }

            override fun onRequestPermissionFailure(permissions: List<String>) {
                showToast(resources.getString(R.string.permission_denied_unusable))
                PermissionUtil().gotoAppDetailIntent(activity!!)
            }
        }, RxPermissions(this))
    }


    /**
     * 选择提示框
     */
    private fun showBtnDialog(title: String, left: String, right: String, showRight: Boolean) {
        if (mSelectDialog == null) {
            mSelectDialog = SelectDialog(context, showRight)
        }
        mSelectDialog?.showDialog()
        mSelectDialog?.setCanceledOnTouchOutside(false)
        mSelectDialog?.setCancelable(false)
        mSelectDialog?.setTitle(title)
        mSelectDialog?.setLeftAndRightText(left, right)
        mSelectDialog?.setListener(object : SelectDialogListener {
            override fun rightClick() {
                activity?.finish()
            }

            override fun leftClick() {
                activity?.finish()
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

    override fun onStop() {
        super.onStop()
        locateHelper?.unRegisterLocationListener(this.mLocationListener!!)
        locateHelper?.stop()
    }

    override fun onClick(v: View?) {
    }

    override fun loginSuccess(data: UserDetailEntity?, token: String?) {
    }

    override fun loginFail(e: String?) {
        showToast(e.toString())
    }

    override fun wxLoginSuccess(data: UserInfoResult?) {
    }

    override fun WeatherTestSuccess(data: WeatherEntity?) {
        //hideBaseLoading()
//        tv_test.setText("近几日天气：" + data?.forecast)
    }

    override fun getTokenSuccess(token: TokenEntity) {

    }

    override fun getRegisterTokenSuccess(token: TokenEntity) {

    }

    override fun getTokenFail(s: String) {

    }


    override fun getUserInfoSuccess(result: UserInfoResult) {

    }

}