package com.abdulkadirkara.data.di.mapper

import com.abdulkadirkara.data.local.room.FilmEntity
import com.abdulkadirkara.data.mapper.CRUDResponseToCRUDResponseEntityMapper
import com.abdulkadirkara.data.mapper.FilmCardToFilmCardItemMapper
import com.abdulkadirkara.data.mapper.FilmEntityModelToFilmEntity
import com.abdulkadirkara.data.mapper.FilmEntityToFilmEntityModel
import com.abdulkadirkara.data.mapper.FilmToFilmCardEntityMapper
import com.abdulkadirkara.data.mapper.FilmToFilmCategoryEntityMapper
import com.abdulkadirkara.data.mapper.FilmToFilmImageEntityMapper
import com.abdulkadirkara.data.remote.dto.allFilms.Film
import com.abdulkadirkara.data.remote.dto.crud.CRUDResponse
import com.abdulkadirkara.data.remote.dto.filmCard.FilmCard
import com.abdulkadirkara.domain.mapper.Mapper
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.model.FilmImageEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindCRUDResponseToCRUDResponseEntityMapper(mapper: CRUDResponseToCRUDResponseEntityMapper):
            Mapper<CRUDResponse, CRUDResponseEntity>

    @Binds
    abstract fun bindFilmCardToFilmCardItemMapper(mapper: FilmCardToFilmCardItemMapper):
            Mapper<FilmCard, FilmCardItem>

    @Binds
    abstract fun bindFilmToFilmCardEntityMapper(mapper: FilmToFilmCardEntityMapper):
            Mapper<Film, FilmCardEntity>

    @Binds
    abstract fun bindFilmToFilmCategoryEntityMapper(mapper: FilmToFilmCategoryEntityMapper):
            Mapper<Film, FilmCategoryEntity>

    @Binds
    abstract fun bindFilmToFilmImageEntityMapper(mapper: FilmToFilmImageEntityMapper):
            Mapper<Film, FilmImageEntity>

    @Binds
    abstract fun bindFilmEntityModelToFilmEntity(mapper: FilmEntityModelToFilmEntity):
            Mapper<FilmEntityModel, FilmEntity>

    @Binds
    abstract fun bindFilmEntityToFilmEntityModel(mapper: FilmEntityToFilmEntityModel):
            Mapper<FilmEntity, FilmEntityModel>

}