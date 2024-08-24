package dev.shuanghua.weather.data.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index


/**
 * 站点页面所选择的站点
 */
@Entity(
	tableName = "selected_station",
	primaryKeys = ["screen"],
	indices = [(Index("stationId"))]
)
data class SelectedStationEntity(
	val screen: String = "StationScreen",//only StationScreen
	@ColumnInfo(defaultValue = "") val stationId: String,
	@ColumnInfo(defaultValue = "1") val isLocation: String,
	@ColumnInfo(defaultValue = "") val districtName: String,
	@ColumnInfo(defaultValue = "") val stationName: String,
)