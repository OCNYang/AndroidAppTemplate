package com.app.network.api

import com.app.api.bean.BaseResult
import com.app.network.HttpManager
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

/**
 * 这里没有直接继承 [@link com.app.api.TemplateApi]
 * 是因为 [@link com.app.api.TemplateApi] 的方法并不是都是网络请求，参数和网络请求参数也不同
 */
interface TemplateService {

    @HTTP(method = "GET", path = PATH.GET_USER_INFO, hasBody = false)
    suspend fun getDetailTemplate(): BaseResult<List<String>>

    @POST(PATH.GET_USER_INFO)
    suspend fun getDetailTemplateX(@Body requestBody: RequestBody): BaseResult<List<String>>

    companion object {
        val instance by lazy { HttpManager.create(TemplateService::class.java) }
    }
}