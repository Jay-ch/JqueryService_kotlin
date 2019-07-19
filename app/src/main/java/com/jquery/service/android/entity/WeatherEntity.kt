package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author j.query
 * @date 2018/10/18
 * @email j-query@foxmail.com
 */

data class WeatherEntity(val yesterday: YesterdayEntity,
                         val city: String,
                         val aqi: String,
                         val forecast: List<ForecastEntity>,
                         val ganmao: String,
                         val wendu: String) : Serializable
