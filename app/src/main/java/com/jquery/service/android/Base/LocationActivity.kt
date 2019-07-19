package com.jquery.service.android.Base

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import butterknife.Unbinder
import com.jquery.service.android.R
import com.umeng.analytics.MobclickAgent

/**
 * @author J.query
 * @date 2019/3/26
 * @email j-query@foxmail.com
 */
abstract class LocationActivity<T : BasePresenter<*>> : BaseLocaActivity() {
    lateinit var mPresenter: T
    private lateinit var bind: Unbinder
    override fun providerContext(): Context {
        return this.providerContext()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (createLayout() != 0)
            setContentView(createLayout())
        mPresenter = createPresenter()
        mPresenter.attachView(this)
        bind = ButterKnife.bind(this)
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL)//场景类型设置接口。
    }

    protected abstract fun createPresenter(): T

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null)
            mPresenter.detachView()
    }

    abstract override fun createLayout(): Int


    override fun setTitle(title: String) {
        val tvTitle = findViewById(R.id.include_tv) as TextView
        tvTitle.text = title
        val tvBack = findViewById(R.id.tv_back) as TextView
        tvBack.setOnClickListener { finish() }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initViews()
        initData()
        initViewAndData()
    }

    override fun initViewAndData() {

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


    @Optional
    @OnClick(R.id.tv_back)
    override fun finishActivity() {
        this.finish()
    }

    override fun showLoading(s: String?) {
        showBaseLoading(s)
    }

    override fun hideLoading() {
        hideBaseLoading()
    }

}