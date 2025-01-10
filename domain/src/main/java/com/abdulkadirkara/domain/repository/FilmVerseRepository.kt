package com.abdulkadirkara.domain.repository

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.model.FilmImageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing film-related data and operations.
 *
 * Provides methods for retrieving images, categories, movies, and managing
 * the movie cart, as well as inserting and deleting films.
 */
interface FilmVerseRepository {

    /**
     * Retrieves all film images as a reactive data stream.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmImageEntity].
     */
    suspend fun getAllImages(): Flow<NetworkResponse<List<FilmImageEntity>>>

    /**
     * Retrieves all film categories as a reactive data stream.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCategoryEntity].
     */
    suspend fun getAllCategories(): Flow<NetworkResponse<List<FilmCategoryEntity>>>

    /**
     * Retrieves all movies as a reactive data stream.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCardEntity].
     */
    suspend fun getAllMovies(): Flow<NetworkResponse<List<FilmCardEntity>>>

    /**
     * Inserts a new movie with the specified details.
     *
     * @param name The name of the movie.
     * @param image The URL of the movie's image.
     * @param price The price of the movie.
     * @param category The category of the movie.
     * @param rating The rating of the movie.
     * @param year The release year of the movie.
     * @param director The director of the movie.
     * @param description A brief description of the movie.
     * @param orderAmount The order amount for the movie.
     * @return A [Flow] emitting a [NetworkResponse] containing a [CRUDResponseEntity].
     */
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
    ): Flow<NetworkResponse<CRUDResponseEntity>>

    /**
     * Retrieves all items in the movie cart as a reactive data stream.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCardItem].
     */
    suspend fun getMovieCart(): Flow<NetworkResponse<List<FilmCardItem>>>

    /**
     * Deletes a movie from the cart by its ID.
     *
     * @param cartId The ID of the movie to be deleted from the cart.
     * @return A [Flow] emitting a [NetworkResponse] containing a [CRUDResponseEntity].
     */
    suspend fun deleteMovie(cartId: Int): Flow<NetworkResponse<CRUDResponseEntity>>

    /**
     * Inserts a film entity into the local database.
     *
     * @param film The [FilmEntityModel] to be inserted.
     */
    suspend fun insertFilm(film: FilmEntityModel)

    /**
     * Deletes a film entity from the local database.
     *
     * @param film The [FilmEntityModel] to be deleted.
     */
    suspend fun deleteFilm(film: FilmEntityModel)

    /**
     * Retrieves all films from the local database as a reactive data stream.
     *
     * @return A [Flow] emitting a list of [FilmEntityModel].
     */
    fun getAllFilms(): Flow<List<FilmEntityModel>>
}
