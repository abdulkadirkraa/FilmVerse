package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import javax.inject.Inject

class InsertFilmUseCase @Inject constructor(private val filmVerseRepository: FilmVerseRepository) {
    suspend operator fun invoke(filmEntityModel: FilmEntityModel){
        filmVerseRepository.insertFilm(filmEntityModel)
    }
}