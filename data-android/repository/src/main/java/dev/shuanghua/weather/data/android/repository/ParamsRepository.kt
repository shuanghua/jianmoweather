package dev.shuanghua.weather.data.android.repository

import dev.shuanghua.weather.data.android.model.InnerParams
import dev.shuanghua.weather.data.android.model.OuterParams
import dev.shuanghua.weather.data.android.model.request.CityScreenRequest
import dev.shuanghua.weather.data.android.model.request.DistrictScreenRequest
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenCityRequest
import dev.shuanghua.weather.data.android.model.request.FavoriteScreenStationRequest
import dev.shuanghua.weather.data.android.model.request.QueryCityIdRequest
import dev.shuanghua.weather.data.android.model.request.WeatherScreenRequest
import dev.shuanghua.weather.data.android.network.RequestParamsDataSource
import javax.inject.Inject

/**
 * 将 Params 转换成完整 json
 */
class ParamsRepository @Inject constructor(
    private val paramDataSource: RequestParamsDataSource
) {
    data class Params(val innerParams: InnerParams, val outerParams: OuterParams)

    private var params: Params? = null

    fun updateRequestParams(params: Params) {
        this.params = params
    }

    fun getRequestParams(): Params = this.params!!

    fun getMainWeatherRequestParams(params: WeatherScreenRequest): String =
        paramDataSource.getMainWeatherRequestParams(params)

    fun getFavoriteStationWeatherRequestParams(params: FavoriteScreenStationRequest): String =
        paramDataSource.getFavoriteStationWeatherRequestParams(params)

    fun getFavoriteCityWeatherRequestParams(params: FavoriteScreenCityRequest): String =
        paramDataSource.getFavoriteCityWeatherRequestParams(params)

    fun getDistrictListRequestParams(params: DistrictScreenRequest): String =
        paramDataSource.getDistrictListRequestParams(params)

    fun getCityListRequestParams(params: CityScreenRequest): String =
        paramDataSource.getCityListRequestParams(params)

    fun getQueryCityIdRequestParams(params: QueryCityIdRequest): String =
        paramDataSource.getQueryCityIdRequestParams(params)
}