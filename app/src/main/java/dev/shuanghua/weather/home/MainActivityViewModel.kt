package dev.shuanghua.weather.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.datastore.settings.SettingsDataSource
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//companion object operator invoke
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    dataStoreRepository: SettingsDataSource,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        dataStoreRepository.theme.map {
            MainActivityUiState.Success(
                themeSettings = ThemeSettings(it)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainActivityUiState.Loading,
        )
}

data class ThemeSettings(
    val themeConfig: ThemeConfig,
)

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val themeSettings: ThemeSettings) : MainActivityUiState
}