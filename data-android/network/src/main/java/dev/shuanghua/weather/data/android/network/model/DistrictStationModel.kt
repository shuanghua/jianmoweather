package dev.shuanghua.weather.data.android.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DistrictStationModel(
    @SerialName("name") val name: String,
    @SerialName("list") val list: MutableList<StationModel>
)

@Serializable
data class StationModel(
    @SerialName("obtid") val stationId: String,
    @SerialName("issele") val isSelect: String,
    @SerialName("obtname") val stationName: String
)