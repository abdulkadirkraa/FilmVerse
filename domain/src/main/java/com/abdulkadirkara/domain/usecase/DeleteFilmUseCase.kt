package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import javax.inject.Inject

class DeleteFilmUseCase @Inject constructor(private val filmVerseRepository: FilmVerseRepository) {
    suspend operator fun invoke(film: FilmEntityModel){
        filmVerseRepository.deleteFilm(film)
    }
}