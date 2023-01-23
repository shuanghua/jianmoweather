package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.model.params.CityListParams
import dev.shuanghua.weather.data.android.model.params.DistrictParams
import dev.shuanghua.weather.data.android.model.params.FavoriteCityParams
import dev.shuanghua.weather.data.android.model.params.SearchCityByKeywordsParams
import dev.shuanghua.weather.data.android.model.params.WeatherParams
import dev.shuanghua.weather.data.android.network.ParamsDataSource
import javax.inject.Inject

/**
 * 将 Params 转换成完整 json
 */
class ParamsRepository @Inject constructor(
    private val paramDataSource: ParamsDataSource
) {

    private var weatherParams: WeatherParams? = null

    fun setWeatherParams(params: WeatherParams) {
        this.weatherParams = params
    }

    fun getWeatherParams(): WeatherParams {
        if (weatherParams != null) {
            return weatherParams as WeatherParams
        } else {
            throw NullPointerException("ParamsRepository -> weatherParams 没有初始化！")
        }
    }

    fun weatherParamsToJson(params: WeatherParams): String =
        paramDataSource.weatherParamsToJson(params)

    fun favoriteCityParamsToJson(params: FavoriteCityParams): String =
        paramDataSource.favoriteCityParamsToJson(params)

    fun districtListParamsToJson(params: DistrictParams): String =
        paramDataSource.districtListParamsToJson(params)

    fun cityListParamsToJson(params: CityListParams): String =
        paramDataSource.cityListParamsToJson(params)

    fun searchCityByKeywordParamsToJson(params: SearchCityByKeywordsParams): String =
        paramDataSource.searchCityByKeywordParamsToJson(params)
}