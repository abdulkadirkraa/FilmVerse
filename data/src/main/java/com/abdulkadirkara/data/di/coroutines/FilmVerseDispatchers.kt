package com.abdulkadirkara.data.di.coroutines

import javax.inject.Qualifier

/**
 * Custom qualifier annotation used to distinguish between different types of CoroutineDispatchers.
 * This allows the injection of specific dispatchers depending on the type of operation (Main, IO, Default, Unconfined).
 *
 * @param type The type of dispatcher that should be injected, specified using [DispatcherType].
 */
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class FilmVerseDispatchers(val type: DispatcherType)

enum class DispatcherType{
    Main, Io, Default, Unconfined
}