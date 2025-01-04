package com.abdulkadirkara.filmverse.presentation.screens.screencard

sealed class CardUIState<out T> {
    data object Loading : CardUIState<Nothing>()
    data class Success<T>(val data: T) : CardUIState<T>()
    data class Error(val message: String) : CardUIState<Nothing>()
    data object Empty : CardUIState<Nothing>()
}