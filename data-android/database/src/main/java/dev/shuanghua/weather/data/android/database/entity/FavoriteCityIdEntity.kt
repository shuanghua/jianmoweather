package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_city_id"
)
data class FavoriteCityIdEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "") val id: String,
)
