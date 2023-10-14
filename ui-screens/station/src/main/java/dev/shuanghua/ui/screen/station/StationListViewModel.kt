package dev.shuanghua.ui.screen.station

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shuanghua.weather.data.android.model.SelectedStation
import dev.shuanghua.weather.data.android.model.Station
import dev.shuanghua.weather.data.android.repository.DistrictStationRepository
import dev.shuanghua.weather.shared.AppDispatchers
import dev.shuanghua.weather.shared.UiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 无网络获取
 * 根据 传递的区县名，向数据库 查询对应 站点列表
 */
class StationViewModel(
	savedStateHandle: SavedStateHandle,
	private val repository: DistrictStationRepository,
	private val dispatchers: AppDispatchers,
) : ViewModel() {

	//区县名 用于向 数据库 获取对应 站点列表
	private val districtName: String = checkNotNull(savedStateHandle[districtNameArg])

	private val viewModelState = MutableStateFlow(ViewModelState(isLoading = true))

	val uiState: StateFlow<StationsUiState> =
		viewModelState.map(ViewModelState::toUiState)
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000),
				initialValue = viewModelState.value.toUiState(),
			)

	init {
		viewModelScope.launch(dispatchers.io) {
			repository.observerStationList(districtName)
				.collect { stationList ->
					if (stationList.isEmpty()) {
						viewModelState.update {
							val errorMessage =
								it.errorMessage + UiMessage("数据库没有站点数据:$districtName")
							it.copy(isLoading = false, errorMessage = errorMessage)
						}
					} else {
						viewModelState.update {
							it.copy(isLoading = false, stationList = stationList)
						}
					}
				}
		}
	}

	fun saveSelectedStation(obtId: String) {
		viewModelScope.launch(dispatchers.io) {
			val selectedStation = SelectedStation(
				obtId = obtId,
				isLocation = "1"   //返回到首页定位则传1，完美情况应该根据定位是否成功来判定
			)
			repository.saveSelectedStation(selectedStation)
		}
	}
}

sealed interface StationsUiState {
	val isLoading: Boolean
	val errorMessage: List<UiMessage>

	data class NoData(
		override val isLoading: Boolean,
		override val errorMessage: List<UiMessage>
	) : StationsUiState

	data class HasData(
		override val isLoading: Boolean,
		override val errorMessage: List<UiMessage>,
		val stationList: List<Station>
	) : StationsUiState
}

private data class ViewModelState(
	val isLoading: Boolean = false,
	val errorMessage: List<UiMessage> = emptyList(),
	val stationList: List<Station> = emptyList()
) {
	fun toUiState(): StationsUiState {
		return if (stationList.isEmpty()) {
			StationsUiState.NoData(
				isLoading = isLoading,
				errorMessage = errorMessage
			)
		} else {
			StationsUiState.HasData(
				isLoading = isLoading,
				errorMessage = errorMessage,
				stationList = stationList
			)
		}
	}
}