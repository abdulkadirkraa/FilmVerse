package com.abdulkadirkara.data.mapper

import com.abdulkadirkara.data.local.room.FilmEntity
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.FilmEntityModel
import javax.inject.Inject

class FilmEntityModelToFilmEntity @Inject constructor() : Mapper<FilmEntityModel, FilmEntity> {
    override fun map(input: FilmEntityModel): FilmEntity {
        return FilmEntity(
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