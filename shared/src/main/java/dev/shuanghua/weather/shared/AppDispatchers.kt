package dev.shuanghua.weather.shared

import kotlinx.coroutines.CoroutineDispatcher

data class AppDispatchers(
    val io: CoroutineDispatcher,
    val cpu: CoroutineDispatcher,
    val main: CoroutineDispatcher
)