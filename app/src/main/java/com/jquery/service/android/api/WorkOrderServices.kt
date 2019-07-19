package com.jquery.service.android.api

import com.jquery.service.android.entity.WorkOrderEntity
import com.jquery.service.android.retrofit.HttpResult
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface WorkOrderServices {
    /**
     * 第三方登录
     *
     * @param body
     * @return
     */
    @POST("ecapi.auth.workorder")
    fun workOrder(@Body body: HashMap<String, String>): Flowable<HttpResult<WorkOrderEntity>>
}
