package com.app.network.interceptor

import okhttp3.Interceptor

//val noNetworkInterceptor by lazy {
//    object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                if (NetworkUtil.isConnected(SumAppHelper.getApplication())) {
//                    val request = chain.request()
//                    return chain.proceed(request)
//                } else {
//                    throw NoNetWorkException(ERROR.NETWORD_ERROR)
//                }
//            }
//        }
//}