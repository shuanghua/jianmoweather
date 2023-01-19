package dev.shuanghua.ui.screen.favorites.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.GetFavoriteWeatherUseCase
import dev.shuanghua.weather.data.android.model.Weather
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesWeatherViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWeatherUseCase: GetFavoriteWeatherUseCase
) : ViewModel() {

    private val cityId: String = checkNotNull(savedStateHandle[cityIdArg])
    private val stationName: String = checkNotNull(savedStateHandle[stationNameArg])

    private val viewModelState = MutableStateFlow(WeatherViewModelState(isLoading = false))

    internal val uiState: StateFlow<WeatherUiState> = viewModelState
        .map(WeatherViewModelState::toUiState)
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
            getWeatherUseCase(cityId, stationName).asResult().collect { result ->
                viewModelState.update {
                    when (result) {
                        is Result.Success -> it.copy(weather = result.data, isLoading = false)
                        is Result.Error -> {
                            Timber.e("-------->>${result.exception}")
                            val errorMessage = it.uiMessage + (UiMessage(result.exception))
                            it.copy(uiMessage = errorMessage, isLoading = false)
                        }
                    }
                }
            }
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

internal sealed interface WeatherUiState {
    val isLoading: Boolean
    val uiMessage: List<UiMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>
    ) : WeatherUiState

    data class HasData(
        val weather: Weather,
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>,
    ) : WeatherUiState
}

internal data class WeatherViewModelState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val uiMessage: List<UiMessage> = emptyList()
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
