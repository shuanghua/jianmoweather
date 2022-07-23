package dev.shuanghua.weather.shared.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val moshi: Moshi = Moshi.Builder().build()

fun Map<String, Any>.toJsonString(): String {
    val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    val mapAdapter = moshi.adapter<Map<String, Any>>(type)
    return mapAdapter.toJson(this)
}