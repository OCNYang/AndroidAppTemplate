package com.app.base

import android.widget.Toast

fun showToast(msg: String) {
    showToast(msg, false)
}

fun showToast(msg: String, long: Boolean = false) {
    Toast.makeText(BaseApplication.get(), msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}