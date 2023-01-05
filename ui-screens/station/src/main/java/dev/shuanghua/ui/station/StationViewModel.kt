package dev.shuanghua.ui.station

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shuanghua.weather.data.db.dao.StationDao
import dev.shuanghua.weather.data.db.entity.SelectedStationEntity
import dev.shuanghua.weather.data.db.entity.StationEntity
import dev.shuanghua.weather.data.usecase.ObserverAutoStationUseCase
import dev.shuanghua.weather.data.usecase.ObserverStationUseCase
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
) : ViewModel() {
    private var autoStationName: String = ""

    //区县列表传递过来的区县名称
    //使用该名称向数据查询观测站点列表
    private val districtName: String =
        checkNotNull(savedStateHandle[StationDestination.districtNameArg])

    private val observerLoading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<StationUiState> = combine(
        observerStation.flow,
        observerAutoStation.flow,
        uiMessageManager.flow,
        observerLoading.flow
    ) { station, autoStation, message, loading ->
        autoStationName = autoStation.name
        StationUiState(
            list = station,
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

    /**
     * 保存当前选择的站点(非自动定位站点)，用于下一次打开应用时的请求参数
     * 自动站点使用: "" 和 "1"  非定位站点: "obtId" 和"0"
     */
    fun updateStation(obtId: String, obtName: String) {
        viewModelScope.launch(dispatchers.io) {
            val stationReturn = SelectedStationEntity(
                screen = "StationScreen",
                obtId = if (obtName == autoStationName) "G0000" else obtId,
                isLocation = "1"  //返回到首页定位则传1，完美情况应该根据定位是否成功来判定
            )
            stationDao.insertStationReturn(stationReturn)
        }
    }
}

data class StationUiState(
    val list: List<StationEntity> = emptyList(),
    val autoStationId: String = "",
    val autoStationName: String = "",
    val message: UiMessage? = null,
    val loading: Boolean = false,
) {
    companion object {
        val Empty = StationUiState()
    }
}