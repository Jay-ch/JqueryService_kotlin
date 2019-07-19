package com.jquery.service.android.entity

import java.io.Serializable

data class ForecastEntity(val date: String,
                     val high: String,
                     val fengli: String,
                     val low: String,
                     val fengxiang: String,
                     val type: String
                     ) :Serializable