package com.app.network

import android.text.TextUtils
import okhttp3.Request
import okio.Buffer
import java.io.EOFException
import java.net.InetAddress
import java.net.URI
import java.net.URISyntaxException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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


private val UTF8 = StandardCharsets.UTF_8

@Throws(Exception::class)
 fun getRequestParams(request: Request): String? {
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

 fun isPlaintext(buffer: Buffer): Boolean {
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