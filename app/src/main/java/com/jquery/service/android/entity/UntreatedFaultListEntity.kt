package com.jquery.service.android.entity

import java.io.Serializable

/*data class HandledFaultListEntity(var id: Int,
                                  var brand: Int,
                                  var name: String, var photo: PhotoEntity) : Serializable*/
data class UntreatedFaultListEntity(var id: String, var type: String,
                                    var time: String,
                                    var address: String) :Serializable