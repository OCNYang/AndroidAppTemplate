package com.app.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 请求重试拦截器
 */
class RetryInterceptor(private val maxRetry: Int = 2) : Interceptor {

    private var retryNum = 0 // 假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response = chain.proceed(request)
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            response = chain.proceed(request)
        }
        retryNum = 0
        return response
    }
}