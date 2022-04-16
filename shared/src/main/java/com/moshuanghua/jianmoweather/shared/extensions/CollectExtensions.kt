package com.moshuanghua.jianmoweather.shared.extensions

import com.moshuanghua.jianmoweather.shared.UiMessage
import com.moshuanghua.jianmoweather.shared.UiMessageManager
import com.moshuanghua.jianmoweather.shared.usecase.InvokeError
import com.moshuanghua.jianmoweather.shared.usecase.InvokeStarted
import com.moshuanghua.jianmoweather.shared.usecase.InvokeStatus
import com.moshuanghua.jianmoweather.shared.usecase.InvokeSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

suspend fun Flow<InvokeStatus>.collectStatus(
    loadingCounter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
) = collect {
    when (it) {
        is InvokeStarted -> {
            loadingCounter.add()
        }
        is InvokeSuccess -> {
            delay(600)
            loadingCounter.remove()
        }
        is InvokeError -> {
            uiMessageManager?.emitMessage(UiMessage(t = it.throwable))
            Timber.e("Update-Error: ${it.throwable}")
            loadingCounter.remove()
        }
    }
}

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())
    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun add() {
        loadingState.value = count.incrementAndGet() // 以原子方式将当前值加一并获取该值
    }

    fun remove() {
        loadingState.value = count.decrementAndGet()
    }
}