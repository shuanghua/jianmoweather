package jianmoweather.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jianmoweather.data.db.entity.City
import jianmoweather.data.db.entity.Province
import jianmoweather.data.db.entity.StationArea

@JsonClass(generateAdapter = true)
data class ShenZhen<out T>(@Json(name = "returnData") val data: T?)

@JsonClass(generateAdapter = true)
data class CityList(var list: MutableList<City>?)

@JsonClass(generateAdapter = true)
data class ProvinceList(var list: MutableList<Province>)

@JsonClass(generateAdapter = true)
data class StationList(var list: MutableList<StationArea>)