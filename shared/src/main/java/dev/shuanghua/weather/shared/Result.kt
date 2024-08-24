package dev.shuanghua.weather.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed class Result<out R> {
	data object Loading : Result<Nothing>()
	data class Success<out T>(val data: T) : Result<T>()
	data class Error<out T>(val exception: Exception, val data: T? = null) : Result<T>()
//	data class Error(val exception: Exception) : Result<Nothing>()
}

/**
 * 不关心 Error 信息 , 只关心结果
 */
fun <T> Result<T>.successOr(fallback: T): T {
	return (this as? Result.Success<T>)?.data ?: fallback
}

/**
 * 处理 Error
 * 把异常转成 Result.Error
 */
fun <T> Flow<T>.asResult(fallback: T? = null): Flow<Result<T>> {
	return this
		.map<T, Result<T>> { Result.Success(it) }
		.catch {
//			throw it
			emit(Result.Error(it as Exception, fallback))
		}
}

/**
 * Flow -> Flow<Result>
 */
//fun <T> Flow<T>.asResult(): Flow<Result<T>> {
//    return this
//        .map<T, Result<T>> { Result.Success(it) }
//        .onStart { emit(Result.Loading) }
//        .catch { emit(Result.Error(it)) }
//}