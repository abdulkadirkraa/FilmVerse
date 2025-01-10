package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the movie cart items from the repository.
 *
 * @param filmVerseRepository The repository responsible for fetching the movie cart items.
 */
class GetMovieCartUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Retrieves the items currently in the movie cart.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCardItem] objects.
     */
    suspend operator fun invoke(): Flow<NetworkResponse<List<FilmCardItem>>> =
        filmVerseRepository.getMovieCart()
}