package dev.shuanghua.weather.shared.usecase

import androidx.lifecycle.MediatorLiveData
import dev.shuanghua.weather.shared.Result

abstract class MediatorUseCase<in P, R> {
    protected val result = MediatorLiveData<Result<R>>()

    open fun observe(): MediatorLiveData<Result<R>> {
        return result
    }

    abstract fun execute(parameters: P)
}