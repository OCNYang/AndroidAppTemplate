package com.app.base

import android.util.Log

typealias LogX = com.orhanobut.logger.Logger

private val TAG = BaseApplication.TAG

object Log {
    fun d(msg: String) = Log.d(TAG, msg)
    fun e(msg: String) = Log.e(TAG, msg)
    fun i(msg: String) = Log.i(TAG, msg)
}