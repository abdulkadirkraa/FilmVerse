package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.CartState
import com.abdulkadirkara.domain.model.FilmCardEntity
import javax.inject.Inject

class FilmToFilmCardEntityMapper @Inject constructor() : Mapper<Film, FilmCardEntity> {
    override fun map(input: Film): FilmCardEntity {
        return FilmCardEntity(
            id = input.id,
            image = input.image,
            name = input.name,
            category = input.category,
            rating = input.rating,
            price = input.price,
            isFavorite = false,
            campaign = null,
            year = input.year,
            description = input.description,
            director = input.director,
            cartState = CartState.NOT_IN_CART
        )
    }
}