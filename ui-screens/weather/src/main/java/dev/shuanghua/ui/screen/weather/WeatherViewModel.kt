package dev.shuanghua.ui.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shuanghua.weather.data.android.domain.SaveStationToFavoriteUseCase
import dev.shuanghua.weather.data.android.domain.UpdateLocationCityWeatherUseCase
import dev.shuanghua.weather.data.android.domain.UpdateStationWeatherUseCase
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.data.android.repository.StationRepository
import dev.shuanghua.weather.data.android.repository.WeatherRepository
import dev.shuanghua.weather.shared.ObservableLoadingCounter
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.collectStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class WeatherViewModel(
	weatherRepository: WeatherRepository,
	private val stationRepository: StationRepository,
	private val updateLocationCityWeather: UpdateLocationCityWeatherUseCase,
	private val updateStationWeather: UpdateStationWeatherUseCase,
	private val addStationToFavoriteUseCase: SaveStationToFavoriteUseCase,
) : ViewModel() {

	private var cityId: String = ""
	private var stationId: String = ""
	private var stationName: String = ""
	private var isLocation: Boolean = false
	private val isLoading = ObservableLoadingCounter()
	private val messages = UiMessageManager()

	private val viewModelState = MutableStateFlow(
		WeatherViewModelState(isLoading = false)
	)

	val uiState: StateFlow<WeatherUiState> = viewModelState
		.map(WeatherViewModelState::toUiState)
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			viewModelState.value.toUiState()
		)

	init {
		observerWeather(
			weatherRepository.observerWeather(),
			isLoading.flow,
			messages.flow
		)
		observerSelectedStation()
	}


	fun refresh() {
		viewModelScope.launch {
			if (isLocation) {
				updateLocationCityWeather(Unit).collectStatus(isLoading, messages)
			} else {
				updateStationWeather(
					UpdateStationWeatherUseCase.Params(cityId, stationId)
				).collectStatus(isLoading, messages)
			}
		}
	}

	private fun observerWeather(
		weatherFlow: Flow<Weather>,
		isLoadingFlows: Flow<Boolean>,
		errorMessageFlow: Flow<UiMessage?>,
	) = viewModelScope.launch {
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
				uiMessage = errorMessage
			)
		}.collect { newData ->
			viewModelState.update {
				it.copy(
					weather = newData.weather,
					isLoading = newData.isLoading,
					uiMessage = newData.uiMessage
				)
			}
		}
	}

	/**
	 * 1. 自动定位 包含 经纬度，不用含有城市id, 站点id
	 * 2. 手动选择站点，需要含有 城市id, 站点id
	 * 3. 数据库没有站点，且自动定位失败，使用 手动选择城市，只需要 传城市id
	 */
	private fun observerSelectedStation() = viewModelScope.launch {
		stationRepository.observerSelectedStation()
			.distinctUntilChanged()
			.collect {
				Timber.d("station: $it")
				if (it == null || it.isLocation == "1") {//数据库没有站点，按自动定位
					isLocation = true
					updateLocationCityWeather(Unit).collectStatus(isLoading, messages)
				} else {
					isLocation = false
					stationId = it.stationId
					updateStationWeather(
						UpdateStationWeatherUseCase.Params(cityId, it.stationId)
					).collectStatus(isLoading, messages)
				}
			}
	}

	fun clearMessage(id: Long) = viewModelScope.launch {
		messages.clearMessage(id)
	}

	fun saveToFavorite() = viewModelScope.launch {
		try {
			addStationToFavoriteUseCase(cityId, stationName)
		} catch (e: Exception) {
			Timber.e(e)
			messages.emitMessage(UiMessage("不要重复收藏哦"))
		}
	}
}

sealed interface WeatherUiState {
	val isLoading: Boolean
	val uiMessage: UiMessage?

	data class NoData(
		override val isLoading: Boolean,
		override val uiMessage: UiMessage?,
	) : WeatherUiState

	data class HasData(
		val weather: Weather,
		override val isLoading: Boolean,
		override val uiMessage: UiMessage?,
	) : WeatherUiState
}

private data class WeatherViewModelState(
	val weather: Weather? = null,
	val isLoading: Boolean = false,
	val uiMessage: UiMessage? = null,
) {
	fun toUiState(): WeatherUiState = if (weather == null) {
		WeatherUiState.NoData(
			isLoading = isLoading,
			uiMessage = uiMessage
		)
	} else {
		WeatherUiState.HasData(
			weather = weather,
			isLoading = isLoading,
			uiMessage = uiMessage
		)
	}
}
