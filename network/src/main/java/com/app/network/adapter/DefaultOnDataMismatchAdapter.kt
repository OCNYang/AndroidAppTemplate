package com.app.network.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.io.IOException
import java.lang.reflect.Type

/**
 * 类型不匹配
 */
class DefaultOnDataMismatchAdapter<T> private constructor(
    private val delegate: JsonAdapter<T>,
    private val defaultValue: T?
) :
    JsonAdapter<T>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): T? =
        try {
            delegate.fromJsonValue(reader.readJsonValue())
        } catch (e: Exception) {
            println("Wrongful content - could not parse delegate " + delegate.toString())
            defaultValue
        }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: T?) {
        delegate.toJson(writer, value)
    }

    /**
     * type: 期望的类型
     * defaultValue: 默认值(如果返回的数据不匹配时，返回默认值)
     */
    companion object {
        @JvmStatic
        fun <T> newFactory(type: Class<T>, defaultValue: T?): JsonAdapter.Factory {
            return object : JsonAdapter.Factory {
                override fun create(
                    requestedType: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
                    if (type != requestedType) {
                        return null
                    }
                    val delegate = moshi.nextAdapter<T>(this, type, annotations)
                    return DefaultOnDataMismatchAdapter(delegate, defaultValue)
                }
            }
        }
    }
}
