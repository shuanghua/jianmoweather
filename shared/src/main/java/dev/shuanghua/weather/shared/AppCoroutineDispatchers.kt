package dev.shuanghua.weather.shared

import kotlinx.coroutines.CoroutineDispatcher

data class AppCoroutineDispatchers(
    val io: CoroutineDispatcher,
    val cpu: CoroutineDispatcher,
    val main: CoroutineDispatcher
)