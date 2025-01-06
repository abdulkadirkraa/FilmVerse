package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import javax.inject.Inject

class FilmToFilmCategoryEntityMapper @Inject constructor() : Mapper<Film, FilmCategoryEntity> {
    override fun map(input: Film): FilmCategoryEntity {
        return FilmCategoryEntity(
            category = input.category
        )
    }
}