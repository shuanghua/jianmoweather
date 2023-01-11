package dev.shuanghua.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.datastore.settings.SettingsDataSource
import dev.shuanghua.weather.data.android.model.ThemeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: SettingsDataSource,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> = dataStoreRepository.theme.map {
        SettingsUiState.Success(ThemeSettings(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState.Loading,
    )

    fun updateThemeConfig(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            dataStoreRepository.setThemeMode(themeConfig)
        }
    }
}

data class ThemeSettings(
    val themeConfig: ThemeConfig,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val themeSettings: ThemeSettings) : SettingsUiState
}

