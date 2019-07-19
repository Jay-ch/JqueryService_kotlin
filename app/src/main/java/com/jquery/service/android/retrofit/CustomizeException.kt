package com.jquery.service.android.retrofit

/**
 * @author J.query
 * @date 2019/5/30
 * @email j-query@foxmail.com
 */
class CustomizeException : RuntimeException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}