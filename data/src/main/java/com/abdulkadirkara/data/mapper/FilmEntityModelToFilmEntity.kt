package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.local.room.FilmEntity
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmEntityModel
import javax.inject.Inject

/**
 * A mapper class for converting a `FilmEntityModel` object to a `FilmEntity` object.
 *
 * This class is typically used to transform domain models into entities suitable
 * for local data storage in Room.
 *
 * @constructor Injected using Hilt for dependency injection.
 */
class FilmEntityModelToFilmEntity @Inject constructor() : Mapper<FilmEntityModel, FilmEntity> {

    /**
     * Maps a `FilmEntityModel` object to a `FilmEntity` object.
     *
     * @param input The `FilmEntityModel` object from the domain layer.
     * @return A `FilmEntity` object formatted for local data storage.
     */
    override fun map(input: FilmEntityModel): FilmEntity {
        return FilmEntity(
            id = 0, // ID is auto-generated in the database.
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
