package dev.shuanghua.ui.favorite

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.usecase.ObserverFavoriteCityWeather
import dev.shuanghua.weather.data.usecase.UpdateFavoriteCityWeather
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val updateFavoriteCityWeather: UpdateFavoriteCityWeather,
    observerFavoriteCityWeather: ObserverFavoriteCityWeather
) : ViewModel() {
    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val cityIdList: ArrayList<String> = arrayListOf(
        "28060159493", "32010145005", "28010159287", "02010058362", "01010054511"
    )

    init {
        observerFavoriteCityWeather(ObserverFavoriteCityWeather.Params(""))
        refresh()
    }

    val favoriteUiState: StateFlow<FavoriteUiState> = combine(
        observerFavoriteCityWeather.flow,
        uiMessageManager.flow,
        observerLoading.observable
    ) { favorites, message, loading ->
        FavoriteUiState(favorites = favorites, message = message, refreshing = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavoriteUiState.Empty
    )

    fun refresh() {
        viewModelScope.launch {
            updateFavoriteCityWeather(
                UpdateFavoriteCityWeather.Params(cityIdList)
            ).collectStatus(observerLoading, uiMessageManager)
        }
    }
}

@Immutable
data class FavoriteUiState(
    val favorites: List<Favorite> = emptyList(),
    val message: UiMessage? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = FavoriteUiState()
        const val FAVORITE_SCREEN = "FavoriteScreen"
    }
}