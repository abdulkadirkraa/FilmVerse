package com.abdulkadirkara.data.base

import com.abdulkadirkara.common.networkResponse.NetworkResponse
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
        apiCall: suspend () -> T
    )
            : T {
        return withContext(ioDispatcher) { apiCall() }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResponse<T> {
        return try {
            val response = apiCall()
            if (response is List<*> && response.isEmpty() ||
                response is FilmResponse && response.movies.isEmpty()
            ) {
                NetworkResponse.Empty
            } else {
                NetworkResponse.Success(response)
            }
        } catch (e: HttpException) {
            NetworkResponse.Error.HttpError(e)
        } catch (e: IOException) {
            NetworkResponse.Error.NetworkError(e)
        } catch (e: Exception) {
            NetworkResponse.Error.UnknownError(e)
        }
    }
}