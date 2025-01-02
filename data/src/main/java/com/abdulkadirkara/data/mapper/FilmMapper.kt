package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCard
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.model.CartState
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI

object FilmMapper {

    fun Film.toFilmImageUI(): FilmImageUI {
        return FilmImageUI(
            id = id,
            image = image
        )
    }

    fun Film.toFilmCategoryUI(): FilmCategoryUI {
        return FilmCategoryUI(
            category = category
        )
    }

    fun Film.toFilmUI(): FilmCardUI {
        return FilmCardUI(
            id = id,
            image = image,
            name = name,
            category = category,
            rating = rating,
            price = price,
            isFavorite = false,
            campaign = null,
            year = year,
            description = description,
            director = director,
            cartState = CartState.NOT_IN_CART
        )
    }

    fun FilmCard.toFilmCardItem(): FilmCardItem {
        return FilmCardItem(
            cartId = cartId,
            name = name,
            image = image,
            price = price,
            category = category,
            rating = rating,
            year = year,
            director = director,
            description = description,
            orderAmount = orderAmount,
            userName = userName
        )
    }

    fun CRUDResponse.toCRUDResponseUI(): CRUDResponseUI {
        return CRUDResponseUI(
            success = success,
            message = message
        )
    }

}