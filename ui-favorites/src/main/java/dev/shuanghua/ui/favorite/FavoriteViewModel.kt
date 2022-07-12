package dev.shuanghua.ui.favorite

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.Favorite
import dev.shuanghua.weather.data.usecase.AddFavoriteUseCase
import dev.shuanghua.weather.data.usecase.ObserverFavoriteCityWeather
import dev.shuanghua.weather.data.usecase.RemoveFavoriteUseCase
import dev.shuanghua.weather.data.usecase.UpdateFavoriteCityWeather
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import dev.shuanghua.weather.shared.extensions.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val WhileViewSubscribed = SharingStarted.WhileSubscribed(5000)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val updateFavoriteCityWeather: UpdateFavoriteCityWeather,
    observerFavoriteData: ObserverFavoriteCityWeather
) : ViewModel() {
    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

//    private var _favorites = MutableStateFlow<List<Favorite>>(emptyList())
//    val list: StateFlow<List<Favorite>>
//        get() = _favorites.stateIn(
//            scope = viewModelScope,
//            started = WhileViewSubscribed,
//            initialValue = emptyList()
//        )

    private var _favorites: SnapshotStateList<Favorite> = listOf<Favorite>().toMutableStateList()
    val list: List<Favorite>
        get() = _favorites.sortedBy { it.position }

    private val cityIdList: ArrayList<String> = arrayListOf(
        "01010054511", "02010058362", "28010159287", "32010145005"
    )

    init {
        observerFavoriteData(ObserverFavoriteCityWeather.Params(""))
        //refresh()
    }

    val uiState: StateFlow<FavoriteUiState> = combine(
        observerFavoriteData.flow,
        uiMessageManager.flow,
        observerLoading.observable
    ) { favorites, message, loading ->
        _favorites = favorites.toMutableStateList()
        FavoriteUiState(
            favorites = favorites,
            message = message,
            refreshing = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = FavoriteUiState.Empty
    )

    fun refresh() {
        viewModelScope.launch {
            updateFavoriteCityWeather(
                UpdateFavoriteCityWeather.Params(cityIdList)
            ).collectStatus(observerLoading, uiMessageManager)
        }
    }

//    fun test() {
//        _favorites.clear()
//        _favorites.addAll(getList.toMutableStateList())
//    }

//    fun addFavoriteToUi(favorite: Favorite) {
//        _favorites.add(favorite)
//    }
//
//    fun removeUiFavorite(favorite: Favorite) {
//        _favorites.remove(favorite)
//    }

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            addFavoriteUseCase.executeSync(AddFavoriteUseCase.Params(favorite))
        }
    }

    // 数据库移除
    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            removeFavoriteUseCase.executeSync(RemoveFavoriteUseCase.Params(favorite))
//            cityIdList.remove(favorite.cityid)
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
        const val FAVORITE_SCREEN = "FavoriteScreen"
    }
}