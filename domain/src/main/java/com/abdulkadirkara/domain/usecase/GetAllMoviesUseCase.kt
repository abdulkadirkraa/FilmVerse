package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all movies from the repository.
 *
 * @param filmVerseRepository The repository responsible for fetching the movies.
 */
class GetAllMoviesUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Retrieves a list of all available movies.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a list of [FilmCardEntity] objects.
     */
    suspend operator fun invoke(): Flow<NetworkResponse<List<FilmCardEntity>>> =
        filmVerseRepository.getAllMovies()
}