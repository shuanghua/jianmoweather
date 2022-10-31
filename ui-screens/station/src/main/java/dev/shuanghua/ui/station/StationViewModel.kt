package dev.shuanghua.ui.station

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.Station
import dev.shuanghua.weather.data.db.entity.StationReturn
import dev.shuanghua.weather.data.usecase.ObserverAutoStationUseCase
import dev.shuanghua.weather.data.usecase.ObserverStationUseCase
import dev.shuanghua.weather.data.usecase.UpdateStationReturnUseCase
import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.extensions.ObservableLoadingCounter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observerStation: ObserverStationUseCase,
    observerAutoStation: ObserverAutoStationUseCase,
    private val stationDao: StationDao,
    private val dispatchers: AppCoroutineDispatchers,
    private val updateStationReturn: UpdateStationReturnUseCase
) : ViewModel() {

    private val districtName: String =
        checkNotNull(savedStateHandle[StationDestination.districtNameArg])

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<StationUiState> = combine(
        observerStation.flow, observerAutoStation.flow, uiMessageManager.flow, observerLoading.flow
    ) { station, autoStation, message, loading ->
        StationUiState(
            list = station,
            autoStationId = autoStation.id,
            autoStationName = autoStation.name,
            message = message,
            loading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StationUiState.Empty,
    )

    init {
        observerStation(districtName)
        observerAutoStation(Unit)
    }

    fun update(obtId: String, isLocation: String) {
        viewModelScope.launch(dispatchers.io) {
            val stationReturn = StationReturn(
                screen = "StationScreen",
                obtId = obtId,
                isLocation = isLocation
            )
            Timber.d("Update0:$stationReturn")

            stationDao.insertStationReturn(stationReturn)
        }
    }
}

data class StationUiState(
    val list: List<Station> = emptyList(),
    val autoStationId: String = "",
    val autoStationName: String = "",
    val message: UiMessage? = null,
    val loading: Boolean = false
) {
    companion object {
        val Empty = StationUiState()
    }
}