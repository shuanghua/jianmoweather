/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.moshuanghua.jianmoweather.shared.retrofit

import androidx.lifecycle.LiveData
import retrofit2.*
import retrofit2.CallAdapter.Factory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 基本上就是直接复制粘贴
 * 只需要把 LiveData::class.java 和 ApiResponse::class.java 换成自定义的类型
 */
class LiveDataCallAdapterFactory : Factory() {
    //重写get函数
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //1 检查 service 中函数的返回类型是否一致
        if(LiveData::class.java != getRawType(returnType)) return null
        if (returnType !is ParameterizedType)
            throw IllegalStateException(
                "LiveData return type must be parameterized as LiveData<Foo> or LiveData<out Foo>"
            )

        //2 生成 Call -> LiveData
        val responseType = getParameterUpperBound(0, returnType)
        //3 生成请求 Response -> ApiResponse
        val rawLiveDataType = getRawType(responseType)
        //4 检查确认 Response -> ApiResponse
        if(rawLiveDataType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }

        //5 检查 Call -> LiveData
        if(responseType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        //6 通过 Call / LiveData 来生成 body 类型
        val bodyType = getParameterUpperBound(0, responseType)

        //7 返回自定义的 CallAdapter -> LiveDataCallAdapter
        return LiveDataCallAdapter<Any>(bodyType)
    }
}

class LiveDataCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {

        return object : LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(ApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(ApiResponse.create(throwable))
                        }
                    })
                }
            }
        }
    }
}