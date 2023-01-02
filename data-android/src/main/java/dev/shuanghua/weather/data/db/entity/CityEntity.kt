package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shuanghua.weather.data.model.CityResource

/**
 * 城市、县城 Entity
 */
@Entity(
    tableName = "city",
    primaryKeys = ["id"],
    indices = [(Index("id"))]
)
@JsonClass(generateAdapter = true)
data class CityEntity(
    @Json(name = "cityName") val name: String,
    @Json(name = "cityid") val id: String,
)

fun CityEntity.asExternalModel(): CityResource = CityResource(
    name = name,
    id = id
)