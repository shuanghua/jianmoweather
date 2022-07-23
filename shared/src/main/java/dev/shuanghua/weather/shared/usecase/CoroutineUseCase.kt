package dev.shuanghua.weather.shared.usecase

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class CoroutineUseCase<in P, R>(
    private val context: CoroutineContext,
) {
    protected abstract suspend fun execute(params: P): R
    suspend operator fun invoke(params: P): R {
        return try {
            withContext(context) {
                execute(params)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}