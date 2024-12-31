package com.abdulkadirkara.data.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.base.BaseRepository
import com.abdulkadirkara.data.datasource.RemoteDataSource
import com.abdulkadirkara.domain.model.CRUDResponse
import com.abdulkadirkara.domain.model.FilmResponse
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmVerseRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : FilmVerseRepository, BaseRepository() {
    override suspend fun getAllMovies(): Flow<NetworkResponse<FilmResponse>> {
        TODO("Not yet implemented")
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
    ): Flow<NetworkResponse<CRUDResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieCart(userName: String): Flow<NetworkResponse<FilmResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMovie(
        cartId: Int,
        userName: String
    ): Flow<NetworkResponse<CRUDResponse>> {
        TODO("Not yet implemented")
    }
}