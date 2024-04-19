package com.app.api

interface CallListener {
    fun onError(code: Int?, msg: String?) {}
    fun onCallStart() {}
    fun onCallEnd() {}
}