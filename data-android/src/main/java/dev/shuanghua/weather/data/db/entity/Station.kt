package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "station",
    primaryKeys = ["stationId"],
    indices = [(Index("stationId"))]
)
data class Station(
    val districtName: String,
    val stationId: String,
    val stationName: String
)