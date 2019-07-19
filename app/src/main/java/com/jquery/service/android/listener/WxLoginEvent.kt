package com.jquery.service.android.listener

import com.tencent.mm.opensdk.modelbase.BaseResp

/**
 * @author J.query
 * @date 2019/1/23
 * @email j-query@foxmail.com
 */
class WxLoginEvent {
    private var baseResp: BaseResp

    constructor(baseResp: BaseResp) {
        this.baseResp = baseResp
    }

    fun getBaseResp(): BaseResp {
        return baseResp
    }
}