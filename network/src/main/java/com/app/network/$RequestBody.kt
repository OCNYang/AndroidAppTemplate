package com.app.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

fun JSONObject.toOKHttpRequestBody() = toString().toRequestBody("application/json; charset=utf-8".toMediaType())

fun convert2RequestBody(vararg pairs: Pair<String, Any>): RequestBody {
    val jsonObject = JSONObject()
    pairs.forEach { try {
        jsonObject.put(it.first,it.second)
    }catch (e:Exception){
        e.printStackTrace()
    } }
    return jsonObject.toOKHttpRequestBody()
}

fun String.toOKHttpRequestBody() = toRequestBody("text/plain".toMediaType())

fun List<File>.toMultipartBodyParts(): List<MultipartBody.Part> = mapIndexed { index, it ->
    it.toMultipartBodyParts()
}

fun File.toMultipartBodyParts(): MultipartBody.Part =
    MultipartBody.Part.createFormData("files", name, asRequestBody("image/${extension}".toMediaType()))

fun File.toMultipartBodyPartsWithLogs(): MultipartBody.Part =
    MultipartBody.Part.createFormData("files", name, asRequestBody("text/${extension}".toMediaType()))

fun <A> List<A>.list2JsonArray(): JSONArray {
    val jsonArray = JSONArray()
    this.forEach {
        jsonArray.put(it)
    }
    return jsonArray
}