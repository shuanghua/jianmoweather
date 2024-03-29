package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import dev.shuanghua.weather.data.android.model.SelectedStation


/**
 * 站点页面所选择的站点
 */
@Entity(
    tableName = "selected_station",
    primaryKeys = ["screen"],
    indices = [(Index("obtId"))]
)
data class SelectedStationEntity(
    val screen: String = "StationScreen",//only StationScreen
    @ColumnInfo(defaultValue = "") val obtId: String,
    @ColumnInfo(defaultValue = "1") val isLocation: String,
)