package dev.shuanghua.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.usecase.ObserverStationReturn
import dev.shuanghua.weather.data.usecase.ObserverWeatherUseCase
import dev.shuanghua.weather.data.usecase.UpdateWeatherUseCase
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import dev.shuanghua.weather.shared.extensions.ifNullToValue
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
    observerWeather: ObserverWeatherUseCase,  // db(pojo) -> ViewModel
    private val updateWeather: UpdateWeatherUseCase, // network -> db
    private val observerStationReturn: ObserverStationReturn
) : ViewModel() {

    private var cityName: String = ""
    private var cityId: String = ""

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    // 协程库 combine 默认最多支持传入 5 个 Flow
    val uiStateFlow: StateFlow<WeatherUiState> = combine(
        observerWeather.flow, uiMessageManager.flow, observerLoading.flow
    ) { weather, message, loading ->
        if (weather != null) { // pojo
            cityName = weather.temperature?.cityName.ifNullToValue()
            cityId = weather.temperature?.cityId.ifNullToValue()
            WeatherUiState(
                temperature = weather.temperature,
                alarms = weather.alarms,
                oneHours = weather.oneHours,
                oneDays = weather.oneDays,
                others = weather.others,
                exponents = weather.exponents,
                message = message,
                loading = loading
            )
        } else {
            WeatherUiState.Empty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeatherUiState.Empty,
    )

    private var isLoc: String = "1"

    init {
        observerStationReturn(Unit)
        observerWeather(Unit)
        viewModelScope.launch {
            observerStationReturn.flow.collect {
                Timber.d("-Update1-----$it")
                refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateWeather(
                UpdateWeatherUseCase.Params(
                    cityName = cityName,
                    cityId = cityId,
                )
            ).collectStatus(observerLoading, uiMessageManager)
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun addToFavorite() {
        //TODO addToFavorite
    }
}