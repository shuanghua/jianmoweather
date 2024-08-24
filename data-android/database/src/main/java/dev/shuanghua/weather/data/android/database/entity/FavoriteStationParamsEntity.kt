package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "favorite_station"
)
data class FavoriteStationParamsEntity(
	@PrimaryKey
	val stationName: String, //主键
	val isAutoLocation: String = "1",
	val cityId: String = "",
	val stationId: String = "",
	val lat: String = "",
	val lon: String = "",
	val pcity: String = "",
	val parea: String = "",
)


