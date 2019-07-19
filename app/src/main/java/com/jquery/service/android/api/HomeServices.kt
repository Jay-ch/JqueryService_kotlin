package com.jquery.service.android.api

import android.view.MenuItem
import com.jquery.service.android.entity.CategoryItem
import com.jquery.service.android.entity.UserInfoResult
import com.jquery.service.android.retrofit.CommListResult
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
interface HomeServices {
    /**
     * 顶部菜单
     *
     * @return
     */
    @POST("liveapi.article.categories")
    abstract fun getTopMenus(): Flowable<HttpResult<List<MenuItem>>>

    /**
     * 获取分类列表
     *
     * @return
     */
    @POST("liveapi.article.list")
    abstract fun getCategoryList(@Body body: RequestBody): Flowable<HttpResult<CommListResult<CategoryItem>>>

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("ecapi.user.info")
    abstract fun getUserInfo(): Flowable<HttpResult<UserInfoResult>>

}