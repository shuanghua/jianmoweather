package dev.shuanghua.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.GetRequestCityListParamsUseCase
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.repository.CityRepository
import dev.shuanghua.weather.data.android.repository.FavoritesRepository
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.ifEmptyHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
    private val cityRepository: CityRepository,
    private val favoriteRepository: FavoritesRepository,
    private val getRequestCityListParamsUseCase: GetRequestCityListParamsUseCase
) : ViewModel() {

    private val provinceId: String = checkNotNull(savedStateHandle[provinceIdArg])
    private val provinceName: String = checkNotNull(savedStateHandle[provinceNameArg])

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
            cityRepository.observerCityList(provinceName).collect { cityList ->
                cityList.ifEmptyHandle(
                    emptyHandle = { updateCityList() },
                    notEmptyHandle = { updateViewModelState(provinceName, cityList) },
                )
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


    private fun updateCityList() {
        viewModelScope.launch {
            try {
                cityRepository.updateCityList(
                    provinceName = provinceName,
                    paramsJson = getRequestCityListParamsUseCase(provinceId)
                )
            } catch (e: Exception) {
                viewModelState.update {
                    val errorMessage = it.message + UiMessage(e)
                    it.copy(isLoading = false, message = errorMessage)
                }
            }
        }
    }

    fun addCityIdToFavorite(cityId: String) {
        viewModelScope.launch {
            favoriteRepository.saveFavoriteCity(cityId)
        }
    }
}

sealed interface CityUiState {
    val isLoading: Boolean
    val message: List<UiMessage>

    data class HasData(
        override val isLoading: Boolean,
        override val message: List<UiMessage>,
        val provinceName: String,
        val cityList: List<City>
    ) : CityUiState

    data class NoData(
        override val isLoading: Boolean,
        override val message: List<UiMessage>
    ) : CityUiState
}

data class ViewModelState(
    val isLoading: Boolean,
    val message: List<UiMessage> = emptyList(),
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
