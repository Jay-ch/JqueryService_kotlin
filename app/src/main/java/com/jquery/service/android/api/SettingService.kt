package com.jquery.service.android.api

import com.jquery.service.android.entity.ImageListEntity
import com.jquery.service.android.entity.UpdateUserEntity
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.retrofit.ImageUploadResult
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
interface SettingService {

    /**
     * @return
     */
  /*  @POST("ecapi.auth.signout")
    abstract fun logOut(): Flowable<HttpResult>*/

    /**
     * 提交修改信息
     *
     * @param body
     * @return
     */
    @POST("ecapi.user.profile.update")
    abstract fun updateUserInfo(@Body body: RequestBody): Flowable<HttpResult<UpdateUserEntity>>


    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("ecapi.user.image.upload")
    abstract fun uploadImage(@Part file: MultipartBody.Part): Flowable<ImageUploadResult<ImageListEntity>>

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("ecapi.user.image.upload")
    abstract fun uploadImageMore(@Part file: List<MultipartBody.Part>): Flowable<ImageUploadResult<ImageListEntity>>

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("ecapi.user.image.upload")
    abstract fun updateApp(@Body body: RequestBody): Flowable<ImageUploadResult<ImageListEntity>>

}