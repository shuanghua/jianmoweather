package dev.shuanghua.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.SaveStationParamsToFavoriteList
import dev.shuanghua.weather.data.android.domain.usecase.UpdateWeatherUseCase
import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.ObservableLoadingCounter
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val stationRepository: StationRepository,
    private val updateWeatherUseCase: UpdateWeatherUseCase, // network -> db
    private val saveRequestParamsToFavoriteUseCase: SaveStationParamsToFavoriteList
) : ViewModel() {

    private var cityId: String = ""  // 保存服务返回的城市ID，懒的再单独查询一遍城市ID
    private var stationName: String = ""  // 用于添加到收藏
    private var lastStation: SelectedStation = SelectedStation("", "1")

    private val isLoading = ObservableLoadingCounter()
    private val messages = UiMessageManager()

    private val viewModelState = MutableStateFlow(WeatherViewModelState(isLoading = false))

    val uiState: StateFlow<WeatherUiState> = viewModelState
        .map(WeatherViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    init {
        // 观察数据库
        viewModelScope.launch {
            observerWeather() { newData ->
                viewModelState.update {
                    it.copy(
                        weather = newData.weather,
                        isLoading = newData.isLoading,
                        errorMessage = newData.errorMessage
                    )
                }
            }
        }

        // 观察站点
        viewModelScope.launch {
            stationRepository.getSelectedStation().collect {
                if (it != null) lastStation = it
                updateWeatherUseCase(
                    UpdateWeatherUseCase.Params(cityId, lastStation)
                ).collectStatus(isLoading, messages)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateWeatherUseCase(
                UpdateWeatherUseCase.Params(cityId, lastStation)
            ).collectStatus(isLoading, messages)
        }
    }

    private suspend fun observerWeather(
        weatherFlow: Flow<Weather> = weatherRepository.getOfflineWeather(),
        isLoadingFlows: Flow<Boolean> = isLoading.flow,
        errorMessageFlow: Flow<UiMessage?> = messages.flow,
        updateViewModelState: (WeatherViewModelState) -> Unit
    ) {
        combine(  // coroutines Zip.kt 最多允许 5 个 flow，超过需要自定义
            weatherFlow,
            isLoadingFlows,
            errorMessageFlow
        ) { weather, loadingStatus, errorMessage ->
            stationName = weather.stationName
            cityId = weather.cityId
            WeatherViewModelState(
                weather = weather,
                isLoading = loadingStatus,
                errorMessage = errorMessage
            )
        }.collect { updateViewModelState(it) }
    }


    fun clearMessage(id: Long) {
        viewModelScope.launch {
            messages.clearMessage(id)
        }
    }

    fun addStationToFavoriteList() {
        viewModelScope.launch {
            //使用 executeSync 执行耗时任务时，记得在 doWork 中切到非 Ui 换线程
            try {
                saveRequestParamsToFavoriteUseCase.executeSync(
                    SaveStationParamsToFavoriteList.Params(
                        cityId, stationName
                    )
                )
            } catch (e: Exception) {
                messages.emitMessage(UiMessage("不要重复收藏哦"))
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
        val weather: Weather,
        override val isLoading: Boolean,
        override val errorMessage: UiMessage?,
    ) : WeatherUiState
}

private data class WeatherViewModelState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val errorMessage: UiMessage? = null
) {
    fun toUiState(): WeatherUiState = if (weather == null) {
        WeatherUiState.NoData(
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    } else {
        WeatherUiState.HasData(
            weather = weather,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}
