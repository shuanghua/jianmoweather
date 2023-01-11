package dev.shuanghua.weather.shared

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

suspend fun Flow<InvokeStatus>.collectStatus(
    loadingCounter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
) = collect {
    when (it) {
        is InvokeStarted -> loadingCounter.add()
        is InvokeSuccess -> {
            delay(150L)
            loadingCounter.remove()
        }

        is InvokeError -> {
            uiMessageManager?.emitMessage(UiMessage(t = it.throwable))
            Log.e("出错:", "${it.throwable.message}")
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

fun List<String>.asArrayList() = ArrayList<String>(this)