package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_city_weather"
)
data class FavoriteCityWeatherEntity(
    @PrimaryKey val cityId: String,
    val cityName: String = "",
    val provinceName: String = "",
    val isAutoLocation: String = "",
    val currentT: String = "",
    val bgImageNew: String = "",
    val iconUrl: String = "",
)
