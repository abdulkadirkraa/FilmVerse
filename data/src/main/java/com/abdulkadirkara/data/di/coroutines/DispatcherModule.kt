package com.abdulkadirkara.data.di.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object DispatcherModule {
    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Main)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Io)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Unconfined)
    fun provideUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}