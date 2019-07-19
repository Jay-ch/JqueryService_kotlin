package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/4/30
 * @email j-query@foxmail.com
 */
data class RecentWorkItem(var day: String,var title: String,
                          var display_type: Int, var address: String, var time: String) : Serializable