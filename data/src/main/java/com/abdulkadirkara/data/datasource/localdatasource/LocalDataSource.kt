package com.abdulkadirkara.data.datasource.localdatasource

import com.abdulkadirkara.data.local.room.FilmEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for local data operations.
 *
 * This interface provides methods for inserting, deleting, and retrieving films
 * from the local database.
 */
interface LocalDataSource {

    /**
     * Inserts a film into the local database.
     *
     * @param film The [FilmEntity] object to be inserted.
     */
    suspend fun insertFilm(film: FilmEntity)

    /**
     * Deletes a film from the local database.
     *
     * @param film The [FilmEntity] object to be deleted.
     */
    suspend fun deleteFilm(film: FilmEntity)

    /**
     * Retrieves all films from the local database as a stream of data.
     *
     * @return A [Flow] emitting a list of [FilmEntity] objects.
     */
    fun getAllFilms(): Flow<List<FilmEntity>>
}
