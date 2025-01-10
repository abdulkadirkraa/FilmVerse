package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import javax.inject.Inject

/**
 * Use case for inserting a film into the repository.
 *
 * @param filmVerseRepository The repository responsible for inserting the film.
 */
class InsertFilmUseCase @Inject constructor(
    private val filmVerseRepository: FilmVerseRepository
) {

    /**
     * Inserts a film into the repository.
     *
     * @param filmEntityModel The film entity to be inserted.
     */
    suspend operator fun invoke(filmEntityModel: FilmEntityModel) {
        filmVerseRepository.insertFilm(filmEntityModel)
    }
}