package dev.shuanghua.weather.data.android.repository.convert

import dev.shuanghua.weather.data.android.database.entity.FavoriteStationWeatherParamsEntity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.model.FavoriteStationWeatherParams

/**
 * 收藏，站点天气详情页面调用
 */
fun FavoriteStationWeatherParamsEntity.asRequestModel(): FavoriteStationWeatherParams =
    FavoriteStationWeatherParams(
        lon = lon,
        lat = lat,
        isauto = isauto,
        cityids = cityids,
        cityid = cityid,
        obtId = obtId,
        pcity = pcity,
        parea = parea
    )
