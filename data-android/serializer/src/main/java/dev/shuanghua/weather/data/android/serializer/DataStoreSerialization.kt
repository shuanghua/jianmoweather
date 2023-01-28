package dev.shuanghua.weather.data.android.serializer

import com.squareup.moshi.JsonAdapter
import dev.shuanghua.weather.data.android.serializer.model.AppDataStore

import javax.inject.Inject

interface DataStoreSerialization {
    suspend fun toJson(model: AppDataStore): String
    suspend fun fromJson(string: String): AppDataStore
}

class DataStoreMoshiSerializer @Inject constructor(
    private val locationAdapter: JsonAdapter<AppDataStore>,
) : DataStoreSerialization {
    override suspend fun toJson(
        model: AppDataStore
    ): String = locationAdapter.toJson(model)

    override suspend fun fromJson(
        string: String
    ): AppDataStore = locationAdapter.fromJson(string)!!
}

