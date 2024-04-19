package com.app.base

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * first: key
 * second: value type
 * third: default value
 */
typealias MMKVKEY = Triple<String, Class<*>, Any>

fun MMKVKEY.put(value: Any) {
    when (this.second) {
        String::class.java -> MMKV.defaultMMKV().encode(this.first, value as String)
        Int::class.java -> MMKV.defaultMMKV().encode(this.first, value as Int)
        Long::class.java -> MMKV.defaultMMKV().encode(this.first, value as Long)
        Float::class.java -> MMKV.defaultMMKV().encode(this.first, value as Float)
        Boolean::class.java -> MMKV.defaultMMKV().encode(this.first, value as Boolean)
        Double::class.java -> MMKV.defaultMMKV().encode(this.first, value as Double)
        ByteArray::class.java -> MMKV.defaultMMKV().encode(this.first, value as ByteArray)
        Parcelable::class.java -> MMKV.defaultMMKV().encode(this.first, value as Parcelable)
        else -> throw IllegalArgumentException("Unsupported type: ${this.second.name}")
    }
}

fun MMKVKEY.get(): Any {
    when (this.second) {
        String::class.java -> return MMKV.defaultMMKV().decodeString(this.first, this.third as String) ?: ""
        Int::class.java -> return MMKV.defaultMMKV().decodeInt(this.first, this.third as Int)
        Long::class.java -> return MMKV.defaultMMKV().decodeLong(this.first, this.third as Long)
        Float::class.java -> return MMKV.defaultMMKV().decodeFloat(this.first, this.third as Float)
        Boolean::class.java -> return MMKV.defaultMMKV().decodeBool(this.first, this.third as Boolean)
        Double::class.java -> return MMKV.defaultMMKV().decodeDouble(this.first, this.third as Double)
        ByteArray::class.java -> return MMKV.defaultMMKV().decodeBytes(this.first, this.third as ByteArray) ?: ByteArray(0)
        Parcelable::class.java -> return MMKV.defaultMMKV().decodeParcelable(this.first, this.second as Class<Parcelable>, this.third as Parcelable) ?: this.third
        else -> throw IllegalArgumentException("Unsupported type: ${this.second.name}")
    }
}

fun withMMKV(block: MMKV.() -> Unit) {
    block(MMKV.defaultMMKV())
}


