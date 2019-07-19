package com.jquery.service.android.ui.home.presenter

import com.jquery.service.android.Base.BasePresenter
import com.jquery.service.android.Base.BaseSubscriber
import com.jquery.service.android.entity.WeatherEntity
import com.jquery.service.android.logger.LogUtil
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.ui.home.model.AdminStatisticsContract
import com.jquery.service.android.ui.home.model.HomeModel


/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class AdminStatisticsPresenter : BasePresenter<AdminStatisticsContract.StatisticsView>(), AdminStatisticsContract.StatisticsPresenter {


    val mModel: HomeModel by lazy {
        HomeModel()
    }

    /**
     * 签到情况
     */
    override fun getUnsignedEmployee() {
        getView()?.showLoading("数据加载中...")
        LogUtil.e("getMessage","getUnsignedEmployee")
        //TODO 修改请求
        addSubscriber(mModel.WeatherTest("重庆"), object : BaseSubscriber<HttpResult<WeatherEntity>>() {
            override fun onSuccess(result: HttpResult<WeatherEntity>) {
                LogUtil.e("getMessage","getUnsignedEmployee")
                getView()?.hideLoading()

                val unSignList = ArrayList<String>()
                unSignList.add("员工1")
                unSignList.add("员工2")
                unSignList.add("员工3")
                unSignList.add("员工4")
                unSignList.add("员工5")
                getView()?.getUnsignedEmployeeSuccess(unSignList)

                getFaultStatue()
            }

            override fun onFail(s: String?) {
                getView()?.hideLoading()

                getView()?.getUnsignedEmployeeFail(s)
            }
        })
    }

    /**
     * 故障情况
     */
    override fun getFaultStatue() {
        LogUtil.e("getMessage","getFaultStatue")
        //TODO 修改请求
        addSubscriber(mModel.WeatherTest("重庆"), object : BaseSubscriber<HttpResult<WeatherEntity>>() {
            override fun onSuccess(result: HttpResult<WeatherEntity>) {
                getView()?.hideLoading()

                val unSignList = ArrayList<String>()
                unSignList.add("员工1")
                unSignList.add("员工2")
                unSignList.add("员工3")
                unSignList.add("员工4")
                unSignList.add("员工5")
                getView()?.getFaultStatueSuccess(unSignList)
            }

            override fun onFail(s: String?) {
                getView()?.hideLoading()
                getView()?.getFaultStatueFail(s)
            }
        })
    }
}
