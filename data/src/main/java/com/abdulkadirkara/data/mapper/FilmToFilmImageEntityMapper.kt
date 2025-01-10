package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmImageEntity
import javax.inject.Inject

/**
 * A mapper class for converting a `Film` object to a `FilmImageEntity` object.
 *
 * This mapper extracts the image information from the remote data source (DTO)
 * for use in displaying film images within the application.
 *
 * @constructor Injected using Hilt for dependency injection.
 */
class FilmToFilmImageEntityMapper @Inject constructor() : Mapper<Film, FilmImageEntity> {

    /**
     * Maps a `Film` object to a `FilmImageEntity` object.
     *
     * @param input The `Film` object received from the remote data source.
     * @return A `FilmImageEntity` object containing image-related data.
     */
    override fun map(input: Film): FilmImageEntity {
        return FilmImageEntity(
            id = input.id,
            image = input.image
        )
    }
}
