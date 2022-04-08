package com.moshuanghua.jianmoweather.shared.usecase

import androidx.lifecycle.MediatorLiveData
import com.moshuanghua.jianmoweather.shared.Result

abstract class MediatorUseCase<in P, R> {
    protected val result = MediatorLiveData<Result<R>>()

    open fun observe(): MediatorLiveData<Result<R>> {
        return result
    }

    abstract fun execute(parameters: P)
}