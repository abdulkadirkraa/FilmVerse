package com.abdulkadirkara.data.base

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * BaseRepository is an abstract class that provides a common method for safely making API calls
 * and transforming the results into the desired format while handling different states of the
 * network response (loading, success, empty, error).
 */
abstract class BaseRepository {

    /**
     * Safely makes an API call and transforms the result into the desired type.
     *
     * This method executes an API call and handles its response by emitting the appropriate
     * [NetworkResponse] states. It wraps the API call into a [Flow] that emits loading, success,
     * empty, or error states, ensuring that the consuming code can react to each state accordingly.
     *
     * - If the response is successful, it transforms the data using the provided [transform]
     *   function and emits a [NetworkResponse.Success] with the transformed data.
     * - If the response is empty, it emits [NetworkResponse.Empty].
     * - If the response is loading, it emits [NetworkResponse.Loading].
     * - If there is an error, it emits a [NetworkResponse.Error] containing the error details.
     *
     * @param apiCall The suspend function that makes the API call and returns a [NetworkResponse].
     * @param transform A lambda function that transforms the data from type [T] to type [R].
     * @return A [Flow] emitting [NetworkResponse] that represents the result of the API call.
     */
    suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> NetworkResponse<T>,
        transform: (T) -> R
    ): Flow<NetworkResponse<R>> = flow {
        // Emit the Loading state initially before the API call is made
        emit(NetworkResponse.Loading)

        // Make the API call and handle the response
        val response = apiCall()

        // Handle the various response states
        response.onSuccess { data ->
            // If the response is successful, transform the data and emit Success
            emit(NetworkResponse.Success(transform(data)))
        }.onEmpty {
            // If the response is empty, emit Empty state
            emit(NetworkResponse.Empty)
        }.onLoading {
            // If the response is still loading, emit Loading state
            emit(NetworkResponse.Loading)
        }.onError {
            // If there is an error, emit the error state
            emit(it)
        }
    }
}