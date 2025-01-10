package com.abdulkadirkara.data.base

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * BaseDataSource is an abstract class that provides common methods for performing safe API calls
 * and handling various network operations, such as dispatching to the IO dispatcher, catching
 * exceptions, and returning appropriate [NetworkResponse] types.
 */
abstract class BaseDataSource {

    /**
     * Executes a given API call within a specified IO dispatcher context.
     *
     * This method is designed to perform network operations on a background thread using a
     * specified dispatcher. It ensures that the API call is made on the correct dispatcher.
     *
     * @param ioDispatcher The dispatcher to be used for the IO operation.
     * @param apiCall The suspend function that makes the API call.
     * @return The result of the API call (of type T).
     * @throws Exception If the API call fails or throws an exception.
     */
    suspend fun <T> ioDispatcherCall(
        @FilmVerseDispatchers(DispatcherType.Io) ioDispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): T {
        return withContext(ioDispatcher) { apiCall() }
    }

    /**
     * Safely makes an API call and handles potential errors.
     *
     * This method wraps an API call in a try-catch block to handle various exceptions, including
     * [HttpException], [IOException], and general exceptions. It returns a [NetworkResponse]
     * indicating success, failure, or empty results based on the outcome of the call.
     *
     * - If the response is an empty list or an empty FilmResponse, it returns [NetworkResponse.Empty].
     * - If the API call succeeds, it returns [NetworkResponse.Success] with the response data.
     * - If an exception occurs, it returns an appropriate [NetworkResponse.Error] instance.
     *
     * @param apiCall The suspend function that makes the API call.
     * @return A [NetworkResponse] representing the outcome of the API call.
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResponse<T> {
        return try {
            val response = apiCall()
            // Check if the response is empty and return an empty response if so
            if (response is List<*> && response.isEmpty() ||
                response is FilmResponse && response.movies.isEmpty()
            ) {
                NetworkResponse.Empty
            } else {
                NetworkResponse.Success(response)
            }
        } catch (e: HttpException) {
            // Handle HTTP errors and return an HttpError response
            NetworkResponse.Error.HttpError(e, e.message ?: "HTTP Error")
        } catch (e: IOException) {
            // Handle network-related errors and return a NetworkError response
            NetworkResponse.Error.NetworkError(e, e.message ?: "Network Error")
        } catch (e: Exception) {
            // Handle all other errors and return an UnknownError response
            NetworkResponse.Error.UnknownError(e, e.message ?: "Unknown Error")
        }
    }
}