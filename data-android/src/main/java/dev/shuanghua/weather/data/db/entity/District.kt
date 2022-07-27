package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "district",
    primaryKeys = ["name"],
    indices = [(Index("name"))]
)
data class District(
    val name: String
)