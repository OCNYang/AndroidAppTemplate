package com.app.api

/**
 * 适配层：网络请求的方法定义
 * 采用 SPI 机制实现，所以整个 API-lib 只做方法声明，不关心具体实现。
 *
 * 具体实现的 lib 只需要在 `resources/META-INF.services` 文件中声明好此类的全路径 [com.app.api.TemplateApi]；
 * 详见具体实现模块 network-lib
 *
 * 使用 `val api = ServiceLoader.load(TemplateApi::class.java).first()` ，然后直接调用具体方法
 *
 */
interface TemplateApi {

    // fun getTemplate(): Flow<List<String>>
    // 同步方法，非网络请求方法
    fun getTemplate(listener: CallListener? = null): List<String>

    // 异步方法，网络请求或查询本地数据
    suspend fun getDetailTemplate(listener: CallListener? = null): Any?
}