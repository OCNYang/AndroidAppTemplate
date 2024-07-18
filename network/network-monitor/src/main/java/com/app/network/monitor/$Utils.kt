package com.app.network.monitor

import okhttp3.Request
import okio.Buffer
import java.io.EOFException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
        param = try {
            URLDecoder.decode(string, "UTF-8")
        } catch (e: IllegalArgumentException) {
            replacer(string)
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