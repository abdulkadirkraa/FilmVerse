package com.abdulkadirkara.common.networkResponse


import retrofit2.HttpException
import java.io.IOException

sealed interface NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>
    data object Loading : NetworkResponse<Nothing>
    data object Empty : NetworkResponse<Nothing>
    sealed interface Error : NetworkResponse<Nothing> {
        data class HttpError(val exception: HttpException) : Error
        data class NetworkError(val exception: IOException) : Error
        data class UnknownError(val exception: Throwable) : Error
    }
}

inline fun <T> NetworkResponse<T>.onSuccess(action: (T) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Success) {
        action(data)
    }
    return this
}

inline fun <T> NetworkResponse<T>.onLoading(action: () -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Loading) {
        action()
    }
    return this
}

inline fun <T> NetworkResponse<T>.onEmpty(action: () -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Empty) {
        action()
    }
    return this
}

inline fun <T> NetworkResponse<T>.onError(action: (NetworkResponse.Error) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Error) {
        action(this)
    }
    return this
}