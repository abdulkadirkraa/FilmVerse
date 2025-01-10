package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.local.room.FilmEntity
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmEntityModel
import javax.inject.Inject

/**
 * A mapper class for converting a `FilmEntity` object to a `FilmEntityModel` object.
 *
 * This mapper is used to transform data from the local database layer (Room entities)
 * into domain models for use in business logic or UI.
 *
 * @constructor Injected using Hilt for dependency management.
 */
class FilmEntityToFilmEntityModel @Inject constructor() : Mapper<FilmEntity, FilmEntityModel> {

    /**
     * Maps a `FilmEntity` object to a `FilmEntityModel` object.
     *
     * @param input The `FilmEntity` object retrieved from the local database.
     * @return A `FilmEntityModel` object ready for use in the domain layer.
     */
    override fun map(input: FilmEntity): FilmEntityModel {
        return FilmEntityModel(
            id = input.id,
            category = input.category,
            description = input.description,
            director = input.director,
            imagePath = input.imagePath,
            name = input.name,
            price = input.price,
            rating = input.rating,
            year = input.year
        )
    }
}
