package com.abdulkadirkara.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    /**
     * Inserts a [FilmEntity] into the database.
     * If a film with the same primary key exists, it will be replaced.
     *
     * @param film The [FilmEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FilmEntity)

    /**
     * Deletes a [FilmEntity] from the database.
     *
     * @param film The [FilmEntity] to delete.
     */
    @Delete
    suspend fun deleteFilm(film: FilmEntity)

    /**
     * Retrieves all films from the database.
     * This function returns a [Flow] of the list of [FilmEntity]s,
     * allowing for asynchronous updates when data changes.
     *
     * @return A [Flow] of the list of all films.
     */
    @Query("SELECT * FROM film_table")
    fun getAllFilms(): Flow<List<FilmEntity>>
}
