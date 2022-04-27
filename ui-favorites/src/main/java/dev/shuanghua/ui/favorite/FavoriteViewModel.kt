package dev.shuanghua.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.usecase.UpdateFavoriteCityWeather
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val updateFavoriteCityWeather: UpdateFavoriteCityWeather
) : ViewModel() {
    val loadingStateFlow: StateFlow<LoadingUiState> = MutableStateFlow(LoadingUiState.Hide)
    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()//flow
    private val cityIdList: ArrayList<String> =
        arrayListOf("28060159493", "32010145005", "28010159287", "02010058362", "01010054511")

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            updateFavoriteCityWeather(
                UpdateFavoriteCityWeather.Params(cityIdList)
            ).collectStatus(observerLoading, uiMessageManager)
        }
    }


}

data class FavoriteUiState(
    val favorites: List<Favorite> = emptyList(),
    val message: UiMessage? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = FavoriteUiState()
        const val HOME_SCREEN = "FavoriteScreen"
    }
}

data class LoadingUiState(val isLoading: Boolean = false) {
    companion object {
        val Hide = LoadingUiState()
    }
}