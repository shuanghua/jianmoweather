package jianmoweather.home.favorite

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class FavoriteViewModel : ViewModel() {
    val loadingStateFlow: StateFlow<LoadingUiState> ? = null
}

data class LoadingUiState(val isLoading: Boolean = false) {
    companion object {
        val Hide = LoadingUiState()
    }
}