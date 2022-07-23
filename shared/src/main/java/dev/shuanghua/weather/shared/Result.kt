package dev.shuanghua.weather.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Result<T>,
    errorMessage: String
): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        Result.Error(Exception("$errorMessage: ${e.message}"))
    }
}

/**
 * Flow -> Flow<Result>
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}
