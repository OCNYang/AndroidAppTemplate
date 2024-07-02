package com.app.base

import android.widget.Toast

/**
 * Toast 的封装函数
 */
fun showToast(msg: String) {
    showToast(msg, false)
}

fun showToast(msg: String, long: Boolean = false) {
    Toast.makeText(BaseApplication.INSTANCE, msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}