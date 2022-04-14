package jianmoweather.home.weather

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moshuanghua.jianmoweather.shared.UiMessage
import com.moshuanghua.jianmoweather.shared.UiMessageManager
import com.moshuanghua.jianmoweather.shared.extensions.ObservableLoadingCounter
import com.moshuanghua.jianmoweather.shared.extensions.collectStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import jianmoweather.data.db.entity.*
import jianmoweather.data.usecase.ObserverWeatherUseCase
import jianmoweather.data.usecase.UpdateWeatherUseCase
import jianmoweather.home.weather.WeatherUiState.Companion.HOME_SCREEN
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WeatherViewModel @Inject constructor(
	private val updateWeather: UpdateWeatherUseCase, // 网络刷新保存到数据库 FlowWithStatusUseCase
	observerWeatherUseCase: ObserverWeatherUseCase,  // pojo  SubjectInteractor
) : ViewModel() {

	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()//flow

	val refreshState: StateFlow<LoadingUiState> = // flow to stateflow
		observerLoading.observable.map { LoadingUiState(it) }.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = LoadingUiState.Hide
		)


	val uiStateFlow: StateFlow<WeatherUiState> = combine( // 官方 combine 默认最多只支持传入 5 个 Flow
		observerWeatherUseCase.flow,
		uiMessageManager.message
	) { weather, message ->
		if(weather != null) {
			WeatherUiState(
				temperature = weather.temperature,
				alarms = weather.alarms,
				oneHours = weather.oneHours,
				oneDays = weather.oneDays,
				others = weather.others,
				exponents = weather.exponents,
				message = message,
			)
		} else {
			WeatherUiState.Empty
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = WeatherUiState.Empty,
	)

	init {
		refresh()
		// 定位为标志的主键, 只观察定位城市的数据库表, 这样不用因为跨城市情况而需要重新设置观察
		observerWeatherUseCase(ObserverWeatherUseCase.Params(HOME_SCREEN))
	}

	/**
	 * 后台自动更新定位，
	 * 观察参数数据表，一旦出现变动则自动更新天气
	 */
//	fun autoRefresh(screen: String = HOME_SCREEN) { //
//		viewModelScope.launch {
//			updateWeather(UpdateWeatherUseCase.Params(HOME_SCREEN))
//				.collectStatus(observerLoading, uiMessageManager)
//		}
//	}

	/**
	 * requestParamsWithLocation(): 返回具体数据 Result<Params>
	 * updateWeather(params): 不返回具体数据，只返回Flow<InvokeStatus>
	 */
	fun refresh() {
		viewModelScope.launch {
			updateWeather(UpdateWeatherUseCase.Params(HOME_SCREEN))
				.collectStatus(observerLoading, uiMessageManager)
		}
	}

	fun clearMessage(id: Long) {
		viewModelScope.launch {
			uiMessageManager.clearMessage(id)
		}
	}
}

@Immutable
data class WeatherUiState(
	val temperature: Temperature? = null,
	val alarms: List<Alarm> = emptyList(),
	val oneHours: List<OneHour> = emptyList(),
	val oneDays: List<OneDay> = emptyList(),
	val others: List<Condition> = emptyList(),
	val exponents: List<Exponent> = emptyList(),
	val message: UiMessage? = null,
//    val refreshing: Boolean = false
) {
	companion object {
		val Empty = WeatherUiState()
		const val HOME_SCREEN = "WeatherScreen"
	}
}

data class LoadingUiState(val isLoading: Boolean = false) {
	companion object {
		val Hide = LoadingUiState()
	}
}