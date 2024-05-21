package com.app.network

import com.app.base.LogX
import com.app.network.monitor.HttpData
import com.app.network.monitor.NetworkEventListener
import retrofit2.MonitorCallAdapterFactory

object Report : MonitorCallAdapterFactory.ErrorHandler {
    override fun invoke(requestBodyStr: String, e: Exception, rawCallId: Int?) {
        val rawCallID: Int? = rawCallId

        val httpData = (if (rawCallID != null && rawCallID != 0) {
            (NetworkEventListener.NETWORK_MONITOR_LOGS_LIST[rawCallID]?.let {
                if (it.responseCode != null && it.responseCode != 200) {
                    it.responseCode = -1
                }
                it.errorMsg = "${it.errorMsg} ${e.toString()}"
                it
            })
        } else {
            null
        }) ?: HttpData(responseCode = -1, errorMsg = e.toString(), request = requestBodyStr)

        // todo 这里处理网络请求的错误上报
        LogX.e("网络请求错误：$httpData")
    }
}