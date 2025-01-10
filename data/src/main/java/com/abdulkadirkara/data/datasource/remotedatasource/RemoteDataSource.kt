package com.abdulkadirkara.data.datasource.remotedatasource

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse

/**
 * Interface defining the contract for remote data operations.
 *
 * This interface provides methods for interacting with the remote API to fetch,
 * insert, and delete movie data.
 */
interface RemoteDataSource {

    /**
     * Fetches all movies from the remote API.
     *
     * @return A [NetworkResponse] containing a [FilmResponse] on success or an error state.
     */
    suspend fun getAllMovies(): NetworkResponse<FilmResponse>

    /**
     * Inserts a new movie into the remote database.
     *
     * @param name The name of the movie.
     * @param image The URL of the movie's image.
     * @param price The price of the movie.
     * @param category The category of the movie.
     * @param rating The rating of the movie.
     * @param year The release year of the movie.
     * @param director The director of the movie.
     * @param description A brief description of the movie.
     * @param orderAmount The number of copies ordered.
     * @return A [NetworkResponse] containing a [CRUDResponse] on success or an error state.
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
    ): NetworkResponse<CRUDResponse>

    /**
     * Fetches the user's movie cart from the remote API.
     *
     * @return A [NetworkResponse] containing a [FilmCardResponse] on success or an error state.
     */
    suspend fun getMovieCart(): NetworkResponse<FilmCardResponse>

    /**
     * Deletes a movie from the user's cart in the remote database.
     *
     * @param cartId The unique identifier of the cart item to be deleted.
     * @return A [NetworkResponse] containing a [CRUDResponse] on success or an error state.
     */
    suspend fun deleteMovie(cartId: Int): NetworkResponse<CRUDResponse>
}
