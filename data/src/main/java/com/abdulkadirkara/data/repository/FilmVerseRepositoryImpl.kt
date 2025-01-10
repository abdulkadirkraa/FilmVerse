package com.abdulkadirkara.data.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.base.BaseRepository
import com.abdulkadirkara.data.datasource.localdatasource.LocalDataSource
import com.abdulkadirkara.data.datasource.remotedatasource.RemoteDataSource
import com.abdulkadirkara.data.local.room.FilmEntity
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
import com.abdulkadirkara.domain.model.FilmEntityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the [FilmVerseRepository] interface.
 *
 * This class handles interactions with both remote and local data sources,
 * providing methods for managing film data. It uses mappers to transform
 * data between different layers of the application.
 *
 * @property remoteDataSource The remote data source for fetching film data.
 * @property localDataSource The local data source for managing film data in the local database.
 * @property crudResponseToCrudResponseEntityMapper Mapper for transforming [CRUDResponse] to [CRUDResponseEntity].
 * @property filmCardToFilmCardItemMapper Mapper for transforming [FilmCard] to [FilmCardItem].
 * @property filmToFilmCardEntityMapper Mapper for transforming [Film] to [FilmCardEntity].
 * @property filmToFilmCategoryEntityMapper Mapper for transforming [Film] to [FilmCategoryEntity].
 * @property filmToFilmImageEntityMapper Mapper for transforming [Film] to [FilmImageEntity].
 * @property filmEntityModelToFilmEntity Mapper for transforming [FilmEntityModel] to [FilmEntity].
 * @property filmEntityToFilmEntityModel Mapper for transforming [FilmEntity] to [FilmEntityModel].
 */
class FilmVerseRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val crudResponseToCrudResponseEntityMapper: Mapper<CRUDResponse, CRUDResponseEntity>,
    private val filmCardToFilmCardItemMapper: Mapper<FilmCard, FilmCardItem>,
    private val filmToFilmCardEntityMapper: Mapper<Film, FilmCardEntity>,
    private val filmToFilmCategoryEntityMapper: Mapper<Film, FilmCategoryEntity>,
    private val filmToFilmImageEntityMapper: Mapper<Film, FilmImageEntity>,
    private val filmEntityModelToFilmEntity: Mapper<FilmEntityModel, FilmEntity>,
    private val filmEntityToFilmEntityModel: Mapper<FilmEntity, FilmEntityModel>
) : FilmVerseRepository, BaseRepository() {

    /**
     * @see FilmVerseRepository.getAllImages
     */
    override suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { film -> filmToFilmImageEntityMapper.map(film) } }
        )
    }

    /**
     * @see FilmVerseRepository.getAllCategories
     */
    override suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response ->
                response.movies.map { filmToFilmCategoryEntityMapper.map(it) }
                    .distinctBy { it.category }
            }
        )
    }

    /**
     * @see FilmVerseRepository.getAllMovies
     */
    override suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardEntity>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getAllMovies() },
            transform = { response -> response.movies.map { film -> filmToFilmCardEntityMapper.map(film) } }
        )
    }

    /**
     * @see FilmVerseRepository.insertMovie
     */
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

    /**
     * @see FilmVerseRepository.getMovieCart
     */
    override suspend fun getMovieCart(): Flow<NetworkResponse<List<FilmCardItem>>> {
        return safeApiCall(
            apiCall = { remoteDataSource.getMovieCart() },
            transform = { response -> response.filmCards.map { filmCard -> filmCardToFilmCardItemMapper.map(filmCard) } }
        )
    }

    /**
     * @see FilmVerseRepository.deleteMovie
     */
    override suspend fun deleteMovie(cartId: Int): Flow<NetworkResponse<CRUDResponseEntity>> {
        return safeApiCall(
            apiCall = { remoteDataSource.deleteMovie(cartId) },
            transform = { response -> crudResponseToCrudResponseEntityMapper.map(response) }
        )
    }

    /**
     * @see FilmVerseRepository.insertFilm
     */
    override suspend fun insertFilm(film: FilmEntityModel) {
        localDataSource.insertFilm(filmEntityModelToFilmEntity.map(film))
    }

    /**
     * @see FilmVerseRepository.deleteFilm
     */
    override suspend fun deleteFilm(film: FilmEntityModel) {
        localDataSource.deleteFilm(filmEntityModelToFilmEntity.map(film))
    }

    /**
     * @see FilmVerseRepository.getAllFilms
     */
    override fun getAllFilms(): Flow<List<FilmEntityModel>> {
        return localDataSource.getAllFilms().map {
            it.map { filmEntity -> filmEntityToFilmEntityModel.map(filmEntity) }
        }
    }
}
