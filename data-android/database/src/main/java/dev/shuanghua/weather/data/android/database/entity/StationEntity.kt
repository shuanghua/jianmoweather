package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "station",
    primaryKeys = ["stationId"],
    indices = [(Index("stationId"))]
)
data class StationEntity(
    val districtName: String,
    val stationId: String,
    val stationName: String
)