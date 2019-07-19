package com.jquery.service.android.ui.home.presenter

import android.view.MenuItem
import com.jquery.service.android.Base.BaseSubscriber
import com.jquery.service.android.Base.BasePresenter
import com.jquery.service.android.entity.*
import com.jquery.service.android.retrofit.AddressResult
import com.jquery.service.android.retrofit.HttpResult
import com.jquery.service.android.ui.home.model.MineContract
import com.jquery.service.android.ui.home.model.MineModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class MinePresenter : BasePresenter<MineContract.HomeView>(), MineContract.HomePresenter {
    val mModel: MineModel by lazy {
        MineModel()
    }

    override fun getAddress() {
        addSubscriber(mModel.getAddress(),
                object : BaseSubscriber<HttpResult<AddressResult<AddressEntity>>>() {
                    override fun onFail(s: String?) {
                    }

                    override fun onSuccess(result: HttpResult<AddressResult<AddressEntity>>) {
                        getView()?.getAddressSuccess(result.data.consignees)
                    }

                })
    }

    override fun getTopMenus() {
        addSubscriber(mModel.getTopMenus(),
                object : BaseSubscriber<HttpResult<List<MenuItem>>>() {
                    override fun onFail(s: String?) {
                    }

                    override fun onSuccess(result: HttpResult<List<MenuItem>>) {
                        getView()?.getTopMenusSuccess(result.data)
                    }
                })
    }

    override fun getUserInfo() {
        addSubscriber(mModel.getUserInfo(),
                object : BaseSubscriber<HttpResult<UserInfoResult>>() {
                    override fun onFail(s: String?) {
                    }

                    override fun onSuccess(result: HttpResult<UserInfoResult>) {
                        getView()?.getUserInfoSuccess(result.data)
                    }

                })
    }

    override fun uploadImageIdcard(paths: String) {
        val body = createImageBody(paths, "image_file")
        addSubscriber(mModel.uploadImageIdcard(body),
                object : BaseSubscriber<ImageUploadIdCardResult<ImageIdCardListEntity>>() {
                    override fun onFail(s: String?) {
                        getView()?.uploadImageIdcardFail(s!!)
                    }

                    override fun onSuccess(result: ImageUploadIdCardResult<ImageIdCardListEntity>) {
                        for (imageEntity in result.data?.list!!) {
                            updateIdCard(imageEntity.avatar, imageEntity.avatar)
                        }
                        //getView()?.uploadImageIdcardSuccess(s);
                    }

                })
    }

    override fun applyForFostInfo(id_name: String, id_card: String, is_protocol: String, image_file: String) {
        getView()?.showLoading(null)
        val map = HashMap<String, Any>()
        if (image_file != null) {
            map["image_file"] = image_file
        }
        if (id_card != null) {
            map["id_card"] = id_card
        }
        if (id_name != null) {
            map["id_name"] = id_name
        }

        if (is_protocol != null) {
            map["is_protocol"] = is_protocol
        }

        addSubscriber(mModel.updateUserInfo(map),
                object : BaseSubscriber<HttpResult<UpdateUserEntity>>() {
                    override fun onFail(s: String?) {
                        getView()?.hideLoading()
                    }

                    override fun onSuccess(result: HttpResult<UpdateUserEntity>) {
                        getView()?.hideLoading()
                        // getView().updateSuccess(result.data.user);
                    }

                })
    }

    /**
     * 创建image body
     *
     * @param paths
     * @param key
     * @return
     */
    private fun createImageBody(paths: String, key: String): MultipartBody.Part {
        val imageFile: File
        imageFile = File(paths)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
        return MultipartBody.Part.createFormData(key, imageFile.name, requestFile)
    }

    fun updateIdCard(avatar: String?, avatar_small: String?) {
        applyForFostInfo("", "", avatar!!, avatar_small!!)
    }
}