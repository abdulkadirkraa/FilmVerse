package com.abdulkadirkara.data.di.coroutines

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class FilmVerseDispatchers(val type: DispatcherType) {
}

enum class DispatcherType{
    Main, Io, Default, Unconfined
}