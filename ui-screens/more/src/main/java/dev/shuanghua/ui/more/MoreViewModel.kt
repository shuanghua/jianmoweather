package dev.shuanghua.ui.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.datastore.DataStoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val themeModeState: StateFlow<ThemeModeUiState> =
        dataStoreRepository.themeMode.map {
            ThemeModeUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeModeUiState(),
        )

    //TODO需要读取上次保存的主题模式参数，然后显示到Screen上
    fun setThemeMode(tm: String) {
        viewModelScope.launch {
            val mode = when (tm) {
                "暗色" -> 0
                "亮色" -> 1
                else -> 2
            }
            dataStoreRepository.setThemeMode(mode)
        }
    }
}

data class ThemeModeUiState(val tm: Int = 2)