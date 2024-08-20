package com.app.api

/**
 * 适配层：网络请求结果回调
 */
interface CallListener {
    fun onError(code: Int?, msg: String?) {}
    fun onCallStart() {}
    fun onCallEnd() {}
}