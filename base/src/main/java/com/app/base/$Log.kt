package com.app.base

import android.util.Log


private val TAG = BaseApplication.TAG

/**
 * 打印日志工具类
 *
 * @sample `Log.d("x")`
 */
object Log {
    private fun d(msg: String) = Log.d(TAG, msg)
    private fun e(msg: String) = Log.e(TAG, msg)
    private fun i(msg: String) = Log.i(TAG, msg)
    fun d(vararg msg: String) = d(msg.joinToString(" "))
    fun e(vararg msg: String) = e(msg.joinToString(" "))
    fun i(vararg msg: String) = i(msg.joinToString(" "))
}

/**
 * 打印日志三方工具类
 *
 * @sample `LogX.d("x")`
 */
typealias LogX = com.orhanobut.logger.Logger