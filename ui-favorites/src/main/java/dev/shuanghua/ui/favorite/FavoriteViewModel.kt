package dev.shuanghua.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.FavoriteCityWeather
import dev.shuanghua.weather.data.repo.ParamsRepository
import dev.shuanghua.weather.data.repo.favorite.FavoriteRepository
import dev.shuanghua.weather.data.usecase.ObserverFavoriteCityIdsUseCase
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

val WhileViewSubscribed = SharingStarted.WhileSubscribed(5000)

/**
 * 当使用 ObservableUseCase 的时候，记得先调用 invoke()
 * 然后一定要 collect 该 Flow, 才能观察到数据
 * 此页面请求地址中的 cityids不能为空，必须至少有一个城市id
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    paramsRepository: ParamsRepository,
    favoriteRepository: FavoriteRepository,
    private val favoriteDao: FavoriteDao,
    observerFavoriteIds: ObserverFavoriteCityIdsUseCase,
) : ViewModel() {
    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val cityIdsFlow: Flow<ArrayList<String>> = observerFavoriteIds.flow.map { cIds ->
        val array = ArrayList<String>()
        cIds.forEach {
            array.add(it)
        }
        array
    }

    //天气数据请求
    private val favoriteWeatherFlow: Flow<List<FavoriteCityWeather>> =
        cityIdsFlow.filterNot { it.isEmpty() }.map {
            val requestParam = paramsRepository.getFavoriteWeatherRequestJson(it)
            favoriteRepository.getFavoriteCityWeather(requestParam)
        }.flowOn(dispatchers.io)

    val uiState: StateFlow<FavoriteUiState> = combine(
        cityIdsFlow,
        favoriteWeatherFlow,
        observerLoading.flow,
        uiMessageManager.flow
    ) { _, weathers, loading, message ->
        FavoriteUiState(favorites = weathers, message = message, loading = loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoriteUiState.Empty
    )


    init {
        observerFavoriteIds("")
    }

    fun refresh() {

    }


    fun deleteFavorite(favorite: FavoriteCityWeather) {

    }
}

sealed interface FavoriteWeatherState {
    data class Success(val data: List<FavoriteCityWeather>) : FavoriteWeatherState
    object Error : FavoriteWeatherState
    object Loading : FavoriteWeatherState
}

data class FavoriteUiState(
    val favorites: List<FavoriteCityWeather> = emptyList(),
    val message: UiMessage? = null,
    val loading: Boolean = false
) {
    companion object {
        val Empty = FavoriteUiState()
        const val FAVORITE_SCREEN = "FavoriteScreen"
    }
}