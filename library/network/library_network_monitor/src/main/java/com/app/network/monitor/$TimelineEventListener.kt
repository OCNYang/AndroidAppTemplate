package com.app.network.monitor

import com.app.network.monitor.NetworkEventListener.Companion.getTimeLine
import okhttp3.Call
import okhttp3.Connection
import okhttp3.EventListener
import okhttp3.Handshake
import okhttp3.HttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

abstract class TimelineEventListener: EventListener() {
    override fun cacheConditionalHit(call: Call, cachedResponse: Response) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.cacheConditionalHit(call, cachedResponse)
    }

    override fun cacheHit(call: Call, response: Response) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.cacheHit(call, response)
    }

    override fun cacheMiss(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.cacheMiss(call)
    }

    override fun callEnd(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.callEnd(call)
    }

    override fun callFailed(call: Call, ioe: IOException) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.callFailed(call, ioe)
    }

    override fun callStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.callStart(call)
    }

    override fun canceled(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.canceled(call)
    }

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
    }

    override fun connectFailed(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?, ioe: IOException) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.connectStart(call, inetSocketAddress, proxy)
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.connectionAcquired(call, connection)
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.connectionReleased(call, connection)
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.dnsEnd(call, domainName, inetAddressList)
    }

    override fun dnsStart(call: Call, domainName: String) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.dnsStart(call, domainName)
    }

    override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.proxySelectEnd(call, url, proxies)
    }

    override fun proxySelectStart(call: Call, url: HttpUrl) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.proxySelectStart(call, url)
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.requestBodyEnd(call, byteCount)
    }

    override fun requestBodyStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.requestBodyStart(call)
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.requestFailed(call, ioe)
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.requestHeadersEnd(call, request)
    }

    override fun requestHeadersStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.requestHeadersStart(call)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.responseBodyEnd(call, byteCount)
    }

    override fun responseBodyStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.responseBodyStart(call)
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.responseFailed(call, ioe)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.responseHeadersEnd(call, response)
    }

    override fun responseHeadersStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.responseHeadersStart(call)
    }

    override fun satisfactionFailure(call: Call, response: Response) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.satisfactionFailure(call, response)
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.secureConnectEnd(call, handshake)
    }

    override fun secureConnectStart(call: Call) {
        call.getTimeLine()[object {}.javaClass.enclosingMethod.name] = System.currentTimeMillis()
        super.secureConnectStart(call)
    }
}