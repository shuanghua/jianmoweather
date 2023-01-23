package dev.shuanghua.ui.district

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.android.domain.usecase.ObserverDistrictListUseCase
import dev.shuanghua.weather.data.android.domain.usecase.UpdateDistrictListUseCase
import dev.shuanghua.weather.data.android.model.District
import dev.shuanghua.weather.shared.ObservableLoadingCounter
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 有网络获取
 * 从网络获取 区县 和 站点 的所有数据
 */
@HiltViewModel

class DistrictViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observerDistrict: ObserverDistrictListUseCase,
    private val updateDistrict: UpdateDistrictListUseCase
) : ViewModel() {

    private val cityId: String = checkNotNull(savedStateHandle[cityIdArg])
    private val obtId: String = checkNotNull(savedStateHandle[obtIdArg])

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    // 观察数据库
    // 为空->请求网络->缓存数据库
    // 不为空->直接显示

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DistrictUiState> = combine(
        observerDistrict.flow,
        uiMessageManager.flow,
        observerLoading.flow
    ) { districtList, message, loading ->
        if (districtList.isEmpty()) {
            refresh()
            DistrictUiState(
                list = emptyList(),
                message = message,
                loading = loading,
            )
        } else {
            DistrictUiState(
                list = districtList,
                message = message,
                loading = loading,
            )
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DistrictUiState.Empty,
    )

    init {
        observerDistrict(Unit)
    }


    fun refresh() {
        viewModelScope.launch {
            updateDistrict(
                UpdateDistrictListUseCase.Params(
                    cityId = cityId,
                    obtId = obtId
                )
            ).collectStatus(observerLoading, uiMessageManager)
        }
    }
}

data class DistrictUiState(
    val list: List<District> = emptyList(),
    val message: UiMessage? = null,
    val loading: Boolean = false,
) {
    companion object {
        val Empty = DistrictUiState()
    }
}

