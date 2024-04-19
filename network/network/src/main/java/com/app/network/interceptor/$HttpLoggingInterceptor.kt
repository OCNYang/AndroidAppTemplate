package com.app.network.interceptor

import android.util.Log
import com.app.base.BaseApplication
import okhttp3.logging.HttpLoggingInterceptor

// 日志拦截器
val logInterceptor by lazy {
    val interceptor = HttpLoggingInterceptor { message: String ->
        Log.i("okhttp", "data:$message")
    }
    if (BaseApplication.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
    }
    interceptor
}

