package dev.shuanghua.ui.screen.province

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shuanghua.weather.data.android.domain.ObserverProvinceUseCase
import dev.shuanghua.weather.data.android.domain.UpdateProvinceUseCase
import dev.shuanghua.weather.data.android.model.Province
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

class ProvincesViewModel(
	private val updateProvince: UpdateProvinceUseCase,
	observerProvince: ObserverProvinceUseCase
) : ViewModel() {
	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()

	init {
		observerProvince(Unit)
		refresh()
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	val uiState: StateFlow<ProvinceUiState> = combine(
		observerProvince.flow,
		uiMessageManager.flow,
		observerLoading.flow
	) { provinces, message, isLoading ->
		if (provinces.isEmpty()) {
//            refresh()
			ProvinceUiState(
				provinces = emptyList(),
				uiMessage = message,
				isLoading = isLoading
			)
		} else {
			ProvinceUiState(
				provinces = provinces,
				uiMessage = message,
				isLoading = isLoading
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ProvinceUiState.Empty
	)


	/**
	 * 当本地数据库没有数据的时候会自动调用网络刷新
	 */
	fun refresh() {
		viewModelScope.launch {
			updateProvince(Unit).collectStatus(
				observerLoading,
				uiMessageManager
			)
		}
	}

	fun clearMessage(id: Long) {
		viewModelScope.launch {
			uiMessageManager.clearMessage(id)
		}
	}

}

class ProvinceUiState(
	val provinces: List<Province> = emptyList(),
	val uiMessage: UiMessage? = null,
	val isLoading: Boolean = false
) {
	companion object {
		val Empty = ProvinceUiState()
	}
}