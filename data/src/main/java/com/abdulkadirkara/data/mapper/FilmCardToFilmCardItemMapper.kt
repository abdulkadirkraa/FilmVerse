package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.filmCard.FilmCard
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmCardItem
import javax.inject.Inject

/**
 * A mapper class for converting a `FilmCard` object to a `FilmCardItem` object.
 *
 * This mapper transforms data from the remote data source (DTO) to a domain model
 * for use in the presentation layer (e.g., UI).
 *
 * @constructor Injected using Hilt to streamline dependency injection.
 */
class FilmCardToFilmCardItemMapper @Inject constructor() : Mapper<FilmCard, FilmCardItem> {

    /**
     * Maps a `FilmCard` object to a `FilmCardItem` object.
     *
     * @param input The `FilmCard` object containing raw data from the remote source.
     * @return A `FilmCardItem` object formatted for use in the application's UI.
     */
    override fun map(input: FilmCard): FilmCardItem {
        return FilmCardItem(
            cartId = input.cartId,
            name = input.name,
            image = input.image,
            price = input.price,
            category = input.category,
            rating = input.rating,
            year = input.year,
            director = input.director,
            description = input.description,
            orderAmount = input.orderAmount,
            userName = input.userName
        )
    }
}
