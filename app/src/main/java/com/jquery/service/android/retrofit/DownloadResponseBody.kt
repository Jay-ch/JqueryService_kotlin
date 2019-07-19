package com.jquery.service.android.retrofit

import com.orhanobut.logger.Logger
import com.jquery.service.android.listener.DownloadListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException
import java.util.concurrent.Executor

/**
 * @author J.query
 * @date 2019/5/29
 * @email j-query@foxmail.com
 */
class DownloadResponseBody : ResponseBody {
    private val responseBody: ResponseBody
    private val downloadListener: DownloadListener?
    private var bufferedSource: BufferedSource? = null
    private val executor: Executor?

    constructor(responseBody: ResponseBody, executor: Executor, downloadListener: DownloadListener) : super() {
        this.responseBody = responseBody
        this.downloadListener = downloadListener
        this.executor = executor
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                if (null != downloadListener) {
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    //totalBytesRead += if (bytesRead != -1) bytesRead else 0
                    /*  while (true) {
                          // 将Buffer中的数据写到outputStream对象中
                          if (bytesRead <= 0) {
                              totalBytesRead = 0
                              break
                          }
                          totalBytesRead += bytesRead

                      }*/
                    //logger.log(buffer.clone().readString(charset!!), LogUtil.JSON)
                    //LogUtil.e("DownloadUtil已经下载的：" + totalBytesRead + "共有：" + responseBody.contentLength())

                    Logger.t("DownloadUtil").d("已经下载的：" + totalBytesRead + "共有：" + responseBody.contentLength())
                    val progress = (totalBytesRead * 100 / responseBody.contentLength()).toInt()
                    if (executor != null) {
                        executor.execute {
                            downloadListener.onProgress(progress)
                        }
                    } else {
                        downloadListener.onProgress(progress)
                    }
                }
                return bytesRead
            }
        }
    }
}

