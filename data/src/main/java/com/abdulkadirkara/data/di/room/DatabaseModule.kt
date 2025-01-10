package com.abdulkadirkara.data.di.room

import android.content.Context
import androidx.room.Room
import com.abdulkadirkara.data.local.room.AppDatabase
import com.abdulkadirkara.data.local.room.FilmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the Room database.
     * The database is built using the application context and the [AppDatabase] class.
     *
     * @param context The application context.
     * @return A singleton [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, // Database class
            "film_database" // Database name
        ).build()
    }

    /**
     * Provides the DAO (Data Access Object) for films.
     * The [FilmDao] is used to interact with the film table in the Room database.
     *
     * @param database The [AppDatabase] instance.
     * @return An instance of [FilmDao] to access film-related data.
     */
    @Provides
    fun provideFilmDao(database: AppDatabase): FilmDao {
        return database.filmDao() // Retrieve the DAO for film operations
    }
}
