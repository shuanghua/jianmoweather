package com.moshuanghua.jianmoweather.shared

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Result<T>,
    errorMessage: String
): Result<T> {
    return try {
        call()
    } catch(e: Exception) {
        Result.Error(Exception("$errorMessage: ${e.message}"))
    }
}