package com.app.network.monitor

import androidx.collection.SparseArrayCompat
import okhttp3.Call
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.LinkedList
import java.util.Queue

/**
 * 这里是对网络请求的监控，比如 dns、连接、请求各阶段的时间
 * 这里的网络异常只能监控到 HTTP 异常
 */
class NetworkEventListener : TimelineEventListener() {

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
        with(call.getHttpData()) {
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
        with(call.getHttpData()) {
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
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        val httpData = call.getHttpData()
        if (httpData.url.isNullOrBlank()) {
            return
        }
    }

    companion object {
        @JvmStatic
        var NetworkMonitorHistoryMaxSize: Int = 10

        private val httpHistory: SparseArrayCompat<HttpData> = SparseArrayCompat()
        private val httpHistoryQueue: Queue<Int> = object : LinkedList<Int>() {
            override fun offer(p0: Int?): Boolean {
                while (this.size >= NetworkMonitorHistoryMaxSize) {
                    val id = this.poll()
                    removeNetworkMonitorHistoryByRequestHashCode(id)
                }
                return super.offer(p0)
            }
        }

        internal fun Call.getHttpData() = httpHistory[this.request().hashCode()] ?: HttpData().apply {
            val requestHashCode = this@getHttpData.request().hashCode()
            httpHistoryQueue.offer(requestHashCode)
            httpHistory.put(this@getHttpData.request().hashCode(), this)
        }

        internal fun Call.getTimeLine() = getHttpData().timeline

        @JvmStatic
        fun popNetworkMonitorHistoryByRequestHashCode(requestHashCode: Int): HttpData? {
            val httpData = httpHistory[requestHashCode]
            removeNetworkMonitorHistoryByRequestHashCode(requestHashCode)
            return httpData
        }

        private fun removeNetworkMonitorHistoryByRequestHashCode(requestHashCode: Int) {
            httpHistoryQueue.remove(requestHashCode)
            httpHistory.remove(requestHashCode)
        }

        @JvmStatic
        fun clearNetworkMonitorHistory() {
            httpHistory.clear()
            httpHistoryQueue.clear()
        }
    }
}

