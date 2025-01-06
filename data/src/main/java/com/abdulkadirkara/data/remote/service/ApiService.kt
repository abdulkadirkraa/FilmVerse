package com.abdulkadirkara.data.remote.service

import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.data.remote.dto.allFilms.FilmResponse
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCardResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET(ApiConstants.GET_ALL_MOVIES_END_POINT)
    suspend fun getAllMovies(): FilmResponse

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
    ) : CRUDResponse

    @POST(ApiConstants.GET_MOVIE_CART_END_POINT)
    @FormUrlEncoded
    suspend fun getMovieCart(@Field("userName") userName: String = ApiConstants.USER_NAME ) : FilmCardResponse

    @POST(ApiConstants.DELETE_MOVIE_END_POINT)
    @FormUrlEncoded
    suspend fun deleteMovie(
        @Field("cartId") cartId: Int,
        @Field("userName") userName: String
    ) : CRUDResponse
}