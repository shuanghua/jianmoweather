package dev.shuanghua.weather.shared.retrofit

import android.annotation.SuppressLint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import retrofit2.CallAdapter.Factory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class DeferredCallAdapterFactory private constructor() : Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() =
            DeferredCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if(Deferred::class.java != getRawType(returnType)) return null

        if(returnType !is ParameterizedType)
            throw IllegalStateException(
                "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>"
            )

        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)

        if(rawDeferredType != Deferred::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }

        return when(rawDeferredType) { //DeferredResponseCallAdapter, DeferredApiResponseCallAdapter, DeferredBodyCallAdapter
            //使用 Retrofit 的 Response
            Response::class.java -> {//Deferred<Response<User>>
                if(responseType !is ParameterizedType) {
                    throw IllegalStateException(
                        "Response must be parameterized as Response<Foo> or Response<out Foo>"
                    )
                }
                DeferredResponseCallAdapter<Any>(
                    getParameterUpperBound(0, responseType)
                )
            }
            //使用自定义的 Response
            ApiResponse::class.java -> {//Deferred<ApResponse<User>>
                if(responseType !is ParameterizedType) {
                    throw IllegalStateException(
                        "Response must be parameterized as Response<Foo> or Response<out Foo>"
                    )
                }
                DeferredApiResponseCallAdapter<Any>(
                    getParameterUpperBound(0, responseType)
                )
            }
            else -> {//Call<User>
                DeferredBodyCallAdapter<Any>(responseType)
            }
        }
    }
}

class DeferredBodyCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Deferred<T>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): Deferred<T> {
        val deferred = CompletableDeferred<T>()

        deferred.invokeOnCompletion {
            if(deferred.isCancelled) {
                call.cancel()
            }
        }

        call.enqueue(object : Callback<T> {
            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<T>, t: Throwable) {
                deferred.completeExceptionally(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful) {
                    deferred.complete(response.body()!!)
                } else {
                    deferred.completeExceptionally(HttpException(response))
                }
            }
        })

        return deferred
    }
}

class DeferredApiResponseCallAdapter<T>(private val responseType: Type) :
    CallAdapter<T, Deferred<ApiResponse<T>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Deferred<ApiResponse<T>> {
        val deferred = CompletableDeferred<ApiResponse<T>>()
        deferred.invokeOnCompletion {
            if(deferred.isCancelled) {
                call.cancel()
            }
        }
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                deferred.complete(ApiResponse.create(t))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful) deferred.complete(ApiResponse.create(response))
            }
        })
        return deferred
    }
}

class DeferredResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Deferred<Response<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): Deferred<Response<T>> {
        val deferred = CompletableDeferred<Response<T>>()

        deferred.invokeOnCompletion {
            if(deferred.isCancelled) {
                call.cancel()
            }
        }

        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                deferred.completeExceptionally(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                deferred.complete(response)
            }
        })

        return deferred
    }
}

