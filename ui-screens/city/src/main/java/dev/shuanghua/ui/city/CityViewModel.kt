package dev.shuanghua.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.SaveCityToFavoriteUseCase
import dev.shuanghua.weather.data.android.model.City
import dev.shuanghua.weather.data.android.model.CityListParams
import dev.shuanghua.weather.data.android.model.request.CityScreenRequest
import dev.shuanghua.weather.data.android.repository.CityRepository
import dev.shuanghua.weather.data.android.repository.ParamsRepository
import dev.shuanghua.weather.shared.UiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
    private val cityRepository: CityRepository,
    private val paramsRepository: ParamsRepository,
    private val saveCityToFavoriteUseCase: SaveCityToFavoriteUseCase,
) : ViewModel() {

    private val provinceId: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceIdArg])
    private val provinceName: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceNameArg])

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
                if (cityList.isEmpty()) {
                    updateCityList()
                } else {
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            provinceName = provinceName,
                            cityList = cityList
                        )
                    }
                }
            }
        }

    }

    fun refresh() {
        updateCityList()
    }

    private fun updateCityList() {
        viewModelScope.launch {
            try {
                cityRepository.updateCityList(
                    provinceName = provinceName,
                    paramsJson = getRequestParamsJson(provinceId)
                )
            } catch (e: Exception) {
                Timber.e("错误:${e.message}")
                viewModelState.update {
                    val errorMessage = it.message + UiMessage(e)
                    it.copy(isLoading = false, message = errorMessage)
                }
            }
        }
    }

    private fun getRequestParamsJson(pId: String): String {
        val mainParams = paramsRepository.getRequestParams()
        val outerParams = mainParams.outerParams
        val cityListParams =
            CityListParams(
                provId = pId,
                cityids = mainParams.innerParams.cityids
            )
        return paramsRepository.getCityListRequestParams(
            CityScreenRequest(
                innerParams = cityListParams,
                outerParams = outerParams
            )
        )
    }

    fun addCityIdToFavorite(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            saveCityToFavoriteUseCase.executeSync(city)
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
