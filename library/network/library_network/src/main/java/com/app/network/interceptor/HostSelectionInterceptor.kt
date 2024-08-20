package com.app.network.interceptor

import androidx.multidex.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URISyntaxException
import kotlin.concurrent.Volatile

/**
 * 动态修改请求域名
 */

//class HostSelectionInterceptor(preferenceHelper: PreferenceHelper) : Interceptor {
//    @Volatile
//    private var host = HttpUrl.parse(BuildConfig.VERSION_CODE)
//    var preferenceHelper: PreferenceHelper
//
//    init {
//        this.preferenceHelper = preferenceHelper
//        setHostBaseUrl()
//    }
//
//    fun setHostBaseUrl() {
//        if (preferenceHelper.isProdEnvironment()) {
//            host = HttpUrl.parse(BuildConfig.PRODUCTION_BASE_URL)
//        } else {
//            host = HttpUrl.parse(BuildConfig.DEVELOPMENT_BASE_URL)
//        }
//    }
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Chain): Response {
//        var request: Request = chain.request()
//        if (host != null) {
//            var newUrl: HttpUrl? = null
//            try {
//                newUrl = request.url().newBuilder()
//                    .scheme(host!!.scheme())
//                    .host(host!!.toUrl().toURI().host)
//                    .build()
//            } catch (e: URISyntaxException) {
//                e.printStackTrace()
//            }
//            assert(newUrl != null)
//            request = request.newBuilder()
//                .url(newUrl!!)
//                .build()
//        }
//        return chain.proceed(request)
//    }
//}