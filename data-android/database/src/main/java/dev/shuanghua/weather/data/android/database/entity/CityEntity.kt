package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * 城市、县城 Entity
 */
@Entity(
    tableName = "city",
    primaryKeys = ["id"],
    indices = [(Index("id"))]
)
data class CityEntity(
    val provinceName: String,
    val name: String,
    val id: String,
)