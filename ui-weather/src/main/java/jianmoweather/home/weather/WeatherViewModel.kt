package jianmoweather.home.weather

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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val updateWeather: UpdateWeatherUseCase, // 网络刷新保存到数据库 FlowWithStatusUseCase
    observerWeatherUseCase: ObserverWeatherUseCase,  // pojo  SubjectInteractor
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()//flow

    val uiStateFlow: SharedFlow<WeatherUiState> = combine( // 官方 combine 默认最多只支持传入 5 个 Flow
        observerWeatherUseCase.flow, loadingState.observable, uiMessageManager.message
    ) { weather, loadingState, message ->
        println("-VM $weather------------------------")

        if(weather != null) {
            val state = WeatherUiState(
                weatherScreenEntity = weather.temperature,
                alarms = weather.alarms,
                oneDays = weather.oneDays,
                others = weather.others,
                oneHours = weather.oneHours,
                healthExponents = weather.healthExponents,

                refreshing = loadingState,
                message = message
            )
            state
        } else {
            WeatherUiState.Empty
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeatherUiState.Empty,
    )

    init {
        // 定位为标志的主键, 只观察定位城市的数据库表, 这样不用因为跨城市情况而需要重新设置观察
        observerWeatherUseCase(ObserverWeatherUseCase.Params(HOME_SCREEN))
        refresh()
    }

    /**
     * 后台自动更新定位，
     * 观察参数数据表，一旦出现变动则自动更新天气
     */
    fun autoRefresh(screen: String = HOME_SCREEN) { //
        viewModelScope.launch {
            updateWeather(screen).collectStatus(
                loadingState, uiMessageManager
            )
        }
    }


    /**
     * requestParamsWithLocation(): 返回具体数据 Result<Params>
     * updateWeather(params): 不返回具体数据，只返回Flow<InvokeStatus>
     * 下拉刷新的方式 手动调用定位 + 获取天气数据
     */
    fun refresh() {
        viewModelScope.launch {  // launch 不阻塞当前线程      runBlocking 中断阻塞当前线程
            updateWeather(HOME_SCREEN).collectStatus(
                loadingState, uiMessageManager
            )
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}

data class WeatherUiState(
    val weatherScreenEntity: WeatherScreenEntity? = null,
    val alarms: List<Alarm> = emptyList(),
    val oneHours: List<OneHour> = emptyList(),
    val oneDays: List<OneDay> = emptyList(),
    val others: List<OtherItem> = emptyList(),
    val healthExponents: List<HealthExponent> = emptyList(),
    val message: UiMessage? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = WeatherUiState()
        const val HOME_SCREEN = "WeatherScreen"
    }
}