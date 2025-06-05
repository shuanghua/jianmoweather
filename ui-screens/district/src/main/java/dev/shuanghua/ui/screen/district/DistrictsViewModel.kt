package dev.shuanghua.ui.screen.district

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shuanghua.weather.data.android.domain.ObserverDistrictUseCase
import dev.shuanghua.weather.data.android.domain.UpdateDistrictUseCase
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

/**
 * 有网络获取
 * 从网络获取 区县 和 站点 的所有数据
 */
class DistrictListViewModel(
	savedStateHandle: SavedStateHandle,
	observerDistrict: ObserverDistrictUseCase,
	private val updateDistrictUseCase: UpdateDistrictUseCase
) : ViewModel() {

	private val cityId: String = checkNotNull(savedStateHandle["cityId"])
	private val stationName: String = checkNotNull(savedStateHandle["stationName"])

	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()

	// 观察数据库
	// 为空->请求网络->缓存数据库
	// 不为空->直接显示

	@OptIn(ExperimentalCoroutinesApi::class)
	val uiState: StateFlow<DistrictListUiState> = combine(
		observerDistrict.flow,
		uiMessageManager.flow,
		observerLoading.flow
	) { districtList, message, isLoading ->
		if (districtList.isEmpty()) {
			DistrictListUiState(
				districtList = emptyList(),
				uiMessage = message,
				isLoading = isLoading,
			)
		} else {
			DistrictListUiState(
				districtList = districtList,
				uiMessage = message,
				isLoading = isLoading,
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = DistrictListUiState.Empty,
	)

	init {
		observerDistrict(Unit)
		refresh()
	}


	fun refresh() {
		viewModelScope.launch {
			updateDistrictUseCase(
				UpdateDistrictUseCase.Params(
					cityId = cityId
				)
			).collectStatus(observerLoading, uiMessageManager)
		}
	}
}

data class DistrictListUiState(
	val districtList: List<District> = emptyList(),
	val uiMessage: UiMessage? = null,
	val isLoading: Boolean = false,
) {
	companion object {
		val Empty = DistrictListUiState()
	}
}

