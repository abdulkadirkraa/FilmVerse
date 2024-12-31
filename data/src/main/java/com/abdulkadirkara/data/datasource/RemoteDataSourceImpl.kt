package com.abdulkadirkara.data.datasource

import com.abdulkadirkara.data.base.BaseDataSource
import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse
import com.abdulkadirkara.data.remote.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource, BaseDataSource() {
    override suspend fun getAllMovies(): com.abdulkadirkara.common.networkResponse.NetworkResponse<FilmResponse> {
        return ioDispatcherCall(ioDispatcher) {
            safeApiCall { apiService.getAllMovies() }
        }
    }

    override suspend fun insertMovie(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int,
        userName: String
    ): com.abdulkadirkara.common.networkResponse.NetworkResponse<CRUDResponse> {
        return ioDispatcherCall(ioDispatcher){
            safeApiCall { apiService.insertMovie(
                name, image, price, category, rating, year, director, description, orderAmount, userName
            ) }
        }
    }

    override suspend fun getMovieCart(userName: String): com.abdulkadirkara.common.networkResponse.NetworkResponse<FilmCardResponse> {
        return ioDispatcherCall(ioDispatcher){
            safeApiCall { apiService.getMovieCart(userName) }
        }
    }

    override suspend fun deleteMovie(cartId: Int, userName: String): com.abdulkadirkara.common.networkResponse.NetworkResponse<CRUDResponse> {
        return ioDispatcherCall(ioDispatcher){
            safeApiCall { apiService.deleteMovie(cartId, userName) }
        }
    }
}