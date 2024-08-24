package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 收藏城市 Entity
 */
@Entity(
	tableName = "favorite_city",
	indices = [(Index("cityId"))]
)
data class FavoriteCityEntity(
	@PrimaryKey val cityId: String = "",
	val cityName: String = "",
	val provinceName: String = "",
)