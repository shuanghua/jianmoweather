package jianmoweather.home.weather

import androidx.compose.runtime.Immutable
import com.moshuanghua.jianmoweather.shared.UiMessage
import jianmoweather.data.db.entity.*

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