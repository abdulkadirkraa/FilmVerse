package com.abdulkadirkara.data.datasource

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse

interface RemoteDataSource {
    suspend fun getAllMovies(): NetworkResponse<FilmResponse>
    suspend fun insertMovie(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int
    ): NetworkResponse<CRUDResponse>

    suspend fun getMovieCart(): NetworkResponse<FilmCardResponse>
    suspend fun deleteMovie(cartId: Int): NetworkResponse<CRUDResponse>

}