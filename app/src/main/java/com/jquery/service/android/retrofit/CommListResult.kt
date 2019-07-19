package com.jquery.service.android.retrofit

import com.jquery.service.android.entity.Paged

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class CommListResult <T>{
    var list: List<T>? = null
    var total_number: Int = 0
    var paged: Paged? = null
}