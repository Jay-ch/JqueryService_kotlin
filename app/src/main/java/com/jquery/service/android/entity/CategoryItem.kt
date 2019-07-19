package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class CategoryItem : Serializable{
    var title: String? = null
    var images: List<ImageItemEntity>? = null
    var keywords: String? = null
    var media_info: MediaInfo? = null
    var description: String? = null
    var read_count: Int = 0
    var display_type: Int = 0
    var id: Long = 0
    var created_at: Long = 0
    var url: String? = null
    var object_type: String? = null
    var category_id: Int = 0
    var summary: String? = null
    var user: UserDetailEntity? = null
    var relation_goods_count: Int = 0
    var is_praise: Int = 0
    var is_collection: Int = 0
    var praise: Int = 0//点赞数量
    var like_count: Int = 0//收藏数量


    inner class MediaInfo : Serializable {
        var url: String? = null
    }
}