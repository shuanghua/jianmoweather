package dev.shuanghua.weather.shared.usecase

import dev.shuanghua.weather.shared.AppCoroutineDispatchers
import dev.shuanghua.weather.shared.Result
import kotlinx.coroutines.withContext

abstract class CoroutineResultUseCase<in P, R>(
    private val dispatchers: AppCoroutineDispatchers
) {
    suspend operator fun invoke(parameters: P): Result<R> {
        return try {
            withContext(dispatchers.io) {
                execute(parameters).let {
                    Result.Success(it)
                }
            }
        } catch(e: Exception) {
            Result.Error(e)
        }
    }

    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(dispatchers.io) {
                execute(null).let { Result.Success(it) }
            }
        } catch(e: Exception) {
            Result.Error(e)
        }
    }

    protected abstract suspend fun execute(parameters: P?): R
}