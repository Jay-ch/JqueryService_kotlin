package com.jquery.service.android.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import butterknife.OnClick
import com.jquery.service.android.Base.BaseActivity
import com.jquery.service.android.R
import com.jquery.service.android.entity.UserDetailEntity
import com.jquery.service.android.ui.home.view.FaultFragment
import com.jquery.service.android.ui.home.view.HomeFragment
import com.jquery.service.android.ui.home.view.MineHomeFragment
import kotlinx.android.synthetic.main.activity_main1.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @author J.query
 * @date 2019/3/22
 * @email j-query@foxmail.com
 */
class Main : BaseActivity() {

    var mHomeFragment: HomeFragment? = null
    var mFaultFragment: FaultFragment? = null
    var mMineFragment: MineHomeFragment? = null
    var mExitTime: Long = 0
    var mToast: Toast? = null
    private var currentFragment: Fragment? = null
    private var fragmentManager: FragmentManager? = null
    private var userInfo: UserDetailEntity? = null
    private var exitTime: Long = 0

    override fun createLayout(): Int {
        return R.layout.activity_main1
    }

    override fun setStatusBar() {

    }

    override fun initViews() {
        fragmentManager = supportFragmentManager
    }

    override fun initData() {
        changeFragment(R.id.ct_home)
    }

    private fun getToday(): String {
        var list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        var data: Date = Date()
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    @OnClick(R.id.ct_home, R.id.ct_category, R.id.ct_mine)
    fun tabItemClick(view: View) {
        ct_home.isChecked = false
        ct_category.isChecked = false
        ct_mine.isChecked = false
        changeFragment(view.id)
    }

    /**
     * fragment 切换
     *
     * @param checkedId
     */
    private fun changeFragment(checkedId: Int) {
        when (checkedId) {
            R.id.ct_home -> {
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment()
                }
                ct_home.isChecked = true
                showFragment(mHomeFragment!!)
                top_title.setTitle(getToday())
                top_title.setLeftVisible(false)
            }
            R.id.ct_category -> {
                if (mFaultFragment == null) {
                    mFaultFragment = FaultFragment()
                }
                ct_category!!.isChecked = true
                showFragment(mFaultFragment!!)
                top_title.setTitle(this.getString(R.string.fault))
                top_title.setLeftVisible(false)
            }
            R.id.ct_mine -> {
                if (mMineFragment == null) {
                    mMineFragment = MineHomeFragment()
                }
                ct_mine.isChecked = true
                showFragment(mMineFragment!!)
                top_title.setTitle(this.getString(R.string.mine))
                top_title.setLeftVisible(false)
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        if (currentFragment === fragment) {
            return
        }
        val transaction = fragmentManager!!.beginTransaction()
        if (currentFragment == null) {
            if (!fragment.isAdded) {
                transaction.add(R.id.flayout_home, fragment).commit()
            } else {
                transaction.show(fragment).commit()
            }
        } else {
            if (!fragment.isAdded) {
                transaction.hide(currentFragment).add(R.id.flayout_home, fragment).commit()
            } else {
                transaction.hide(currentFragment).show(fragment).commit()
            }
        }
        currentFragment = fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            showToast("再按一次退出APP")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}