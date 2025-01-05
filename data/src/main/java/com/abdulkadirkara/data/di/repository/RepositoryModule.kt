package com.abdulkadirkara.data.di.repository

import com.abdulkadirkara.data.repository.CartRepositoryImpl
import com.abdulkadirkara.data.repository.FilmVerseRepositoryImpl
import com.abdulkadirkara.domain.repository.CartRepository
import com.abdulkadirkara.domain.repository.FilmVerseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFilmVerseRepository(repositoryImpl: FilmVerseRepositoryImpl) : FilmVerseRepository

    @Binds
    @ViewModelScoped
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository
}