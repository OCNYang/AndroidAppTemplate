package com.app.template.comm

import com.app.base.MMKVKEY

/**
 * 存放本地系列化的 key
 */

object MMKVKey {
    val TOKEN = MMKVKEY("token", String::class.java, "")
}