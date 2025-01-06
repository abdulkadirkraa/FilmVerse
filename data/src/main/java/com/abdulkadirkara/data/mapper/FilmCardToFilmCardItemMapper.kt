package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.filmCard.FilmCard
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmCardItem
import javax.inject.Inject

class FilmCardToFilmCardItemMapper @Inject constructor() : Mapper<FilmCard, FilmCardItem> {
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