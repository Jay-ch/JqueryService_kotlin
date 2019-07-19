package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/1/21
 * @email j-query@foxmail.com
 */
class AddressEntity:Serializable {
    var country: Int = 0
    var address: String? = null
    var tel: String? = null
    var mobile: String? = null
    var id_card: String? = null
    var id: Int = 0
    var name: String? = null
    var zip_code: String? = null
    var regions: List<RegionEntity>? = null
    var region: Int = 0
    var is_default: Boolean = false
    var areaId: Int = 0
    var addressDetail: String? = null
    var is_foreigner: Int = 0
}