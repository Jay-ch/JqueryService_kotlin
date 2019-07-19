package com.jquery.service.android.permission

import android.Manifest

/**
 * @author J.query
 * @date 2018/9/11
 * @email j-query@foxmail.com
 */
class Permissions {
    val PICK_PERMISSION = 102
    val gallery_permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
}