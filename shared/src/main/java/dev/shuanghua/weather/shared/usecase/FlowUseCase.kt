package dev.shuanghua.weather.shared.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()

abstract class UseCase<in P, R>(
    private val context: CoroutineContext = Dispatchers.IO
) {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }.flowOn(context)
    }

    protected abstract fun doWork(params: P): R
}

abstract class UseCase2<in P, R>(
    private val context: CoroutineContext = Dispatchers.IO
) {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }.flowOn(context)
    }

    protected abstract suspend fun doWork(params: P): R
}


/**
 * 用于更新数据的 UseCase ,没有结果返回
 */
abstract class UpdateUseCase<in P> {
    operator fun invoke(params: P): Flow<InvokeStatus> = flow { // invoke 调用
//        try {
            withTimeout(defaultTimeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
//        } catch (t: Throwable) {
//            emit(InvokeError(t))
//        }
    }.catch {
        emit(InvokeError(it))
    }

    suspend fun executeSync(params: P) = doWork(params)           // 普通调用,不包含 InvokeStatus
    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(3)
    }
}

/**
 * 用于观察的数据变动的 UseCase
 */
abstract class ObservableUseCase<P : Any, T> {

    // 用于观察参数, 全局变量在类创建的时候定义, 提前于  invoke
    private val paramState = MutableSharedFlow<P>(  // ----------第1步,声明变量
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    // 对参数 SharedFlow 进行变换 (具体的变换算法在 createObservable), 转换后生成一个新的 Flow
    @ExperimentalCoroutinesApi
    val flow: Flow<T> = paramState  //----------第3步
        .distinctUntilChanged() // 当前值与上一次不同时，才会发出
        .flatMapLatest { createObservable(it) } //  根据传入参数, 获取数据库数据
        .distinctUntilChanged() // Flow 中掉重复

    operator fun invoke(params: P) { //----------第2步，invoke 把参数传进 sharedFlow
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}