package com.app.network.monitor

import androidx.collection.SparseArrayCompat
import com.app.network.getRequestParams
import com.app.network.inBlackList
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
    private val mHttpData = HttpData(timeline = timeline)

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        mHttpData.proxy = proxy.toString()
        mHttpData.inetSocketAddress = inetSocketAddress.toString()
        mHttpData.protocol = protocol?.toString()
        mHttpData.rawCallHashCode = call.hashCode()
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        mHttpData.request = request.toString()
        try {
            mHttpData.requestParams = getRequestParams(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mHttpData.updateByCall(call)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        mHttpData.response = response.toString()
        mHttpData.responseCode = response.code
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        if (inBlackList(mHttpData.url)) {
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

        mHttpData.errorMsg = stringBuilder.toString()
        log()
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        if (inBlackList(mHttpData.url)) {
            return
        }
        if (!justLogError) {
            log()
        }
    }

    private fun log() {
        NETWORK_MONITOR_LOGS_LIST.put(mHttpData.rawCallHashCode, mHttpData)
    }

    companion object {
        @JvmStatic
        val NETWORK_MONITOR_LOGS_LIST: SparseArrayCompat<HttpData> = SparseArrayCompat()
            get() {
                if (field.size() > 99) {
                    val currentTime = System.currentTimeMillis()
                    for (i in 0 until field.size()) {
                        val key = field.keyAt(i)
                        val value = field.valueAt(i)
                        if (currentTime - value.startTime > 1000 * 60 * 5) {
                            field.remove(key)
                        }
                    }
                }
                return field
            }
    }
}

