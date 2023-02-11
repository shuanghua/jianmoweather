package dev.shuanghua.weather.data.android.serializer

import com.squareup.moshi.JsonAdapter
import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.SearchCityByKeywordsParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.serializer.model.toMapParams
import javax.inject.Inject

/**
 * 解耦，同时向外提供注入
 * 外部访问抽象,而不应关心具体实现
 */
interface NetworkParamsSerialization {
    fun weatherParamsToJson(params: WeatherParams): String

    fun favoriteCityParamsToJson(params: FavoriteCityParams): String

    fun districtListParamsToJson(params: DistrictParams): String

    fun cityListParamsToJson(params: CityListParams): String

    fun searchCityByKeywordParamsToJson(params: SearchCityByKeywordsParams): String
}


/**
 * 项目目前使用的序列化工具是 moshi
 * 作为学习项目，以后会尝试 kotlinx.serialization
 * 到时可能出现两个或多个序列化工具
 * 为保证解耦，实现 NetworkParamsSerialization 接口，
 * 并传入序列化工具对象
 */
class NetworkParamsMoshiSerializer @Inject constructor(
    private val mapAdapter: JsonAdapter<Map<String, Any>>,
) : NetworkParamsSerialization {

    private fun Map<String, Any>.toJson(): String = mapAdapter.toJson(this)

    override fun weatherParamsToJson(
        params: WeatherParams,
    ): String = params.toMapParams().toJson()


    override fun favoriteCityParamsToJson(
        params: FavoriteCityParams,
    ): String = params.toMapParams().toJson()


    override fun districtListParamsToJson(
        params: DistrictParams,
    ): String = params.toMapParams().toJson()


    override fun cityListParamsToJson(
        params: CityListParams,
    ): String = params.toMapParams().toJson()


    override fun searchCityByKeywordParamsToJson(
        params: SearchCityByKeywordsParams,
    ): String = params.toMapParams().toJson()

}


