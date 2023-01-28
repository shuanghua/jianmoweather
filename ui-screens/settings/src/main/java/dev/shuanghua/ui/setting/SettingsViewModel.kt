package dev.shuanghua.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.model.ThemeConfig
import dev.shuanghua.weather.data.android.repository.SettingsRepository
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.asResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsRepository,
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> =
        settings.getTheme().asResult().map {
            when (it) {
                is Result.Error -> SettingsUiState.Error(
                    it.exception.message!!
                )

                is Result.Success -> SettingsUiState.Success(
                    ThemeSettings(it.data)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState.Loading,
        )

    fun updateTheme(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            settings.setTheme(themeConfig)
        }
    }
}

data class ThemeSettings(
    val themeConfig: ThemeConfig,
)

sealed interface SettingsUiState {
    object Loading : SettingsUiState

    data class Error(
        val errorMessage: String
    ) : SettingsUiState

    data class Success(
        val themeSettings: ThemeSettings
    ) : SettingsUiState
}

