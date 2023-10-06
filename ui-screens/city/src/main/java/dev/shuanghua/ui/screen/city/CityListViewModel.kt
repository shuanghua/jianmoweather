package dev.shuanghua.ui.screen.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.data.android.repository.ProvinceCityRepository
import dev.shuanghua.weather.shared.ObservableLoadingCounter
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,//存储传过来的省份ID
	private val provinceCityRepository: ProvinceCityRepository,
	private val favoriteRepository: FavoritesRepository,
) : ViewModel() {

	private val provinceName: String = checkNotNull(savedStateHandle[provinceNameArg])

	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()

	private val viewModelState = MutableStateFlow(ViewModelState(isLoading = true))


	val uiState: StateFlow<CityUiState> = viewModelState
		.map(ViewModelState::toUiState)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = viewModelState.value.toUiState()
		)

	init {
		viewModelScope.launch {
			provinceCityRepository.observerCityList(provinceName).collect { cityList ->
				updateViewModelState(provinceName, cityList)
			}
		}

		viewModelScope.launch {
			combine(
				observerLoading.flow,
				uiMessageManager.flow
			) { isLoading, errorMessage ->
				ViewModelState(isLoading = isLoading, message = errorMessage)
			}.collect { state ->
				viewModelState.update {
					it.copy(isLoading = state.isLoading, message = state.message)
				}
			}
		}
	}

	private fun updateViewModelState(
		provinceName: String,
		cityList: List<City>,
		isLoading: Boolean = false
	) = viewModelState.update {
		it.copy(
			isLoading = isLoading,
			provinceName = provinceName,
			cityList = cityList
		)
	}

	fun addCityIdToFavorite(cityId: String) {
		viewModelScope.launch {
			favoriteRepository.saveFavoriteCity(cityId)
		}
	}
}

sealed interface CityUiState {
	val isLoading: Boolean
	val message: UiMessage?

	data class HasData(
		override val isLoading: Boolean,
		override val message: UiMessage?,
		val provinceName: String,
		val cityList: List<City>
	) : CityUiState

	data class NoData(
		override val isLoading: Boolean,
		override val message: UiMessage?
	) : CityUiState
}

data class ViewModelState(
	val isLoading: Boolean,
	val message: UiMessage? = null,
	val provinceName: String = "",
	val cityList: List<City> = emptyList()
) {
	fun toUiState(): CityUiState {
		return if (cityList.isEmpty()) {
			CityUiState.NoData(
				isLoading = isLoading,
				message = message
			)
		} else {
			CityUiState.HasData(
				isLoading = isLoading,
				message = message,
				provinceName = provinceName,
				cityList = cityList
			)
		}
	}
}
