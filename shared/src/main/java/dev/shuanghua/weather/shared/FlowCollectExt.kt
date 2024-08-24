package dev.shuanghua.weather.shared

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

/**
 * 扩展函数，用于收集 InvokeStatus 流并处理状态
 *
 * 如果需要在成功时收集返回值，请重新定义一个新的collectStatus函数，
 * 并再定义一个 uiStateFlow 的参数，并在 InvokeSuccess 时 emit 返回值
 */
suspend fun Flow<InvokeStatus>.collectStatus(
    loadingCounter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
) = collect {
    when (it) {
        is InvokeStarted -> loadingCounter.add() // +1
        is InvokeSuccess -> {
            delay(150L) // 设置一个最小延迟
            loadingCounter.remove() // -1
        }
        is InvokeError -> {
            loadingCounter.remove() // -1
            uiMessageManager?.emitMessage(UiMessage(t = it.throwable))
        }
    }
}

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())
    val flow: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged() // 每次调用都生成一个新的flow

    fun add() {
        loadingState.value = count.incrementAndGet() // 以原子方式将当前值加一并获取该值
    }

    fun remove() {
        loadingState.value = count.decrementAndGet()
    }
}