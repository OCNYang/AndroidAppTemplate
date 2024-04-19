package com.app.api

interface TemplateApi {

    // fun getTemplate(): Flow<List<String>>
    // 同步方法，非网络请求方法
    fun getTemplate(listener: CallListener? = null): List<String>

    // 异步方法，网络请求或查询本地数据
    suspend fun getDetailTemplate(listener: CallListener? = null): List<String>?
}