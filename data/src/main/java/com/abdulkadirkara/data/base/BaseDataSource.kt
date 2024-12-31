package com.abdulkadirkara.data.base

import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

abstract class BaseDataSource {
    suspend fun <T> ioDispatcherCall(
        @FilmVerseDispatchers(DispatcherType.Io) ioDispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T)
            : T {
        return withContext(ioDispatcher) { apiCall() }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): com.abdulkadirkara.common.networkResponse.NetworkResponse<T> {
        return try {
            val response = apiCall()
            if (response is List<*> && response.isEmpty() ||
                response is FilmResponse && response.movies.isEmpty()) {
                com.abdulkadirkara.common.networkResponse.NetworkResponse.Empty
            } else {
                com.abdulkadirkara.common.networkResponse.NetworkResponse.Success(response)
            }
        } catch (e: HttpException) {
            com.abdulkadirkara.common.networkResponse.NetworkResponse.Error.HttpError(e)
        } catch (e: IOException) {
            com.abdulkadirkara.common.networkResponse.NetworkResponse.Error.NetworkError(e)
        } catch (e: Exception) {
            com.abdulkadirkara.common.networkResponse.NetworkResponse.Error.UnknownError(e)
        }
    }
}