package com.app.network

import com.app.api.CallListener
import com.app.api.TemplateApi
import com.app.network.api.TemplateService
import com.google.auto.service.AutoService

/**
 * 网络请求的具体实现
 *
 * 这里采用的是
 * okhttp：[HttpManager.initOkHttpClient] 拦截器见 [com.app.network.interceptor]
 * Retrofit：[HttpManager.mRetrofit] [TemplateService]
 * kotlin 协程：[requestFlow]
 * json 解析：[HttpManager.moshi] 解析的适配转换见 [com.app.network.adapter]
 *
 * **其他**
 * 网络监控见：[com.app.network.monitor.NetworkEventListener]
 */
@AutoService(value = [TemplateApi::class])
class TemplateApiImpl : TemplateApi {

    private val service by lazy { TemplateService.instance }

    override fun getTemplate(listener: CallListener?): List<String> {
        // TODO
        return listOf()
    }

    /**
     * 有参数时：
     * service.getDetailTemplateX(convert2RequestBody("a" to "b"))
     */
    override suspend fun getDetailTemplate(listener: CallListener?): Any? {
        val data = requestFlow(
            requestCall = {
                service.getDetailTemplate()
//                service.getDetailTemplateX(convert2RequestBody("a" to "b"))
            },
            listener = listener,
        )
        return data
    }
}