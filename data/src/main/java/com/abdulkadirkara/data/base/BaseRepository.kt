package com.abdulkadirkara.data.base

import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseRepository {

    suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> com.abdulkadirkara.common.networkResponse.NetworkResponse<T>,
        transform: (T) -> R
    ): Flow<com.abdulkadirkara.common.networkResponse.NetworkResponse<R>> = flow {
        emit(com.abdulkadirkara.common.networkResponse.NetworkResponse.Loading)
        val response = apiCall()
        response.onSuccess { data ->
            emit(com.abdulkadirkara.common.networkResponse.NetworkResponse.Success(transform(data)))
        }.onEmpty {
            emit(com.abdulkadirkara.common.networkResponse.NetworkResponse.Empty)
        }.onLoading {
            emit(com.abdulkadirkara.common.networkResponse.NetworkResponse.Loading)
        }.onError {
            emit(it)
        }
    }
}