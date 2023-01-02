package dev.shuanghua.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.model.WeatherResource
import dev.shuanghua.weather.data.network.InnerParam
import dev.shuanghua.weather.data.network.toStationParamEntity
import dev.shuanghua.weather.data.repo.FavoriteRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.usecase.ObserverStationReturn
import dev.shuanghua.weather.data.usecase.ObserverWeatherUseCase
import dev.shuanghua.weather.data.usecase.UpdateWeatherUseCase
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
    private val updateWeatherUseCase: UpdateWeatherUseCase, // network -> db
    observerWeather: ObserverWeatherUseCase,  // db(pojo) -> ViewModel
    private val observerStationReturn: ObserverStationReturn,
) : ViewModel() {

    private var cityName: String = ""
    private var cityId: String = ""
    private var stationName: String = ""

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    // 协程库 combine 默认最多支持传入 5 个 Flow
    val uiStateFlow: StateFlow<WeatherUiState> = combine(
        observerWeather.flow,
        observerLoading.flow,
        uiMessageManager.flow
    ) { weather, loading, log ->
        Timber.d("----->>weather:$weather")
        stationName = weather.stationName
        cityName = weather.cityName
        cityId = weather.cityId //上次请求的城市id  服务器返回的id
        WeatherUiState.Success(weather = weather, loading = loading, log = log)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeatherUiState.Loading
    )


    init {
        observerWeather(Unit)
        observerStationReturn(Unit)

        viewModelScope.launch {
            observerStationReturn.flow.collect {
                Timber.d("--->>站点返回:$it")
                updateWeather()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateWeather()
        }
    }

    private suspend fun updateWeather() {
        updateWeatherUseCase(
            UpdateWeatherUseCase.Params(cityId = cityId, cityName = cityName)
        ).collectStatus(observerLoading, uiMessageManager)
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            val innerParam: InnerParam = paramsRepository.getInnerParam()
            val stationParamEntity = innerParam.toStationParamEntity(stationName)
            favoriteRepository.insertStationParam(stationParamEntity)
        }
    }
}

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val weather: WeatherResource,
        val loading: Boolean,
        val log: UiMessage?,
    ) : WeatherUiState
}