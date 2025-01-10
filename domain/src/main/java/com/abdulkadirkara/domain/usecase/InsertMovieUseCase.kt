package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for inserting a movie into the repository.
 *
 * @param filmVerseRepository The repository responsible for inserting the movie.
 */
class InsertMovieUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Inserts a movie into the repository with the provided details.
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
     *
     * @return A [Flow] emitting a [NetworkResponse] containing the result of the insert operation.
     */
    suspend operator fun invoke(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int
    ): Flow<NetworkResponse<CRUDResponseEntity>> =
        filmVerseRepository.insertMovie(
            name, image, price, category, rating, year, director, description, orderAmount
        )
}