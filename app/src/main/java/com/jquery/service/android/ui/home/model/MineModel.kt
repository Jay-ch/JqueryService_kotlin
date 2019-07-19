package com.jquery.service.android.ui.home.model

import android.view.MenuItem
import com.jquery.service.android.api.AddressService
import com.jquery.service.android.api.HomeServices
import com.jquery.service.android.api.LoginServices
import com.jquery.service.android.api.SettingService
import com.jquery.service.android.entity.*
import com.jquery.service.android.retrofit.AddressResult
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.retrofit.RetrofitHelper
import io.reactivex.Flowable
import okhttp3.MultipartBody
import java.util.*


/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class MineModel :MineContract.Model{
    override fun getAddress(): Flowable<HttpResult<AddressResult<AddressEntity>>> {
        return RetrofitHelper.createApi(AddressService::class.java).getAddress()
    }

    override fun getTopMenus(): Flowable<HttpResult<List<MenuItem>>> {
        return RetrofitHelper.createApi(HomeServices::class.java).getTopMenus()
    }

    override fun getUserInfo(): Flowable<HttpResult<UserInfoResult>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getToken(seq: String, channelId: String, deviceId: String, sign: String): Flowable<HttpResult<TokenEntity>> {
        return RetrofitHelper.createApi(LoginServices::class.java).getToken(seq, channelId, deviceId, sign)
    }

    override fun uploadImageIdcard(file: MultipartBody.Part): Flowable<ImageUploadIdCardResult<ImageIdCardListEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUserInfo(hashMap: HashMap<*, *>): Flowable<HttpResult<UpdateUserEntity>> {
        return RetrofitHelper.createApi(SettingService::class.java).updateUserInfo(createBody(hashMap))
    }

}