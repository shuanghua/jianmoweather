package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "province",
    primaryKeys = ["id"],
    indices = [(Index("id"))]
)
data class ProvinceEntity(
    val name: String,
    val id: String
)


