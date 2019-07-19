package com.jquery.service.android.retrofit

import com.jquery.service.android.logger.LogUtil
import com.jquery.service.android.logger.LogUtilHelper
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.INFO
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit
/**
 * @author j.query
 * @date 2018/12/25
 * @email j-query@foxmail.com
 */
class HttpLoggingInterceptorM @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {

    @Volatile
    private var level = Level.NONE

    enum class Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
        `</pre> *
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
        `</pre> *
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         *
         *
         * Example:
         * <pre>`--> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
        `</pre> *
         */
        BODY
    }

    interface Logger {
        fun log(message: String, @LogUtil.LogType type: Int)

        companion object {

            /**
             * A [Logger] defaults output appropriate for the current platform.
             */
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String, @LogUtil.LogType type: Int) {
                    Platform.get()
                            .log(INFO, message, null)
                }
            }
        }
    }

    /**
     * Change the level at which this interceptor logs.
     */
    fun setLevel(level: Level?): HttpLoggingInterceptorM {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        /*
            打印开始开始请求
         */
        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        val requestMessage = request.method() + ' '.toString() + request.url() + ' '.toString() + protocol
        var requestStartMessage = "--> $requestMessage"
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log("start sending Request: $requestStartMessage", LogUtil.D)


        /*
            请求中,计算请求时间并打印
         */
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("<-- HTTP FAILED: $e", LogUtil.D)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        /*
            请求完后打印RequestBody
         */
        LogUtilHelper.printDivider(LogInterceptor.INTERCEPTOR_TAG_STR)
        logger.log(requestStartMessage, LogUtil.D)

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType()!!, LogUtil.D)
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: " + requestBody.contentLength(), LogUtil.D)
                }
            }

            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logger.log(name + ": " + headers.value(i), LogUtil.D)
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method(), LogUtil.D)
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)", LogUtil.D)
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (isPlaintext(buffer)) {
                    logger.log(buffer.readString(charset!!), LogUtil.D)
                    logger.log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)",
                            LogUtil.D)
                } else {
                    logger.log("--> END "
                            + request.method()
                            + " (binary "
                            + requestBody.contentLength()
                            + "-byte body omitted)", LogUtil.D)
                }
            }
        }

        /*
            请求完后打印ResponseBody
         */
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"
        logger.log("<-- " + response.code() + ' '.toString() + response.message() + ' '.toString() + response.request()
                .url() + " (" + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'.toString(), LogUtil.D)

        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i), LogUtil.D)
                i++
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("<-- END HTTP", LogUtil.D)
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)", LogUtil.D)
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {

                        logger.log("Couldn't decode the response body; charset is likely malformed.", LogUtil.D)
                        logger.log("<-- END HTTP", LogUtil.D)

                        return response
                    }

                }

                if (!isPlaintext(buffer)) {

                    logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)", LogUtil.D)
                    return response
                }

                if (contentLength != 0L) {

                    logger.log(buffer.clone()
                            .readString(charset!!), LogUtil.JSON)
                }

                logger.log("<-- END HTTP: " + requestMessage + " (" + buffer.size() + "-byte body)", LogUtil.D)
            }

            LogUtilHelper.printDivider(LogInterceptor.INTERCEPTOR_TAG_STR)
        }

        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("utf-8", ignoreCase = true)
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        @Throws(EOFException::class)
        internal fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64) buffer.size() else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }

        }
    }
}