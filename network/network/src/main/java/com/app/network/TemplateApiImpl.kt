package com.app.network

import com.app.api.CallListener
import com.app.api.TemplateApi
import com.app.network.api.TemplateService
import com.google.auto.service.AutoService

@AutoService(value = [TemplateApi::class])
class TemplateApiImpl : TemplateApi {

    private val service by lazy { TemplateService.instance }

    override fun getTemplate(listener: CallListener?): List<String> {
        TODO("Not yet implemented")
    }

    /**
     * 有参数时：
     * service.getDetailTemplateX(convert2RequestBody("a" to "b"))
     */
    override suspend fun getDetailTemplate(listener: CallListener?): Any? {
        val data = requestFlow(
            requestCall = {
//                service.getDetailTemplate()
                service.getDetailTemplateX(convert2RequestBody("a" to "b"))
            },
            listener = listener,
        )
        return data
    }
}