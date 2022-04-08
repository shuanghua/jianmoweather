package com.moshuanghua.jianmoweather.shared.livedata

import androidx.lifecycle.LiveData


/**
 * value 为 null 的一个LiveData
 */
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> = AbsentLiveData()
    }
}