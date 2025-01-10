package com.abdulkadirkara.data.datasource.remotedatasource

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.data.base.BaseDataSource
import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse
import com.abdulkadirkara.data.remote.service.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Implementation of the [RemoteDataSource] interface for managing remote data operations.
 *
 * This class uses an [ApiService] to interact with the remote API and a [CoroutineDispatcher]
 * for executing IO-bound operations. It leverages a base data source class to handle
 * safe API calls.
 *
 * @property apiService The service for making API calls.
 * @property ioDispatcher The [CoroutineDispatcher] for performing IO operations.
 */
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : RemoteDataSource, BaseDataSource() {

    /**
     * Fetches all movies from the remote API.
     *
     * @return A [NetworkResponse] containing a [FilmResponse] on success or an error state.
     */
    override suspend fun getAllMovies(): NetworkResponse<FilmResponse> {
        return ioDispatcherCall(ioDispatcher) {
            safeApiCall { apiService.getAllMovies() }
        }
    }

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
    ): NetworkResponse<CRUDResponse> {
        return ioDispatcherCall(ioDispatcher) {
            safeApiCall {
                apiService.insertMovie(name, image, price, category, rating, year, director, description, orderAmount)
            }
        }
    }

    /**
     * Fetches the user's movie cart from the remote API.
     *
     * @return A [NetworkResponse] containing a [FilmCardResponse] on success or an error state.
     */
    override suspend fun getMovieCart(): NetworkResponse<FilmCardResponse> {
        return ioDispatcherCall(ioDispatcher) {
            safeApiCall { apiService.getMovieCart() }
        }
    }

    /**
     * Deletes a movie from the user's cart in the remote database.
     *
     * @param cartId The unique identifier of the cart item to be deleted.
     * @return A [NetworkResponse] containing a [CRUDResponse] on success or an error state.
     */
    override suspend fun deleteMovie(cartId: Int): NetworkResponse<CRUDResponse> {
        return ioDispatcherCall(ioDispatcher) {
            safeApiCall { apiService.deleteMovie(cartId) }
        }
    }
}
