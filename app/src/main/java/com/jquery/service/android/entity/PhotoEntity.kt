package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/3/21
 * @email j-query@foxmail.com
 */
data class PhotoEntity(var thumb: String,
                       var large: String,
                       var width: Int,
                       var height: Int) : Serializable