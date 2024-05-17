package com.app.network.monitor

import android.util.Log
import okhttp3.Call
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * 这里是对网络请求的监控，比如dns、连接、请求各阶段的时间
 * 这里的网络异常只能监控到 HTTP 异常
 */
class NetworkEventListener : TimelineEventListener() {
    private val mHttpData = HttpData(timeline = timeline)

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        mHttpData.proxy = proxy.toString()
        mHttpData.inetSocketAddress = inetSocketAddress.toString()
        mHttpData.protocol = protocol?.toString()
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
        log("callFailed")
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        if (inBlackList(mHttpData.url)) {
            return
        }
        log("callEnd")
    }

    private fun log(message: String) {
        // todo
        Log.e(TAG, "$message ${timeline}")
        Log.e(TAG, "${mHttpData.string()}")
    }

    companion object {
        const val TAG = "NetworkMonitor"

        private val UTF8 = StandardCharsets.UTF_8

        @Throws(Exception::class)
        private fun getRequestParams(request: Request): String? {
            val requestBody = request.body
            val hasRequestBody = requestBody != null
            if (!hasRequestBody) {
                return null
            }
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            var param: String? = null
            if (isPlaintext(buffer) && charset != null) {
                val string = String(buffer.readByteArray(), charset)
                try {
                    param = URLDecoder.decode(string, "UTF-8")
                } catch (e: IllegalArgumentException) {
                    param = replacer(string)
                }
            }
            return param
        }
        private fun isPlaintext(buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = if (buffer.size < 64) buffer.size else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                true
            } catch (e: EOFException) {
                // Truncated UTF-8 sequence.
                false
            }
        }
    }
}

fun replacer(outBuffer: String): String {
    var data = outBuffer
    try {
        data = data.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
        data = data.replace("\\+".toRegex(), "%2B")
        data = URLDecoder.decode(data, "utf-8")
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return data
}