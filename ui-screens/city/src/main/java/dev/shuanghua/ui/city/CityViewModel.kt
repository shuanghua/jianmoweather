package dev.shuanghua.ui.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.dao.FavoriteDao
import dev.shuanghua.weather.data.db.entity.CityEntity
import dev.shuanghua.weather.data.db.entity.FavoriteCityEntity
import dev.shuanghua.weather.data.db.entity.asExternalModel
import dev.shuanghua.weather.data.model.CityResource
import dev.shuanghua.weather.data.usecase.GetCityListUseCase
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,//存储传过来的省份ID
    getCityListUseCase: GetCityListUseCase,
    private val favoriteDao: FavoriteDao,//直接插入数据库
) : ViewModel() {

    private val provinceId: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceIdArg])
    private val provinceName: String =
        checkNotNull(savedStateHandle[CityScreenDestination.provinceNameArg])

    private val cityList: Flow<Result<List<CityEntity>>> = getCityListUseCase(provinceId).asResult()

    val uiState: StateFlow<CityUiState> = cityList.map { result: Result<List<CityEntity>> ->
        when (result) {
            is Result.Loading -> CityUiState.Loading
            is Result.Error -> CityUiState.Error
            is Result.Success -> CityUiState.Success(
                CityUiModel(
                    topBarTitle = provinceName,
                    cityList = result.data.map { it.asExternalModel() }
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CityUiState.Loading
    )

    fun addCityIdToFavorite(city: CityResource) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteCity = FavoriteCityEntity(cityId = city.id, cityName = city.name)
            favoriteDao.insertCity(favoriteCity)
        }
    }
}

data class CityUiModel(
    val topBarTitle: String,
    val cityList: List<CityResource>,
)

sealed interface CityUiState {
    data class Success(val data: CityUiModel) : CityUiState
    object Error : CityUiState
    object Loading : CityUiState
}
