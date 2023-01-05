package dev.shuanghua.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.model.FavoriteCityWeather
import dev.shuanghua.weather.data.model.FavoriteStationResource
import dev.shuanghua.weather.data.network.asFavoriteWeatherParam
import dev.shuanghua.weather.data.network.asOuterParam
import dev.shuanghua.weather.data.repo.FavoriteRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.shared.extensions.mapToArrayList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * 此页面请求地址中的 cityids不能为空，必须至少有一个城市id
 * 城市天气请求参数中 isauto = 0 ,只要首页定位页面 isauto = 1
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
) : ViewModel() {

    val stationUiState: StateFlow<FavoriteStationUiState> =
        favoriteRepository.observerStationParam()
            .map { dbData: List<FavoriteStationResource> ->
                FavoriteStationUiState.Success(dbData.map {
                    StationWeather(
                        stationName = it.stationName,
                        cityName = it.cityName
                    )
                })
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteStationUiState.Loading
            )

    val cityUiState: StateFlow<FavoriteCityUiState> = favoriteRepository.observerCityIds()
        .filterNot { it.isEmpty() }
        .map { it.mapToArrayList() }
        .map { createCityWeatherRequest(it) }//生成请求json
        .map { favoriteRepository.getCityWeather(it) }
        .map { networkData: List<FavoriteCityWeather> ->
            FavoriteCityUiState.Success(
                networkData.map {
                    CityWeather(
                        cityId = it.cityid,
                        cityName = it.cityName,
                        temperature = it.maxT,
                        iconUrl = it.wtype,
                    )
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoriteCityUiState.Loading
        )


    private fun createCityWeatherRequest(ids: ArrayList<String>): String {
        val cityIds = ids.joinToString(separator = ",")
        val innerParam = paramsRepository.getInnerParam().copy(cityids = cityIds)
        paramsRepository.updateInnerParam(innerParam)
        return paramsRepository.createFavoriteWeatherRequestJson(
            innerParam.asOuterParam(),
            innerParam.asFavoriteWeatherParam()
        )
    }

    fun deleteStation(stationName: String) {
        viewModelScope.launch {
            Timber.d("$stationName")
            favoriteRepository.deleteStationParam(stationName)
        }
    }

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            favoriteRepository.deleteCity(cityId)
        }
    }
}

data class CityWeather(
    val cityId: String,
    val cityName: String,
    val temperature: String,
    val iconUrl: String,
)

data class StationWeather(
    val cityName: String,
    val stationName: String,
//    val temperature: String, // 26°
//    val condition: String,  // 多云
)

sealed interface FavoriteCityUiState {
    object Loading : FavoriteCityUiState

    data class Success(
        val cityWeather: List<CityWeather>,
    ) : FavoriteCityUiState
}

sealed interface FavoriteStationUiState {
    object Loading : FavoriteStationUiState
    data class Success(
        val stationWeather: List<StationWeather>,
    ) : FavoriteStationUiState
}