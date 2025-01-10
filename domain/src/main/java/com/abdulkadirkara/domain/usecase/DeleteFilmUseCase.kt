package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import javax.inject.Inject

/**
 * Use case for deleting a film from the repository.
 *
 * @param filmVerseRepository The repository used for deleting a film in the room database.
 */
class DeleteFilmUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Deletes a given film.
     *
     * @param film The [FilmEntityModel] to be deleted.
     */
    suspend operator fun invoke(film: FilmEntityModel) {
        filmVerseRepository.deleteFilm(film)
    }
}