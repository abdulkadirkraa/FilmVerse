package com.abdulkadirkara.data.di.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * This module provides different CoroutineDispatchers for use in the ViewModel layer.
 *
 * It uses Dagger Hilt to inject specific dispatchers depending on the required type of
 * operation (Main, IO, Default, Unconfined).
 *
 * The dispatchers are scoped to the ViewModel lifecycle, meaning they will be available
 * during the lifespan of a ViewModel.
 */
@Module
@InstallIn(ViewModelComponent::class)
object DispatcherModule {

    /**
     * Provides the main dispatcher for UI-related tasks.
     * This dispatcher should be used for tasks that need to update the UI on the main thread.
     *
     * @return The [CoroutineDispatcher] for the main thread.
     */
    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Main)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    /**
     * Provides the IO dispatcher for tasks related to network or disk I/O.
     * This dispatcher is optimized for off-the-main-thread work, like fetching data from a remote API or reading/writing to disk.
     *
     * @return The [CoroutineDispatcher] for IO tasks.
     */
    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Io)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides the default dispatcher for CPU-intensive work.
     * This dispatcher is used for tasks that don't require the UI thread or I/O operations, such as complex data processing.
     *
     * @return The [CoroutineDispatcher] for default tasks.
     */
    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Provides an unconfined dispatcher that is not confined to a specific thread.
     * This dispatcher can be used for situations where you don't need to constrain execution to a particular thread.
     *
     * @return The [CoroutineDispatcher] for unconfined tasks.
     */
    @Provides
    @ViewModelScoped
    @FilmVerseDispatchers(DispatcherType.Unconfined)
    fun provideUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}