package dev.shuanghua.weather.shared

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

sealed class InvokeStatus
object InvokeStarted : InvokeStatus()
object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()

/**
 * 带有返回值
 * 不含有InvokeStatus
 */
abstract class UseCase<in P, R>(
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
 *  这里 将 UpdateUseCase 的 invoke 设计成一个返回值为 Flow 的方法
 *  Flow 中不含有具体数据，只有加载过程中的状态
 */
abstract class UpdateUseCase<in P> {
    operator fun invoke(params: P): Flow<InvokeStatus> = flow { // invoke 调用
        withTimeout(defaultTimeoutMs) {
            emit(InvokeStarted)  // 修改 emit 的发送线程需使用 flowOn()  ，不能使用 withContext()
            doWork(params)
            emit(InvokeSuccess)
        }
    }.catch {
        emit(InvokeError(it))
    }

    suspend fun executeSync(params: P) = doWork(params)           // 普通调用, 不包含 InvokeStatus, 也不返回具体数据
    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(4)
    }
}

/**
 * 用于观察 数据库数据 变动的 UseCase
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
        .distinctUntilChanged() // 当值与上一次不同时，才会发出  , 相同则丢弃当前值
        .flatMapLatest { createObservable(it) } //  根据传入参数, 获取数据库数据
        .distinctUntilChanged()  // 只能过滤连续且重复的数据，不能过滤集合中不相邻的重复数据

    operator fun invoke(params: P) { //----------第2步，invoke 把参数放到 sharedFlow
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}