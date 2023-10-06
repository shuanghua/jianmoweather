package dev.shuanghua.weather.data.android.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "province",
    primaryKeys = ["name"],
    indices = [(Index("name"))]
)
data class ProvinceEntity(
    val name: String
)


