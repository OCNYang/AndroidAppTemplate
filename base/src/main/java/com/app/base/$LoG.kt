package com.app.base

import android.util.Log

typealias LoG = com.orhanobut.logger.Logger

private val TAG by lazy { BaseApplication.get().packageName }

fun Log.d(msg: String) = Log.d(TAG, msg)
fun Log.e(msg: String) = Log.e(TAG, msg)
fun Log.i(msg: String) = Log.i(TAG, msg)