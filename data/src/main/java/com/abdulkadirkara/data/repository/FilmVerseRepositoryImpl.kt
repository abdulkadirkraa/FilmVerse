package com.abdulkadirkara.data.repository

import android.util.Log
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.base.BaseRepository
import com.abdulkadirkara.data.datasource.RemoteDataSource
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import com.abdulkadirkara.data.mapper.FilmMapper.toCRUDResponseUI
import com.abdulkadirkara.data.mapper.FilmMapper.toFilmCardItem
import com.abdulkadirkara.data.mapper.FilmMapper.toFilmCategoryUI
import com.abdulkadirkara.data.mapper.FilmMapper.toFilmImageUI
import com.abdulkadirkara.data.mapper.FilmMapper.toFilmUI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmVerseRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : FilmVerseRepository, BaseRepository(){
    override suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageUI>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { it.toFilmImageUI() } }
        )
    }

    override suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryUI>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response ->
                response.movies.map { it.toFilmCategoryUI() }
                    .distinctBy { it.category }
            }
        )
    }

    override suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardUI>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { it.toFilmUI() }}
        )
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
    ): Flow<NetworkResponse<CRUDResponseUI>> {
        return safeApiCall(
            apiCall = {
                remoteDataSource.insertMovie(name, image, price, category, rating, year, director, description, orderAmount, userName
                ) },
            transform = { response -> response.toCRUDResponseUI() }
        )
    }

    override suspend fun getMovieCart(userName: String): Flow<NetworkResponse<List<FilmCardItem>>> {
        return safeApiCall(
            apiCall = {
                Log.e("FilmVerseRepositoryImpl", "getMovieCart: $userName")
                remoteDataSource.getMovieCart(userName)
            },
            transform = { response -> response.filmCards.map { it.toFilmCardItem() } }
        )
    }

    override suspend fun deleteMovie(
        cartId: Int,
        userName: String
    ): Flow<NetworkResponse<CRUDResponseUI>> {
        return safeApiCall(
            apiCall = { remoteDataSource.deleteMovie(cartId, userName) },
            transform = { response -> response.toCRUDResponseUI() }
        )
    }
}