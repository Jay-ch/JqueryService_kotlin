package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class ImageIdCardListEntity:Serializable {
    var error_code: Int = 0
    var error_desc: String? = null
    var list: List<ImageIdCardList>? = null

    inner class ImageIdCardList {
        var avatar: String? = null
    }
}