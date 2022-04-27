package dev.shuanghua.weather.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.db.entity.Province

@JsonClass(generateAdapter = true)
data class ShenZhenCommon<out T>(@Json(name = "returnData") val data: T?)


//------------------------------------------------------------------------------
//@JsonClass(generateAdapter = true)
//data class FavoriteReturn<out T>(@Json(name = "list") val data: List<T>)
@JsonClass(generateAdapter = true)
data class FavoriteReturn(@Json(name = "list") val list: List<Favorite>)


@JsonClass(generateAdapter = true)
data class CityReturn(@Json(name = "list") val list: List<City>?)


@JsonClass(generateAdapter = true)
data class ProvinceReturn(@Json(name = "list") val list: List<Province>)


@JsonClass(generateAdapter = true)
data class StationReturn(@Json(name = "list") val list: List<District>)