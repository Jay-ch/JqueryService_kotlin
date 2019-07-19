package com.jquery.service.android.entity

import java.io.Serializable

/**
 * @author J.query
 * @date 2019/3/11
 * @email j-query@foxmail.com
 */
class SerialAssistBean:Serializable {
    //private const val serialVersionUID = -5620661009186692227L
    private var isTxt = true
    private var SendTxtA = "COMA"
    private var SendTxtB = "COMB"
    private var SendTxtC = "COMC"
    private var SendTxtD = "COMD"
    private var SendHexA = "AA"
    private var SendHexB = "BB"
    private var SendHexC = "CC"
    private var SendHexD = "DD"
    var sTimeA = "500"
    var sTimeB = "500"
    var sTimeC = "500"
    var sTimeD = "500"
    fun isTxt(): Boolean {
        return isTxt
    }

    fun setTxtMode(isTxt: Boolean) {
        this.isTxt = isTxt
    }

    fun getSendA(): String {
        return if (isTxt) {
            SendTxtA
        } else {
            SendHexA
        }
    }

    fun getSendB(): String {
        return if (isTxt) {
            SendTxtB
        } else {
            SendHexB
        }
    }

    fun getSendC(): String {
        return if (isTxt) {
            SendTxtC
        } else {
            SendHexC
        }
    }

    fun getSendD(): String {
        return if (isTxt) {
            SendTxtD
        } else {
            SendHexD
        }
    }

    fun setSendA(sendA: String) {
        if (isTxt) {
            SendTxtA = sendA
        } else {
            SendHexA = sendA
        }
    }

    fun setSendB(sendB: String) {
        if (isTxt) {
            SendTxtB = sendB
        } else {
            SendHexB = sendB
        }
    }

    fun setSendC(sendC: String) {
        if (isTxt) {
            SendTxtC = sendC
        } else {
            SendHexC = sendC
        }
    }

    fun setSendD(sendD: String) {
        if (isTxt) {
            SendTxtD = sendD
        } else {
            SendHexD = sendD
        }
    }
}