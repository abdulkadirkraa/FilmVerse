package com.abdulkadirkara.data.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.base.BaseRepository
import com.abdulkadirkara.data.datasource.RemoteDataSource
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmImageEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCard
import com.abdulkadirkara.domain.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilmVerseRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val crudResponseToCrudResponseEntityMapper : Mapper<CRUDResponse, CRUDResponseEntity>,
    private val filmCardToFilmCardItemMapper: Mapper<FilmCard, FilmCardItem>,
    private val filmToFilmCardEntityMapper: Mapper<Film, FilmCardEntity>,
    private val filmToFilmCategoryEntityMapper: Mapper<Film, FilmCategoryEntity>,
    private val filmToFilmImageEntityMapper: Mapper<Film, FilmImageEntity>
) : FilmVerseRepository, BaseRepository(){
    override suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { film -> filmToFilmImageEntityMapper.map(film) } }
        )
    }

    override suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { film -> filmToFilmCategoryEntityMapper.map(film) } }
        )
    }

    override suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { film -> filmToFilmCardEntityMapper.map(film) } }
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
        orderAmount: Int
    ): Flow<NetworkResponse<CRUDResponseEntity>> {
        return safeApiCall(
            apiCall = {
                remoteDataSource.insertMovie(
                    name, image, price, category, rating, year, director, description, orderAmount
                )
            },
            transform = { response -> crudResponseToCrudResponseEntityMapper.map(response) }
        )
    }

    override suspend fun getMovieCart(userName: String): Flow<NetworkResponse<List<FilmCardItem>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getMovieCart(userName) },
            transform = { response -> response.filmCards.map { filmCard -> filmCardToFilmCardItemMapper.map(filmCard) } }
        )
    }

    override suspend fun deleteMovie(
        cartId: Int,
        userName: String
    ): Flow<NetworkResponse<CRUDResponseEntity>> {
        return safeApiCall(
            apiCall = { remoteDataSource.deleteMovie(cartId, userName) },
            transform = { response -> crudResponseToCrudResponseEntityMapper.map(response) }
        )
    }
}