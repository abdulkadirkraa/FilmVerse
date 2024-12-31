package com.abdulkadirkara.domain.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponse
import com.abdulkadirkara.domain.model.FilmResponse
import kotlinx.coroutines.flow.Flow

interface FilmVerseRepository {
    suspend fun getAllMovies(): Flow<NetworkResponse<FilmResponse>>
    suspend fun insertMovie(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int,
        userName: String,
    ) : Flow<NetworkResponse<CRUDResponse>>
    suspend fun getMovieCart(userName: String) : Flow<NetworkResponse<FilmResponse>>
    suspend fun deleteMovie(cartId: Int, userName: String) : Flow<NetworkResponse<CRUDResponse>>
}