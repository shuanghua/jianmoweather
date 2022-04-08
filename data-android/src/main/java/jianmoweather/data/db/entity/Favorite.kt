package cjianmoweather.data.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index

/**
 * 收藏
 */
@Entity(
        primaryKeys = ["cityId"],
        indices = [(Index("cityId"))]
)
data class Favorite(
        @NonNull
        val cityId: String,
        val cityName: String,
        val cityStationId: String
)