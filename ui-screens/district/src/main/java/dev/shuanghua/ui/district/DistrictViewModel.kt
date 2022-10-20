package dev.shuanghua.ui.district

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.entity.District
import dev.shuanghua.weather.data.usecase.ObserverDistrictUseCase
import dev.shuanghua.weather.data.usecase.UpdateDistrictUseCase
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

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DistrictViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateDistrict: UpdateDistrictUseCase,
    observerDistrict: ObserverDistrictUseCase
) : ViewModel() {

    private val cityId: String =
        checkNotNull(savedStateHandle[DistrictDestination.cityIdArg])
    private val obtId: String =
        checkNotNull(savedStateHandle[DistrictDestination.obtIdArg])

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val uiState: StateFlow<DistrictUiState> = combine(
        observerDistrict.flow,
        uiMessageManager.flow,
        observerLoading.flow
    ) { district, message, loading ->
        DistrictUiState(
            list = district,
            message = message,
            loading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DistrictUiState.Empty,
    )

    init {
        observerDistrict(Unit)
        refresh()
    }


    fun refresh() {
        viewModelScope.launch {
            updateDistrict(UpdateDistrictUseCase.Params(cityid = cityId, obtid = obtId))
                .collectStatus(observerLoading, uiMessageManager)
        }
    }
}

data class DistrictUiState(
    val list: List<District> = emptyList(),
    val message: UiMessage? = null,
    val loading: Boolean = false
) {
    companion object {
        val Empty = DistrictUiState()
    }
}

