package com.jquery.service.android.listener

import java.io.File

/**
 * @author J.query
 * @date 2019/5/29
 * @email j-query@foxmail.com
 */
interface DownloadListener {

    /*  fun onStartDownload(length: Long)

      fun onProgress(progress: Int)

      fun onFail(errorInfo: String)*/

    fun onFinish(file: File?)

    fun onProgress(progress: Int?)

    fun onFailed(errMsg: String?)

    fun onPause(errMsg: String?)

    fun onCancel(errMsg: String?)
}