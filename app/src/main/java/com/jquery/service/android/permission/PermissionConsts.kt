package com.jquery.service.android.permission

import android.Manifest
/**
 * 常用的一些权限组合
 * @author J.query
 * @date 2019/4/8
 * @email j-query@foxmail.com
 */
interface PermissionConsts {

        companion object {
            val CONTACTS = arrayOf<String>(Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS)

            val PHONE = arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ADD_VOICEMAIL)

            val CALENDAR = arrayOf<String>(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

            val CAMERA = arrayOf<String>(Manifest.permission.CAMERA)

            val SENSORS = arrayOf<String>(Manifest.permission.BODY_SENSORS)

            val LOCATION = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


            val STORAGE = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            val MICROPHONE = arrayOf<String>(Manifest.permission.RECORD_AUDIO)

            val SMS = arrayOf<String>(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS)
        }
}