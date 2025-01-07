package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFilmsUseCase @Inject constructor(private val filmVerseRepository: FilmVerseRepository) {
    operator fun invoke(): Flow<List<FilmEntityModel>> = filmVerseRepository.getAllFilms()
}