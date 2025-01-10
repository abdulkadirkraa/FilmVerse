package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.CartState
import com.abdulkadirkara.domain.model.FilmCardEntity
import javax.inject.Inject

/**
 * A mapper class for converting a `Film` object to a `FilmCardEntity` object.
 *
 * This mapper transforms remote data (DTO) into a domain model for use in features like
 * displaying a list of films in a cart or catalog.
 *
 * @constructor Injected using Hilt for dependency injection.
 */
class FilmToFilmCardEntityMapper @Inject constructor() : Mapper<Film, FilmCardEntity> {

    /**
     * Maps a `Film` object to a `FilmCardEntity` object.
     *
     * @param input The `Film` object received from the remote data source.
     * @return A `FilmCardEntity` object ready for use in the domain layer.
     */
    override fun map(input: Film): FilmCardEntity {
        return FilmCardEntity(
            id = input.id,
            image = input.image,
            name = input.name,
            category = input.category,
            rating = input.rating,
            price = input.price,
            isFavorite = false, // Default value for new films.
            campaign = null,    // Default value for campaign information.
            year = input.year,
            description = input.description,
            director = input.director,
            cartState = CartState.NOT_IN_CART // Default cart state.
        )
    }
}
