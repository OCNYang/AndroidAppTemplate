package com.app.base

/**
 * 命名代码块
 *
 * 对写在一起功能不同的代码，进行分块，并进行命名
 *
 * @sample
 * ```
 * fun main(args: Array<String>) {
 *     // 第一种
 *     "init" {
 *         // 初始化代码
 *     }
 *     // 第二种
 *     1 {
 *         // 第一步
 *     }
 *     // 第三种
 *     first {
 *         // 具有先后持续的代码
 *     }
 * }
 * ```
 */
infix operator fun String.invoke(block: () -> Unit) {
    block.invoke()
}

infix operator fun Int.invoke(block: () -> Unit) {
    block.invoke()
}

fun lineRun(first: () -> Unit, next: () -> Unit = {}, last: () -> Unit) {
    first.invoke()
    next.invoke()
    last.invoke()
}

fun first(block: () -> Unit) = block.invoke()
fun next(block: () -> Unit) = block.invoke()
fun last(block: () -> Unit) = block.invoke()