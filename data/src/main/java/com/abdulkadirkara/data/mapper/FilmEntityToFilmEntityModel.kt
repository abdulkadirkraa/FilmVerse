package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.local.room.FilmEntity
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmEntityModel
import javax.inject.Inject

class FilmEntityToFilmEntityModel @Inject constructor() : Mapper<FilmEntity, FilmEntityModel> {
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