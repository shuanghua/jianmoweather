package dev.shuanghua.weather.data.android.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@JsonClass(generateAdapter = true)
data class DistrictStationModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "list") val list: MutableList<StationModel>
)

@JsonClass(generateAdapter = true)
data class StationModel(
    @Json(name = "obtid") val stationId: String,
    @Json(name = "issele") val isSelect: String,
    @Json(name = "obtname") val stationName: String
)