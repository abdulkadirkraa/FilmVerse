package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for deleting a movie from the repository in API.
 *
 * @param filmVerseRepository The repository responsible for movie operations.
 */
class DeleteMovieUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Deletes a movie based on the given cart ID.
     *
     * @param cartId The ID of the movie to be deleted.
     * @return A [Flow] emitting a [NetworkResponse] with the result of the deletion.
     */
    suspend operator fun invoke(cartId: Int): Flow<NetworkResponse<CRUDResponseEntity>> =
        filmVerseRepository.deleteMovie(cartId)
}