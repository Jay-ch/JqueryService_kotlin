package com.jquery.service.android.ui.home.model

import android.view.MenuItem
import com.jquery.service.android.Base.*
import com.jquery.service.android.entity.*
import com.jquery.service.android.retrofit.AddressResult
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import okhttp3.MultipartBody
import java.util.*

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
interface MineContract {

    interface HomeView : ImBaseView {
        fun getAddressSuccess(consignees: List<AddressEntity>)

        fun getTopMenusSuccess(data: List<MenuItem>)

        fun getUserInfoSuccess(result: UserInfoResult)

        fun onTokenError()

        fun uploadImageIdcard(imageIdCardList: List<ImageIdCardListEntity>)

        fun uploadImageIdcardFail(s: String)

    }

    interface Model : ImBaseModel {

        abstract fun getAddress(): Flowable<HttpResult<AddressResult<AddressEntity>>>

        abstract fun getTopMenus(): Flowable<HttpResult<List<MenuItem>>>

        abstract fun getUserInfo(): Flowable<HttpResult<UserInfoResult>>

        abstract fun getToken(seq: String, channelId: String, deviceId: String, sign: String): Flowable<HttpResult<TokenEntity>>

        abstract fun uploadImageIdcard(file: MultipartBody.Part): Flowable<ImageUploadIdCardResult<ImageIdCardListEntity>>

        abstract fun updateUserInfo(hashMap: HashMap<*, *>): Flowable<HttpResult<UpdateUserEntity>>


    }

    interface HomePresenter : ImBasePresenter {

        abstract fun getAddress()

        abstract fun getTopMenus()

        abstract fun getUserInfo()

        abstract fun uploadImageIdcard(avatar: String)

        abstract fun applyForFostInfo(id_name: String, id_card: String, is_protocol: String, image_file: String)
    }
}