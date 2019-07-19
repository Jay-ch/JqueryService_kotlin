package com.jquery.service.android.entity

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class ImageListEntity {
    var list: List<ImageEntity>? = null

    inner class ImageEntity {
        var avatar: String? = null
        var avatar_small: String? = null
    }
}