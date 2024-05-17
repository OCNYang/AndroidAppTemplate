package com.app.network.monitor

import android.text.TextUtils
import java.net.InetAddress
import java.net.URI
import java.net.URISyntaxException

fun isLocalhost(url: String?): Boolean {
    var isLocal = false
    try {
        val uri = URI(url)
        val host = uri.host
        if (host != null) {
            if (host == "localhost") {
                isLocal = true
            } else {
                val inetAddress = InetAddress.getByName(host)
                if (inetAddress.isLoopbackAddress) {
                    isLocal = true
                }
            }
        }
    } catch (e: URISyntaxException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return isLocal
}

fun inBlackList(url: String?): Boolean {
    return url == null || TextUtils.isEmpty(url.trim())
}