package com.jquery.service.android.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * 根据百度地图API，根据地址得到经纬度
 * @author J.query
 * @date 2019/5/16
 * @email j-query@foxmail.com
 */
class AddressToLatitudeLongitude {
    private var address = "重庆"//地址
    private var Latitude = 45.7732246332393//纬度
    private var Longitude = 126.65771685544611//经度

    constructor(addr_str: String) : super() {
        this.address = addr_str
    }

    /*
     *根据地址得到地理坐标
     */
    fun getLatAndLngByAddress() {
        var addr = ""
        var lat = ""
        var lng = ""
        try {
            addr = java.net.URLEncoder.encode(address, "UTF-8")//编码
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        val url = String.format("http://api.map.baidu.com/geocoder/v2/?" + "address=%s&ak=4rcKAZKG9OIl0wDkICSLx8BA&output=json", addr)
        var myURL: URL? = null
        var httpsConn: URLConnection? = null
        //进行转码
        try {
            myURL = URL(url)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        try {
            httpsConn = myURL!!.openConnection() as URLConnection//建立连接
            if (httpsConn != null) {
                val insr = InputStreamReader(//传输数据
                        httpsConn.getInputStream(), "UTF-8")
                val br = BufferedReader(insr)
                var data: String? = null
                if ((data == br.readLine()) != null) {
                    println(data)
                    //这里的data为以下的json格式字符串,因为简单，所以就不使用json解析了，直接字符串处理
                    //{"status":0,"result":{"location":{"lng":118.77807440802562,"lat":32.05723550180587},"precise":0,"confidence":12,"level":"城市"}}
                    lat = data!!.substring(data.indexOf("\"lat\":") + "\"lat\":".length, data.indexOf("},\"precise\""))
                    lng = data.substring(data.indexOf("\"lng\":") + "\"lng\":".length, data.indexOf(",\"lat\""))
                }
                insr.close()
                br.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        this.Latitude = java.lang.Double.parseDouble(lat)
        this.Longitude = java.lang.Double.parseDouble(lng)
    }

    fun getLatitude(): Double? {
        return this.Latitude
    }

    fun getLongitude(): Double? {
        return this.Longitude
    }

    fun main(args: Array<String>) {
        val at = AddressToLatitudeLongitude("重庆市渝北区")
        at.getLatAndLngByAddress()
        println("" + at.getLatitude() + " " + at.getLongitude())
    }
}