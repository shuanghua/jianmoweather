package dev.shuanghua.weather.shared.extensions

import dev.shuanghua.weather.shared.UiMessage
import dev.shuanghua.weather.shared.UiMessageManager
import dev.shuanghua.weather.shared.usecase.InvokeError
import dev.shuanghua.weather.shared.usecase.InvokeStarted
import dev.shuanghua.weather.shared.usecase.InvokeStatus
import dev.shuanghua.weather.shared.usecase.InvokeSuccess
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
            Timber.e("CollectExtensions: ${it.throwable.message}")
            loadingCounter.remove()
        }
    }
}

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())
    val flow: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged() //每次都生成一个新的flow，互不影响

    fun add() {
        loadingState.value = count.incrementAndGet() // 以原子方式将当前值加一并获取该值
    }

    fun remove() {
        loadingState.value = count.decrementAndGet()
    }
}