package com.app.base

/**
 * 命名代码块
 * 对写在一起功能不同的代码，进行分块，并进行命名
 */
infix operator fun String.invoke(block: () -> Unit) {
    block.invoke()
}

infix operator fun Int.invoke(block: () -> Unit) {
    block.invoke()
}

fun first(block: () -> Unit) = block.invoke()
fun next(block: () -> Unit) = block.invoke()
fun last(block: () -> Unit) = block.invoke()