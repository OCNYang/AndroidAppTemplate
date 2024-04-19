package com.app.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder

class PublicParameterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder().apply {
            addHeader("device-type", "android")
            addHeader("app-version", "") // todo
            addHeader("device-id", "") // todo
            addHeader("device-os-version", "") // todo 获取手机系统版本号
            addHeader("device-name", URLEncoder.encode("", "UTF-8")) // todo 获取设备类型
        }

        return chain.proceed(newBuilder.build())
    }
}