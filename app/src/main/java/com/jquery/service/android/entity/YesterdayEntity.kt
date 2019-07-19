package com.jquery.service.android.entity

import java.io.Serializable

data class YesterdayEntity(val date: String,
                      val high: String,
                      val fx: String,
                      val low: String,
                      val fl: String,
                      val type: String) : Serializable

