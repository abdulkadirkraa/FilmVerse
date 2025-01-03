package com.abdulkadirkara.domain.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI
import kotlinx.coroutines.flow.Flow

interface FilmVerseRepository {
    suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageUI>>>
    suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryUI>>>
    suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardUI>>>
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
        ) : Flow<NetworkResponse<CRUDResponseUI>>
    suspend fun getMovieCart() : Flow<NetworkResponse<List<FilmCardItem>>>
    suspend fun deleteMovie(cartId: Int) : Flow<NetworkResponse<CRUDResponseUI>>
}