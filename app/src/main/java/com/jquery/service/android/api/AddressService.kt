package com.jquery.service.android.api

import com.jquery.service.android.entity.AddressEntity
import com.jquery.service.android.retrofit.AddressResult
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
interface AddressService {
    /**
     * 获取地址列表
     *
     * @param
     * @return
     */
    @POST("ecapi.consignee.list")
    abstract fun getAddress(): Flowable<HttpResult<AddressResult<AddressEntity>>>

    /**
     * @param body
     * @return增加收货地址
     */
    @POST("ecapi.consignee.add")
    abstract fun addAddress(@Body body: RequestBody): Flowable<HttpResult<AddressResult<AddressEntity>>>

    /**
     * @param body
     * @return 删除收货地址
     */
    @POST("ecapi.consignee.delete")
    abstract fun deleteAddress(@Body body: RequestBody): Flowable<HttpResult<AddressResult<AddressEntity>>>


    /**
     * @param body
     * @return 修改收货地址
     */
    @POST("ecapi.consignee.update")
    abstract fun updateAddress(@Body body: RequestBody): Flowable<HttpResult<AddressResult<AddressEntity>>>
}