package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.Index


/**
 * 站点页面所选择的站点
 */
@Entity(
    tableName = "station_return",
    primaryKeys = ["screen"],
    indices = [(Index("obtId"))]
)
data class StationReturn(
    val screen:String,//only StationScreen
    val obtId: String,
    val isLocation: String
)