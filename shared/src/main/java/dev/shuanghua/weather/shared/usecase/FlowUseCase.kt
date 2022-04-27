package dev.shuanghua.weather.shared.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()


/**
 * 用于更新数据的 UseCase
 */
abstract class UpdateUseCase<in P> {
    operator fun invoke(params: P): Flow<InvokeStatus> = flow { // invoke 调用
        try {
            withTimeout(defaultTimeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        } catch(t: Throwable) {
            emit(InvokeError(t))
        }
    }.catch {
        emit(InvokeError(it))
    }

    suspend fun executeSync(params: P) = doWork(params) //普通调用
    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(4)
    }
}

/**
 * 用于观察的数据变动的 UseCase
 */
abstract class ObservableUseCase<P : Any, T> {

    // 全局变量在类创建的时候定义， 并提前于  invoke
    private val paramState = MutableSharedFlow<P>(  // ----------第1步,声明变量
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    // 对默认 SharedFlow 进行修改 (具体的修改算法在 createObservable) ，然后转换生成一个新的 Flow
    @ExperimentalCoroutinesApi
    val flow: Flow<T> = paramState  //----------第3步
        .distinctUntilChanged() // 当前值与上一次不同时，才会发出
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged() // Flow 中掉重复

    operator fun invoke(params: P) { //----------第2步，通过 invoke 调用来使用
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}