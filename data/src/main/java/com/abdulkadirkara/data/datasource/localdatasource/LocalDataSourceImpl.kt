package com.abdulkadirkara.data.datasource.localdatasource

import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.local.room.FilmDao
import com.abdulkadirkara.data.local.room.FilmEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [LocalDataSource] interface for managing local data operations.
 *
 * This class uses a [FilmDao] to interact with the Room database and leverages
 * coroutines for asynchronous data handling.
 *
 * @property filmDao The DAO for accessing film-related database operations.
 * @property ioDispatcher The [CoroutineDispatcher] for performing IO operations.
 */
class LocalDataSourceImpl @Inject constructor(
    private val filmDao: FilmDao,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    /**
     * Inserts a film into the local database.
     *
     * This operation is executed on the [ioDispatcher].
     *
     * @param film The [FilmEntity] object to be inserted.
     */
    override suspend fun insertFilm(film: FilmEntity) {
        withContext(ioDispatcher) {
            filmDao.insertFilm(film)
        }
    }

    /**
     * Deletes a film from the local database.
     *
     * This operation is executed on the [ioDispatcher].
     *
     * @param film The [FilmEntity] object to be deleted.
     */
    override suspend fun deleteFilm(film: FilmEntity) {
        withContext(ioDispatcher) {
            filmDao.deleteFilm(film)
        }
    }

    /**
     * Retrieves all films from the local database as a stream of data.
     *
     * This method directly returns the [Flow] provided by the [FilmDao].
     *
     * @return A [Flow] emitting a list of [FilmEntity] objects.
     */
    override fun getAllFilms(): Flow<List<FilmEntity>> {
        return filmDao.getAllFilms()
    }
}
