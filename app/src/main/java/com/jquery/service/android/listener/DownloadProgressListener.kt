package com.jquery.service.android.listener

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
interface DownloadProgressListener {

     fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}