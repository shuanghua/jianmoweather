package dev.shuanghua.weather.data.android.datastore.serialization

import com.squareup.moshi.JsonAdapter
import dev.shuanghua.weather.data.android.datastore.model.DataStoreModel

interface DataStoreSerialization {
	suspend fun toJson(model: DataStoreModel): String
	suspend fun fromJson(string: String): DataStoreModel
}

class DataStoreSerializer(
	private val locationAdapter: JsonAdapter<DataStoreModel>,
) : DataStoreSerialization {
	override suspend fun toJson(
		model: DataStoreModel
	): String = locationAdapter.toJson(model)

	override suspend fun fromJson(
		string: String
	): DataStoreModel = locationAdapter.fromJson(string)!!
}

