package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "favorite_id",
    primaryKeys = ["cityId"],
    indices = [(Index("cityId"))]
)
data class FavoriteId(val cityId: String)
