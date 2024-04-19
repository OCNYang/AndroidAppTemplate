package com.app.api.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResult<T>(
    val code: Int = 0,
    val msg: String = "",
    val data: T? = null,
    val success: Boolean = false,
    val id:String = ""
) {
    val isSuccess = code == 200
}