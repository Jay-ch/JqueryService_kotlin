package com.jquery.service.android.api

import com.jquery.service.android.entity.*
import com.jquery.service.android.retrofit.CommListResult
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author J.query
 * @date 2019/3/14
 * @email j-query@foxmail.com
 */
interface FaultPageServices {
    /**
     * 顶部菜单
     *
     * @return
     */
    @POST("liveapi.article.categories")
    fun getTopMenus(): Flowable<HttpResult<List<MenuItemEntity>>>

    /**
     * 获取分类列表
     *
     * @return
     */
    @POST("liveapi.article.list")
    fun getCategoryList(@Body body: RequestBody): Flowable<HttpResult<CommListResult<CategoryItem>>>

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("ecapi.user.info")
    fun getUserInfo(): Flowable<HttpResult<UserInfoResult>>


    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("ecapi.host.uploadimage")
    fun uploadImageIdCard(@Part file: MultipartBody.Part): Flowable<ImageUploadIdCardResult<ImageIdCardListEntity>>

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("ecapi.host.uploadimage")
    fun uploadIdCardImage(@Part file: MultipartBody.Part): Flowable<HttpResult<ImageIdCardListEntity>>


}