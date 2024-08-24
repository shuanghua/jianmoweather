package dev.shuanghua.weather.shared

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import timber.log.Timber

sealed class InvokeStatus
data object InvokeStarted : InvokeStatus()
data object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()


/**
 *  这里 将 UpdateUseCase 的 invoke 设计成一个返回值为 Flow 的方法
 *  Flow 中不含有具体数据，只有加载过程中的状态
 *  将开始 -> 成功后 -> 失败, 这 3 种状态结束用 InvokeStatus 包装, 然后使用 flow 发送到 viewmodel
 */
abstract class UpdateUseCase<in P> {
	operator fun invoke(params: P): Flow<InvokeStatus> = flow { // invoke 调用
		withTimeout(10000) {
			emit(InvokeStarted)  // 修改 emit 的发送线程需使用 flowOn()  ，不能使用 withContext()
			doWork(params)
			emit(InvokeSuccess)
		}
	}.catch { // 异常捕获
		when (it) {
			is TimeoutCancellationException -> emit(InvokeError(Throwable("请求超时, 检查网络或者使用中国地区VPN")))
			else -> emit(InvokeError(it))
		}
	}

	suspend fun executeSync(params: P) = doWork(params) // 普通调用, 不包含 InvokeStatus, 也不返回具体数据

	protected abstract suspend fun doWork(params: P)
}

/**
 * 用于观察 数据库数据 变动的 UseCase
 * 如果数据库数据和 ui 一样, 则不会重复发送数据
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

/**
 * 要求 doWork 的返回值类型必须是 Flow 类型
 */
//abstract class FlowUseCase<P : Any, T> {
//	suspend operator fun invoke(params: P): Flow<T> {
//		return try {
//			 withTimeout(defaultTimeoutMs) {
//				doWork(params)
//			}
//		}catch (e: Exception) {
//			Timber.e(e)
//		}
//	}
//
//	protected abstract suspend fun doWork(params: P): Flow<T>
//
//	companion object {
//		private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(4)
//	}
//}