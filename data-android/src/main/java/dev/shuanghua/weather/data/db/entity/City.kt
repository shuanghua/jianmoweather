package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 城市、县城 Entity
 */
@Entity(
    primaryKeys = ["cityId"],
    indices = [(Index("cityId"))]
)
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "cityid") val cityId: String,
    @Json(name = "cityName") val name: String
)