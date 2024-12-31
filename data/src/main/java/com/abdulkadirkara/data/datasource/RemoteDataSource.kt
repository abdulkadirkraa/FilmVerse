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
        orderAmount: Int,
        userName: String,
    ) : NetworkResponse<CRUDResponse>
    suspend fun getMovieCart(userName: String) : NetworkResponse<FilmCardResponse>
    suspend fun deleteMovie(cartId: Int, userName: String) : NetworkResponse<CRUDResponse>

}