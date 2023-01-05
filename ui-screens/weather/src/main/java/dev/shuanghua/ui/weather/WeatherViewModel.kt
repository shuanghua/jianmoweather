package dev.shuanghua.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.model.WeatherResource
import dev.shuanghua.weather.data.network.InnerParam
import dev.shuanghua.weather.data.network.toStationParamEntity
import dev.shuanghua.weather.data.repo.FavoriteRepository
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.WeatherRepository
import dev.shuanghua.weather.data.usecase.ObserverStationReturn
import dev.shuanghua.weather.data.usecase.UpdateWeatherUseCase
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val favoriteRepository: FavoriteRepository,
    private val paramsRepository: ParamsRepository,
    private val updateWeatherUseCase: UpdateWeatherUseCase, // network -> db
    private val observerStationReturn: ObserverStationReturn,
) : ViewModel() {

    private var cityId: String = ""  // 保存服务返回的城市ID，懒的再单独查询一遍城市ID
    private var stationName: String = ""  // 用于添加到收藏

    private val isLoadingFlow = ObservableLoadingCounter()
    private val errorMessageFlow = UiMessageManager()

    private val viewModelState = MutableStateFlow(WeatherViewModelState(isLoading = false))

    val uiState = viewModelState
        .map(WeatherViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    private suspend fun observerWeatherResource(
        viewModelStateFlow: MutableStateFlow<WeatherViewModelState>,
        weatherResourceFlow: Flow<WeatherResource>,
        isLoadingFlow: Flow<Boolean>,
        errorMessageFlow: Flow<UiMessage?>
    ) {
        combine(
            weatherResourceFlow,
            isLoadingFlow,
            errorMessageFlow
        ) { weatherResource, isLoading, errorMessage ->
            Timber.d("isLoading--->$isLoading  , $weatherResource")
            stationName = weatherResource.stationName
            cityId = weatherResource.cityId
            WeatherViewModelState(
                weatherResource = weatherResource,
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }.collect { newViewModelState ->
            viewModelStateFlow.update {
                it.copy(
                    weatherResource = newViewModelState.weatherResource,
                    isLoading = newViewModelState.isLoading,
                    errorMessage = newViewModelState.errorMessage
                )
            }
        }
    }

    init {
        observerStationReturn(Unit)
        viewModelScope.launch {
            observerWeatherResource(
                viewModelState,
                weatherRepository.getWeather(),
                isLoadingFlow.flow,
                errorMessageFlow.flow
            )
        }

        viewModelScope.launch {
            observerStationReturn.flow.collect {
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
            UpdateWeatherUseCase.Params(cityId = cityId)
        ).collectStatus(isLoadingFlow, errorMessageFlow)
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            errorMessageFlow.clearMessage(id)
        }
    }

    fun addToFavorite() {
        viewModelScope.launch {
            val innerParam: InnerParam = paramsRepository.getInnerParam().copy(cityid = cityId)
            val stationParamEntity = innerParam.toStationParamEntity(stationName)
            try {
                favoriteRepository.insertStationParam(stationParamEntity)
            } catch (e: Exception) {
                errorMessageFlow.emitMessage(UiMessage("该站点已经存在，不要重复收藏"))
            }
        }
    }
}


sealed interface WeatherUiState {
    val isLoading: Boolean
    val errorMessage: UiMessage?


    data class NoData(
        override val isLoading: Boolean,
        override val errorMessage: UiMessage?
    ) : WeatherUiState

    data class HasData(
        val weatherResource: WeatherResource,
        override val isLoading: Boolean,
        override val errorMessage: UiMessage?,
    ) : WeatherUiState
}

private data class WeatherViewModelState(
    val weatherResource: WeatherResource? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiMessage? = null
) {
    fun toUiState(): WeatherUiState = if (weatherResource == null) {
        WeatherUiState.NoData(
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    } else {
        WeatherUiState.HasData(
            weatherResource = weatherResource,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}
