package com.abdulkadirkara.domain.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.model.FilmImageEntity
import kotlinx.coroutines.flow.Flow

interface FilmVerseRepository {
    suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageEntity>>>
    suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryEntity>>>
    suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardEntity>>>
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
    ) : Flow<NetworkResponse<CRUDResponseEntity>>
    suspend fun getMovieCart() : Flow<NetworkResponse<List<FilmCardItem>>>
    suspend fun deleteMovie(cartId: Int) : Flow<NetworkResponse<CRUDResponseEntity>>

    suspend fun insertFilm(film: FilmEntityModel)
    suspend fun deleteFilm(film: FilmEntityModel)
    fun getAllFilms(): Flow<List<FilmEntityModel>>
}