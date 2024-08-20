package com.app.network.adapter

import com.squareup.moshi.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * List 为 null
 */
class FilterNullValuesFromListAdapter<T : Any> private constructor(private val delegate: JsonAdapter<List<T?>>) :
    JsonAdapter<List<T>>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): List<T> =
        delegate.fromJsonValue(reader.readJsonValue())?.filterNotNull() ?: listOf()

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: List<T>?) {
        delegate.toJson(writer, value)
    }

    companion object {
        @JvmStatic
        fun <T : Any> newFactory(type: Class<T>): Factory {
            return object : Factory {
                override fun create(
                    requestedType: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
                    val listType = Types.newParameterizedType(List::class.java, type)
                    if (listType != requestedType) {
                        return null
                    }
                    val delegate = moshi.nextAdapter<List<T?>>(this, listType, annotations)
                    return FilterNullValuesFromListAdapter(delegate)
                }
            }
        }
    }
}

/**
 * List 为 "null"
 */
class FilterNullStringFromListAdapter<T : Any> private constructor(private val delegate: JsonAdapter<List<T?>>) :
    JsonAdapter<List<T>>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): List<T> {
        val readJsonValue = reader.readJsonValue()

        if (readJsonValue == "null") {
            return listOf<T>()
        } else {
            return delegate.fromJsonValue(readJsonValue)?.filterNotNull() ?: listOf()
        }
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: List<T>?) {
        delegate.toJson(writer, value)
    }

    companion object {
        @JvmStatic
        fun <T : Any> newFactory(type: Class<T>): Factory {
            return object : Factory {
                override fun create(
                    requestedType: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
//                    val listType = Types.getRawType(requestedType)// 注意这里的判断
//
//                    if ((listType != List::class.java) && (listType != Collection::class.java)) {
//                        return null
//                    }
                    val listType = Types.newParameterizedType(List::class.java, type)
                    val collectionType = Types.newParameterizedType(Collection::class.java, type)
                    if (listType != requestedType && collectionType != requestedType) {
                        return null
                    }

                    val delegate = moshi.nextAdapter<List<T?>>(this, requestedType, annotations)// 注意这里的类型
                    return FilterNullStringFromListAdapter(delegate)
                }
            }
        }
    }
}
