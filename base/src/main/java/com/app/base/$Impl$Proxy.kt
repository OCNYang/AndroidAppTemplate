package com.app.base

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * 接口空实现委托：为了在实现接口时，只实现部分方法
 * @sample
 * `object : ActivityLifecycleCallbacks by noOpDelegate(){...}`
 *
 */
internal inline fun <reified T : Any> noOpDelegate(): T {
    val javaClass = T::class.java
    return Proxy.newProxyInstance(
        javaClass.classLoader, arrayOf(javaClass), NO_OP_HANDLER
    ) as T
}

private val NO_OP_HANDLER = InvocationHandler { _, _, _ -> }