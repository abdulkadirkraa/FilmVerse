package com.abdulkadirkara.data.remote.service

import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Service interface for defining API endpoints using Retrofit.
 *
 * This interface provides methods for performing network operations related to movies,
 * such as fetching all movies, inserting a new movie, retrieving the movie cart, and deleting a movie.
 */
interface ApiService {

    /**
     * Fetches all movies from the server.
     *
     * @return A [FilmResponse] containing the list of all movies.
     */
    @GET(ApiConstants.GET_ALL_MOVIES_END_POINT)
    suspend fun getAllMovies(): FilmResponse

    /**
     * Inserts a new movie into the database on the server.
     *
     * @param name The name of the movie.
     * @param image The image URL of the movie.
     * @param price The price of the movie.
     * @param category The category of the movie.
     * @param rating The rating of the movie.
     * @param year The release year of the movie.
     * @param director The director of the movie.
     * @param description A description of the movie.
     * @param orderAmount The order amount for the movie.
     * @param userName The username of the person adding the movie. Defaults to [ApiConstants.USER_NAME].
     * @return A [CRUDResponse] indicating the success or failure of the operation.
     */
    @POST(ApiConstants.INSERT_MOVIE_END_POINT)
    @FormUrlEncoded
    suspend fun insertMovie(
        @Field("name") name: String,
        @Field("image") image: String,
        @Field("price") price: Int,
        @Field("category") category: String,
        @Field("rating") rating: Double,
        @Field("year") year: Int,
        @Field("director") director: String,
        @Field("description") description: String,
        @Field("orderAmount") orderAmount: Int,
        @Field("userName") userName: String = ApiConstants.USER_NAME
    ): CRUDResponse

    /**
     * Fetches the user's movie cart from the server.
     *
     * @param userName The username for whom the movie cart is being retrieved. Defaults to [ApiConstants.USER_NAME].
     * @return A [FilmCardResponse] containing the movie cart data.
     */
    @POST(ApiConstants.GET_MOVIE_CART_END_POINT)
    @FormUrlEncoded
    suspend fun getMovieCart(@Field("userName") userName: String = ApiConstants.USER_NAME): FilmCardResponse

    /**
     * Deletes a movie from the user's cart on the server.
     *
     * @param cartId The ID of the cart item to be deleted.
     * @param userName The username of the person whose cart item is being deleted. Defaults to [ApiConstants.USER_NAME].
     * @return A [CRUDResponse] indicating the success or failure of the operation.
     */
    @POST(ApiConstants.DELETE_MOVIE_END_POINT)
    @FormUrlEncoded
    suspend fun deleteMovie(
        @Field("cartId") cartId: Int,
        @Field("userName") userName: String = ApiConstants.USER_NAME
    ): CRUDResponse
}
