package dev.shuanghua.weather.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.datastore.DataStoreRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//companion object operator invoke
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val themeModeUiState: StateFlow<ThemeModeUiState> =
        dataStoreRepository.themeMode.map {
            ThemeModeUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeModeUiState(),
        )

//    init {
//        viewModelScope.launch {
//            dataStoreRepository.setThemeMode(2)
//        }
//    }

    fun getThemeModel(): Flow<Int> {
        return dataStoreRepository.themeMode
    }
}

data class ThemeModeUiState(val tm: Int = 2)