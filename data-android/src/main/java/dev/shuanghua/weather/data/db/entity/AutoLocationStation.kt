package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "auto_station",
    primaryKeys = ["screen"],
    indices = [(Index("id"))]
)
data class AutoLocationStation(
    val screen: String,
    val id: String,
    val name: String
)