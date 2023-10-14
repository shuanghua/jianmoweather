package dev.shuanghua.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shuanghua.weather.data.android.domain.GetFavoriteCityWeatherUseCase
import dev.shuanghua.weather.data.android.domain.GetFavoriteStationWeatherUseCase
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 此页面请求地址中的 cityids不能为空，必须至少有一个城市id
 * 城市天气请求参数中 isauto = 0 ,只要首页定位页面 isauto = 1
 */
class FavoriteViewModel(
	private val favoriteRepository: FavoritesRepository,
	private val getStationListUseCase: GetFavoriteStationWeatherUseCase,
	private val getCityListUseCase: GetFavoriteCityWeatherUseCase
) : ViewModel() {

	private val viewModelState = MutableStateFlow(ViewModelState(isLoading = false))

	val uiState: StateFlow<FavoriteUiState> = viewModelState
		.map(ViewModelState::toUiState)
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000),
			viewModelState.value.toUiState()
		)

	init {
		refresh()
	}

	fun refresh() {
		viewModelScope.launch {
			viewModelState.update { it.copy(isLoading = true) }
			delay(400L)
			launch { getCityList() }
			launch { getStationList() }
		}
	}

	private suspend fun getStationList() {
		getStationListUseCase().asResult().collect { result ->
			viewModelState.update {
				when (result) {
					is Result.Success -> it.copy(stationWeather = result.data, isLoading = false)
					is Result.Error -> {
						val errorMessage = it.uiMessage + UiMessage(result.exception)
						it.copy(uiMessage = errorMessage, isLoading = false)
					}
				}
			}
		}
	}

	private suspend fun getCityList() {
		getCityListUseCase().asResult().collect { networkResult ->
			viewModelState.update {
				when (networkResult) {
					is Result.Success -> it.copy(cityWeather = networkResult.data)
					is Result.Error -> {
						val errorMessages = it.uiMessage + UiMessage(networkResult.exception)
						it.copy(uiMessage = errorMessages)
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
		viewModelState.update { currentUiState ->
			//从集合中剔除该id ，然后返回剔除后的集合
			val errorMessages = currentUiState.uiMessage.filterNot { it.id == id }
			currentUiState.copy(uiMessage = errorMessages)
		}
	}
}