package com.app.network.monitor

import android.os.Bundle
import okhttp3.Call
import okhttp3.EventListener

data class HttpData(
    var request: String = "",
    var response: String = "",
    var proxy: String? = null,
    var rawCallHashCode: Int = 0,
    var inetSocketAddress: String? = null,
    var protocol: String? = null,
    var timeline: HashMap<String, Long> = hashMapOf(),

    // http or https
    var schema: String? = null,
    var domain: String? = null,
    // GET,POST
    var method: String? = null,

    // 服务名 api/rest/xxx/xxx
    var methodName: String? = null,

    // url
    var url: String? = null,

    // 请求参数
    var requestParams: String? = null,

    // 错误信息
    var errorMsg: String? = null,

    // Http状态码 200 ,404
    var responseCode: Int? = null,
) {
    val startTime: Long = timeline[TimelineEventListener.START] ?: 0

    // DNS的耗时
    var dnsCost: Long? = timeline[EventListener::dnsEnd.name]?.minus(timeline[EventListener::dnsStart.name] ?: 0)

    // 建连耗时
    var connectCost: Long? = timeline[EventListener::connectEnd.name]?.minus(timeline[EventListener::connectStart.name] ?: 0)

    // 服务器响应耗时
    var responseCost: Long = timeline[EventListener::responseBodyEnd.name]?.minus(timeline[EventListener::responseBodyStart.name] ?: 0) ?: 0

    // 网络请求总耗时
    var totalCost: Long = (timeline[EventListener::callEnd.name] ?: timeline[EventListener::callFailed.name])?.minus(timeline[EventListener::callStart.name] ?: 0) ?: 0

    // TraceId,用于全链路监控
    var traceId: String? = null

    // 响应头
    fun updateByCall(call: Call) {
        schema = call.request().url.scheme
        domain = call.request().url.host
        method = call.request().method
        methodName = call.request().url.encodedPath
        url = call.request().url.toString()
    }

    val isFirst: Boolean
        get() = (dnsCost != null && dnsCost!! >= 0) && (connectCost != null && connectCost!! > 0)

    fun string(): String {
        return "HttpData(${toString()}, dnsCost=$dnsCost, connectCost=$connectCost, responseCost=$responseCost, totalCost=$totalCost, traceId='$traceId', isFirst=$isFirst)"
    }


    fun toBundle(): Bundle {
        return Bundle().apply {
            putString("request", request)
            putString("response", response)
            putString("proxy", proxy)
            putString("inetSocketAddress", inetSocketAddress)
            putString("protocol", protocol)
            putString("timeline", timeline.toString())
            putString("schema", schema)
            putString("domain", domain)
            putString("method", method)
            putString("methodName", methodName)
            putString("url", url)
            putString("requestParams", requestParams)
            putString("errorMsg", errorMsg)
        }
    }

}