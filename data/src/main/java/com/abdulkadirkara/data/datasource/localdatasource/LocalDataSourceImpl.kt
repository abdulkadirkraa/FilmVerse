package com.abdulkadirkara.data.datasource.localdatasource

import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.local.room.FilmDao
import com.abdulkadirkara.data.local.room.FilmEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val filmDao: FilmDao,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : LocalDataSource {

    override suspend fun insertFilm(film: FilmEntity) {
        withContext(ioDispatcher) {
            filmDao.insertFilm(film)
        }
    }

    override suspend fun deleteFilm(film: FilmEntity) {
        withContext(ioDispatcher) {
            filmDao.deleteFilm(film)
        }
    }

    override fun getAllFilms(): Flow<List<FilmEntity>> {
        return filmDao.getAllFilms()
    }
}
