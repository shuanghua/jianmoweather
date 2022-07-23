package dev.shuanghua.ui.province

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.Province
import dev.shuanghua.weather.data.usecase.ObserverProvinceUseCase
import dev.shuanghua.weather.data.usecase.UpdateProvinceUseCase
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

@HiltViewModel
class ProvincesViewModel @Inject constructor(
    private val updateProvince: UpdateProvinceUseCase,
    observerProvince: ObserverProvinceUseCase
) : ViewModel() {
    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    init {
        observerProvince(ObserverProvinceUseCase.Params("ProvinceScreen"))
        refresh()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProvinceUiState> = combine(
        observerProvince.flow,
        uiMessageManager.flow,
        observerLoading.flow
    ) { provinces, message, loading ->
        ProvinceUiState(
            provinces = provinces,
            message = message,
            refreshing = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = ProvinceUiState.Empty
    )


    /**
     * 当本地数据库没有数据的时候会自动调用网络刷新
     */
    fun refresh() {
        viewModelScope.launch {
            updateProvince(Unit).collectStatus(observerLoading, uiMessageManager)
        }
    }
}

class ProvinceUiState(
    val provinces: List<Province> = emptyList(),
    val message: UiMessage? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = ProvinceUiState()
        const val FAVORITE_SCREEN = "ProvinceScreen"
    }
}