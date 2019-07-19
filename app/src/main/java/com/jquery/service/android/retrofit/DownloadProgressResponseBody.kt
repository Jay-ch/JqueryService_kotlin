package com.jquery.service.android.retrofit

import com.orhanobut.logger.Logger
import com.jquery.service.android.listener.DownloadProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
class DownloadProgressResponseBody : ResponseBody {
    private var responseBody: ResponseBody? = null
    private val progressListener: DownloadProgressListener?
    private var bufferedSource: BufferedSource? = null

    constructor(responseBody: ResponseBody?, progressListener: DownloadProgressListener?) : super() {
        this.responseBody = responseBody
        this.progressListener = progressListener
    }

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength()!!
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody?.source()!!))
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
                //totalBytesRead += if (bytesRead != -1) bytesRead else 0
                Logger.t("DownloadUtil").d("已经下载的：" + totalBytesRead + "共有：" + responseBody?.contentLength())

                while (true) {
                    // 将Buffer中的数据写到outputStream对象中
                    if (bytesRead <= 0) {
                        totalBytesRead = 0
                        break
                    }
                    totalBytesRead += bytesRead

                }

                if (null != progressListener) {
                    progressListener?.update(totalBytesRead, responseBody?.contentLength()!!, bytesRead <= 0)
                }
                return bytesRead
            }
        }

    }
}