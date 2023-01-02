package dev.shuanghua.weather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shuanghua.weather.data.model.FavoriteStationResource
import dev.shuanghua.weather.data.model.WeatherStationParamResource

@Entity(
    tableName = "station_param"
)
data class FavoriteStationParamEntity(
    @PrimaryKey
    val stationName: String, //主键
    val lon: String = "",
    val lat: String = "",
    val isauto: String = "1",
    val cityids: String = "",
    val cityid: String = "",
    val obtId: String = "",
    val pcity: String = "",
    val parea: String = "",
)

fun FavoriteStationParamEntity.asFavoriteStationModel(): FavoriteStationResource =
    FavoriteStationResource(
        stationName = stationName,
        parea = parea,
        cityName = pcity
    )

/**
 * 收藏，站点天气详情页面调用
 */
fun FavoriteStationParamEntity.asExternalModel(): WeatherStationParamResource =
    WeatherStationParamResource(
        lon = lon,
        lat = lat,
        isauto = isauto,
        cityids = cityids,
        cityid = cityid,
        obtId = obtId,
        pcity = pcity,
        parea = parea
    )