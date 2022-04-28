package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.JsonClass


@Entity(
    tableName = "favorite",
    primaryKeys = ["cityName"],
    indices = [(Index("cityid"))]
)
@JsonClass(generateAdapter = true)
data class Favorite(
    val cityName: String,
    val cityType: String,
    val cityid: String,
    val isauto: String,
    val maxT: String,
    val minT: String,
    val wnow: String?,
    val wnownew: String,
    val wtype: String
)