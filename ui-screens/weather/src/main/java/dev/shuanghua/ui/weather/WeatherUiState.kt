package dev.shuanghua.ui.weather

import dev.shuanghua.weather.data.db.entity.*
import dev.shuanghua.weather.shared.UiMessage

//@Immutable
data class WeatherUiState(
    val temperature: Temperature? = null,
    val alarms: List<Alarm>? = emptyList(),
    val oneHours: List<OneHour>? = emptyList(),
    val oneDays: List<OneDay>? = emptyList(),
    val others: List<Condition>? = emptyList(),
    val exponents: List<Exponent>? = emptyList(),
    val message: UiMessage? = null,
    val loading: Boolean = false,
) {
    companion object {
        val Empty = WeatherUiState()
        const val HOME_SCREEN = "WeatherScreen"
    }
}