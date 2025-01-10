package com.abdulkadirkara.filmverse.presentation.screens.screenhome

/**
 * Represents the UI state for the Home screen in a strongly typed and sealed manner.
 *
 * @param T The type of data that the Success state will hold.
 */
sealed class HomeUIState<out T> {

    /**
     * Represents a loading state where data is being fetched or processed.
     */
    data object Loading : HomeUIState<Nothing>()

    /**
     * Represents a successful state where data is available.
     *
     * @param data The data fetched or processed successfully.
     */
    data class Success<T>(val data: T) : HomeUIState<T>()

    /**
     * Represents an error state where something went wrong.
     *
     * @param message A message describing the error.
     */
    data class Error(val message: String) : HomeUIState<Nothing>()

    /**
     * Represents an empty state where no data is available.
     */
    data object Empty : HomeUIState<Nothing>()
}
