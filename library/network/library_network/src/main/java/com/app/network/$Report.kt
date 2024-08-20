package com.app.network

import com.app.base.LogX
import com.app.network.error.ExceptionHandler
import com.app.network.monitor.HttpData
import com.app.network.monitor.NetworkEventListener
import retrofit2.MonitorCallAdapterFactory

/**
 * 网络请求埋点上报，根据需求自定义
 */
object Report : MonitorCallAdapterFactory.ErrorHandler, MonitorCallAdapterFactory.SuccessHandler {
    override fun invoke(requestBodyStr: String, e: Exception, requestHashCode: Int?) {
        val handleException = ExceptionHandler.handleException(e)

        val httpData = (if (requestHashCode != null && requestHashCode != 0) {
            (NetworkEventListener.popNetworkMonitorHistoryByRequestHashCode(requestHashCode)?.let {
                if (it.responseCode == null || it.responseCode == 200) {
                    it.responseCode = handleException.errCode
                }
                it.errorMsg = "${it.errorMsg ?: ""} ${handleException.toString()}"
                it
            })
        } else {
            null
        }) ?: HttpData(responseCode = handleException.errCode, errorMsg = handleException.toString(), request = requestBodyStr)

        // todo 这里处理网络请求的错误上报
        LogX.e("网络请求错误：$httpData")
    }

    override fun invoke(requestHashCode: Int?) {
        requestHashCode?.let { NetworkEventListener.popNetworkMonitorHistoryByRequestHashCode(it) }
    }
}