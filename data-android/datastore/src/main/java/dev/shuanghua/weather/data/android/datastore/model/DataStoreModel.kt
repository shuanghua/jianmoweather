package dev.shuanghua.weather.data.android.datastore.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataStoreModel(
	val theme: Int = 1,
	val cityName: String = "",
	val latitude: String = "",
	val longitude: String = "",
	val district: String = "",
	val address: String = ""
)