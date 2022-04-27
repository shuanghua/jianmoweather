package dev.shuanghua.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.usecase.GetFavoriteCityWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoriteCityWeather: GetFavoriteCityWeather
) : ViewModel() {
    val loadingStateFlow: StateFlow<LoadingUiState> = MutableStateFlow(LoadingUiState.Hide)


    init {
        viewModelScope.launch {
            getFavoriteCityWeather()
        }
    }
}

data class LoadingUiState(val isLoading: Boolean = false) {
    companion object {
        val Hide = LoadingUiState()
    }
}