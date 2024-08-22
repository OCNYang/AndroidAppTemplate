package com.app.base

import java.util.ServiceLoader

object ServiceLoaderX {
    fun <S : Any> loadSafe(service: Class<S>): S? {
        return try {
            ServiceLoader.load(service).first()
        } catch (e: RuntimeException) {
            null
        } catch (e: NoSuchElementException) {
            null
        }
    }
}