package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all films from the repository in room database.
 *
 * @param filmVerseRepository The repository responsible for fetching the films.
 */
class GetAllFilmsUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Retrieves a list of all available films.
     *
     * @return A [Flow] emitting a list of [FilmEntityModel] objects.
     */
    operator fun invoke(): Flow<List<FilmEntityModel>> =
        filmVerseRepository.getAllFilms()
}