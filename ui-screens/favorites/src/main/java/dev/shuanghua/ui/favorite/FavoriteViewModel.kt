package dev.shuanghua.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.GetFavoriteCityListWeatherUseCase
import dev.shuanghua.weather.data.android.model.FavoriteCityWeather
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.data.android.repository.FavoriteRepository
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 此页面请求地址中的 cityids不能为空，必须至少有一个城市id
 * 城市天气请求参数中 isauto = 0 ,只要首页定位页面 isauto = 1
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val getFavoriteCityListWeather: GetFavoriteCityListWeatherUseCase
) : ViewModel() {

    private var cityIds = ArrayList<String>()
    private val cityViewModelState = MutableStateFlow(CityViewModelState(isLoading = true))

    val cityUiState: StateFlow<FavoriteCityUiState> = cityViewModelState
        .map(CityViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            cityViewModelState.value.toUiState()
        )


    val stationUiState: StateFlow<FavoriteStationUiState> =
        favoriteRepository.observerStationParam()
            .map { dbData: List<FavoriteStation> ->
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

    init {
        observerCityIds()
    }

    fun refresh() {
        cityViewModelState.update { it.copy(isLoading = true) }
        if (cityIds.isNotEmpty()) {
            updateCityListWeather(cityIds)
        } else {
            cityViewModelState.update { it.copy(isLoading = false) }
        }
    }

    private fun observerCityIds() = viewModelScope.launch(dispatchers.io) {
        favoriteRepository.observerCityIds().collect { ids ->
            cityIds = ids // 用于界面的手动刷新
            if (ids.isNotEmpty()) {
                updateCityListWeather(ids)
            } else {
                cityViewModelState.update {
                    it.copy(cityWeather = emptyList())
                }
            }
        }
    }

    private fun updateCityListWeather(ids: ArrayList<String>) {
        viewModelScope.launch {
            getFavoriteCityListWeather(ids = ids).asResult().collect { result ->
                when (result) {
                    is Result.Success ->
                        cityViewModelState.update {
                            it.copy(
                                cityWeather = result.data,
                                isLoading = false
                            )
                        }

                    is Result.Error -> cityViewModelState.update {
                        val errorMessages = it.errorMessages + UiMessage(result.exception)
                        it.copy(
                            isLoading = false,
                            errorMessages = errorMessages
                        )
                    }
                }
            }
        }
    }

    fun deleteStation(stationName: String) {
        viewModelScope.launch {
            favoriteRepository.deleteStationParam(stationName)
        }
    }

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            favoriteRepository.deleteCity(cityId)
        }
    }

    fun clearMessage(id: Long) {
        cityViewModelState.update { currentUiState ->
            //从集合中剔除该id ，然后返回剔除后的集合
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == id }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}

sealed interface FavoriteCityUiState {

    val isLoading: Boolean
    val errorMessages: List<UiMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val errorMessages: List<UiMessage>
    ) : FavoriteCityUiState

    data class HasData(
        val cityWeather: List<FavoriteCityWeather>,
        override val isLoading: Boolean,
        override val errorMessages: List<UiMessage>
    ) : FavoriteCityUiState
}

private data class CityViewModelState(
    val cityWeather: List<FavoriteCityWeather> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<UiMessage> = emptyList()
) {
    fun toUiState(): FavoriteCityUiState {
        return if (cityWeather.isEmpty()) {
            FavoriteCityUiState.NoData(
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        } else {
            FavoriteCityUiState.HasData(
                cityWeather = cityWeather,
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        }
    }

}


data class StationWeather(
    val cityName: String,
    val stationName: String,
//    val temperature: String, // 26°
//    val condition: String,  // 多云
)

sealed interface FavoriteStationUiState {
    object Loading : FavoriteStationUiState
    data class Success(
        val stationWeather: List<StationWeather>,
    ) : FavoriteStationUiState
}