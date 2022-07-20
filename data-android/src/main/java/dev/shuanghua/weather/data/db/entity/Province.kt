package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "province",
    primaryKeys = ["id"],
    indices = [(Index("id"))]
)
@JsonClass(generateAdapter = true)
data class Province(
    @Json(name = "provName") val name: String,
    @Json(name = "provId") val id: String
)
