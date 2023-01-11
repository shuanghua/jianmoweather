package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.android.model.FavoriteCity

@Entity(
    tableName = "favorite_city"
)
data class FavoriteCityEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "") val cityId: String,
    @ColumnInfo(defaultValue = "") val cityName: String,
)


