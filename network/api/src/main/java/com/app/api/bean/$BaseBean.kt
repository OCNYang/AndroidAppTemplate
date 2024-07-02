package com.app.api.bean

import com.squareup.moshi.JsonClass

/**
 * 网络请求的返回数据格式的基类
 */
@JsonClass(generateAdapter = false)
data class BaseResult<T>(
    val code: Int = 0,
    val msg: String = "",
    val data: T? = null,
    val success: Boolean = false,
) {
    val isSuccess = code == 200
}