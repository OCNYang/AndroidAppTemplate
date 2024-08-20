package com.app.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

object BigDecimalAdapter {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}
//
//object Price2StringAdapter {
//    @FromJson
//    fun fromJson(string: Double): String {
//        return BigDecimal(string).toString()
//    }
//
//    @ToJson
//    fun toJson(value: String): Double {
//        return try {
//            value.toDouble()
//        } catch (e: NumberFormatException) {
//            0.0
//        }
//    }
//}