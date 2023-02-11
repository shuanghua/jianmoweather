package dev.shuanghua.ui.screen.favorite

import dev.shuanghua.weather.data.android.model.FavoriteCity
import dev.shuanghua.weather.data.android.model.FavoriteStation
import dev.shuanghua.weather.shared.UiMessage

sealed interface FavoriteUiState {

    val isLoading: Boolean
    val uiMessage: List<UiMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>
    ) : FavoriteUiState

    data class HasData(
        val cityWeather: List<FavoriteCity>,
        val stationWeather: List<FavoriteStation>,
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>
    ) : FavoriteUiState
}

data class ViewModelState(
    val cityWeather: List<FavoriteCity> = emptyList(),
    val stationWeather: List<FavoriteStation> = emptyList(),
    val isLoading: Boolean = false,
    val uiMessage: List<UiMessage> = emptyList()
) {
    fun toUiState(): FavoriteUiState {
        return if (cityWeather.isEmpty() && stationWeather.isEmpty()) {
            FavoriteUiState.NoData(
                isLoading = isLoading,
                uiMessage = uiMessage
            )
        } else {
            FavoriteUiState.HasData(
                cityWeather = cityWeather,
                stationWeather = stationWeather,
                isLoading = isLoading,
                uiMessage = uiMessage
            )
        }
    }
}