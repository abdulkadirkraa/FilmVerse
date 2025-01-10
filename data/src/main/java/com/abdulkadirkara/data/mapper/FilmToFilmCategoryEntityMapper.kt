package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import javax.inject.Inject

/**
 * A mapper class for converting a `Film` object to a `FilmCategoryEntity` object.
 *
 * This mapper extracts the category information from the remote data source (DTO)
 * for categorizing films within the application.
 *
 * @constructor Injected using Hilt for dependency management.
 */
class FilmToFilmCategoryEntityMapper @Inject constructor() : Mapper<Film, FilmCategoryEntity> {

    /**
     * Maps a `Film` object to a `FilmCategoryEntity` object.
     *
     * @param input The `Film` object received from the remote data source.
     * @return A `FilmCategoryEntity` object containing the category data.
     */
    override fun map(input: Film): FilmCategoryEntity {
        return FilmCategoryEntity(
            category = input.category
        )
    }
}
