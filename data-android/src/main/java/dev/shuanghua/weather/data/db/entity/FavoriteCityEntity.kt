package dev.shuanghua.weather.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.util.getColumnIndexOrThrow
import dev.shuanghua.weather.data.model.FavoriteCityResource

@Entity(
    tableName = "favorite_city"
)
data class FavoriteCityEntity(
    @PrimaryKey
    @ColumnInfo(defaultValue = "") val cityId: String,
    @ColumnInfo(defaultValue = "") val cityName: String,
)

fun List<FavoriteCityEntity>.asExternalModel() = FavoriteCityResource(
    cityIds = map(FavoriteCityEntity::cityId),
    cityNames = map(FavoriteCityEntity::cityName)
)


