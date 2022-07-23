package dev.shuanghua.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.City
import dev.shuanghua.weather.data.db.entity.FavoriteId
import dev.shuanghua.weather.data.usecase.GetCityListUseCase
import dev.shuanghua.weather.data.usecase.UpdateCityIdsUseCase
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
    getCityListUseCase: GetCityListUseCase,
    private val favoriteDao: FavoriteDao,
    private val updateCityIdsUseCase: UpdateCityIdsUseCase
) : ViewModel() {

    private val provinceId: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceIdArg])
    private val provinceName: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceNameArg])

    private val cityList: Flow<Result<List<City>>> = getCityListUseCase(provinceId).asResult()

    val uiState: StateFlow<CityUiState> = cityList.map { cityResult ->
        Timber.d("-------$cityList-----")
        val cityDataState: CityDataState = when (cityResult) {
            is Result.Success -> CityDataState.Success(cityResult.data)
            is Result.Loading -> CityDataState.Loading
            is Result.Error -> CityDataState.Error
        }
        CityUiState(
            topBarTitle = provinceName,
            cityDataState = cityDataState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CityUiState("", CityDataState.Loading)
    )

    fun addCityIdToFavorite(cityId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteCityId = FavoriteId(cityId)
            favoriteDao.insertFavoriteId(favoriteCityId)
        }
    }
}

sealed interface CityDataState {
    data class Success(val data: List<City>) : CityDataState
    object Error : CityDataState
    object Loading : CityDataState
}

data class CityUiState(
    val topBarTitle: String,
    val cityDataState: CityDataState
)
