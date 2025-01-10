package com.abdulkadirkara.filmverse

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The [FilmVerseApp] class represents the application entry point.
 * It is annotated with [@HiltAndroidApp] to trigger Hilt's code generation
 * and setup dependency injection in the app.
 *
 * By annotating the application class with [@HiltAndroidApp], Hilt will
 * generate the necessary components and handle dependency injection for
 * Android components such as Activities, Fragments, and ViewModels.
 *
 * The [FilmVerseApp] class extends [Application] and is the first component
 * initialized when the app starts, setting up the Hilt framework to inject
 * dependencies across the app's lifecycle.
 */
@HiltAndroidApp
class FilmVerseApp : Application()