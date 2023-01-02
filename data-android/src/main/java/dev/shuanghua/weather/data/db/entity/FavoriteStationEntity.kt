package dev.shuanghua.weather.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_station"
)
data class FavoriteStationEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "") val stationName: String,
    @ColumnInfo(defaultValue = "") val cityName: String,
)
