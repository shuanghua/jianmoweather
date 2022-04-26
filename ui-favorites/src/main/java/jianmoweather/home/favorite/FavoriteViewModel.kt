package jianmoweather.home.favorite

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor() : ViewModel() {
    val loadingStateFlow: StateFlow<LoadingUiState> = MutableStateFlow(LoadingUiState.Hide)
}

data class LoadingUiState(val isLoading: Boolean = false) {
    companion object {
        val Hide = LoadingUiState()
    }
}