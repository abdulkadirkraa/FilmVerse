package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmImageEntity
import javax.inject.Inject

class FilmToFilmImageEntityMapper @Inject constructor() : Mapper<Film, FilmImageEntity> {
    override fun map(input: Film): FilmImageEntity {
        return FilmImageEntity(
            id = input.id,
            image = input.image
        )
    }
}