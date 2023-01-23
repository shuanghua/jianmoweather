package dev.shuanghua.weather.data.android.network

import com.squareup.moshi.JsonAdapter
import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.SearchCityByKeywordsParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import javax.inject.Inject

interface ParamsDataSource {
    fun weatherParamsToJson(params: WeatherParams): String

    fun favoriteCityParamsToJson(params: FavoriteCityParams): String

    fun districtListParamsToJson(params: DistrictParams): String

    fun cityListParamsToJson(params: CityListParams): String

    fun searchCityByKeywordParamsToJson(params: SearchCityByKeywordsParams): String
}


class ParamsSerializationDataSource @Inject constructor(
    private val mapAdapter: JsonAdapter<Map<String, Any>>
) : ParamsDataSource {

    private fun Map<String, Any>.toJson(): String = mapAdapter.toJson(this)

    override fun weatherParamsToJson(
        params: WeatherParams
    ): String = params.toMapParams().toJson()


    override fun favoriteCityParamsToJson(
        params: FavoriteCityParams
    ): String = params.toMapParams().toJson()


    override fun districtListParamsToJson(
        params: DistrictParams
    ): String = params.toMapParams().toJson()


    override fun cityListParamsToJson(
        params: CityListParams
    ): String = params.toMapParams().toJson()


    override fun searchCityByKeywordParamsToJson(
        params: SearchCityByKeywordsParams
    ): String = params.toMapParams().toJson()

}