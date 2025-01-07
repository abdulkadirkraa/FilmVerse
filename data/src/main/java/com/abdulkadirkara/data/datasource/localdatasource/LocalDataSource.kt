package com.abdulkadirkara.data.datasource.localdatasource

import com.abdulkadirkara.data.local.room.FilmEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertFilm(film: FilmEntity)
    suspend fun deleteFilm(film: FilmEntity)
    fun getAllFilms(): Flow<List<FilmEntity>>
}