package com.app.network.monitor

import androidx.collection.SparseArrayCompat
import androidx.collection.size
import okhttp3.Call
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * 这里是对网络请求的监控，比如dns、连接、请求各阶段的时间
 * 这里的网络异常只能监控到 HTTP 异常
 *
 * @param justLogError 是否只记录错误
 * ⚠️ 如果只记录错误，当遇到非 HTTP 异常时（比如 JsonDataException）时，将会丢失这里监控的请求信息。
 * 因为 部分错误 是在网络请求结束后产生的（比如 数据解析）。
 */
class NetworkEventListener(private val justLogError: Boolean = false) : TimelineEventListener() {

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        with(call.getHttpData()) {
            this.proxy = proxy.toString()
            this.inetSocketAddress = inetSocketAddress.toString()
            this.protocol = protocol?.toString()
            this.rawCallHashCode = call.hashCode()
        }
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        with(call.getHttpData()){
            this.request = request.toString()
            try {
                this.requestParams = getRequestParams(request)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this.updateByCall(call)
        }
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        with(call.getHttpData()){
            this.response = response.toString()
            this.responseCode = response.code
        }
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        val httpData = call.getHttpData()

        if (httpData.url.isNullOrBlank()) {
            return
        }

        val stringBuilder = StringBuilder()
        stringBuilder
            .append(",Ex:")
            .append(ioe.javaClass.simpleName)
            .append(",Msg:")
            .append(ioe.message)
            .append(",trace:")
        val stackTraceElements = ioe.stackTrace
        if (stackTraceElements.isNotEmpty()) {
            stringBuilder.append(stackTraceElements[0].toString())
        }

        httpData.errorMsg = stringBuilder.toString()
        log(call)
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        val httpData = call.getHttpData()
        if (httpData.url.isNullOrBlank()) {
            return
        }
        if (!justLogError) {
            log(call)
        }
    }

    private fun log(call: Call) {

        call.request()
        // httpHistory.put(mHttpData.rawCallHashCode, mHttpData)

        print("长度：${httpHistory.size} $httpHistory")
    }

    companion object {
        @JvmStatic
        var LOGS_SIZE_THRESHOLD: Int = 99

        @JvmStatic
        var LOGS_OUT_TIME = 1000 * 60 * 5

        @JvmStatic
        private val httpHistory: SparseArrayCompat<HttpData> = SparseArrayCompat()
            get() {
                if (field.size() > LOGS_SIZE_THRESHOLD) {
                    val currentTime = System.currentTimeMillis()
                    for (i in 0 until field.size()) {
                        val key = field.keyAt(i)
                        val value = field.valueAt(i)
                        if (currentTime - value.startTime > LOGS_OUT_TIME) {
                            field.remove(key)
                        }
                    }
                }
                return field
            }

        @JvmStatic
        fun Call.getHttpData() = httpHistory[hashCode()] ?: HttpData().apply { httpHistory.put(this@getHttpData.hashCode(), this) }
        fun Call.getTimeLine() = getHttpData().timeline

        @JvmStatic
        fun findMonitorLogsByRawCallHashCode(rawCallHashCode: Int): HttpData? {
            val httpData = httpHistory[rawCallHashCode]
            httpHistory.remove(rawCallHashCode)
            return httpData
        }


        @JvmStatic
        fun removeMonitorLogsByRawCallHashCode(rawCallHashCode: Int) {
            httpHistory.remove(rawCallHashCode)
        }

        @JvmStatic
        fun clearMonitorLogs() {
            httpHistory.clear()
        }
    }
}

