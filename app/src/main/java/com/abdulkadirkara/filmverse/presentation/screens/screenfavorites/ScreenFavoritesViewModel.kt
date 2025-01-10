package com.abdulkadirkara.filmverse.presentation.screens.screenfavorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.usecase.DeleteFilmUseCase
import com.abdulkadirkara.domain.usecase.GetAllFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the logic of displaying and deleting favorite films.
 * It interacts with the domain layer to fetch films and handle deletion.
 *
 * @param getAllFilmsUseCase Use case for fetching all favorite films.
 * @param deleteFilmUseCase Use case for deleting a film from favorites.
 */
@HiltViewModel
class ScreenFavoritesViewModel @Inject constructor(
    private val getAllFilmsUseCase: GetAllFilmsUseCase,
    private val deleteFilmUseCase: DeleteFilmUseCase
) : ViewModel() {
    // StateFlow to hold the list of favorite films.
    private val _favoriteFilms = MutableStateFlow<List<FilmEntityModel>>(emptyList())
    val favoriteFilms: StateFlow<List<FilmEntityModel>> get() = _favoriteFilms

    // StateFlow to hold any error message related to loading or deleting films.
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        loadFavoriteFilms()
    }

    /**
     * Loads the list of favorite films by invoking the `GetAllFilmsUseCase`.
     * Updates the `_favoriteFilms` StateFlow with the fetched films.
     * If an error occurs during the fetching process, it updates the `_errorMessage` StateFlow.
     */
    fun loadFavoriteFilms() {
        viewModelScope.launch {
            getAllFilmsUseCase()
                // Handle any exceptions that might occur during the film fetch
                .catch {
                    _errorMessage.value = "Favori filmler yüklenirken hata oluştu: ${it.message}"
                }.collect { films ->
                    _favoriteFilms.value = films
                }
        }
    }

    /**
     * Deletes a film from the list of favorite films by invoking the `DeleteFilmUseCase`.
     * After deletion, the list of favorite films is reloaded.
     * If an error occurs during the deletion process, it updates the `_errorMessage` StateFlow.
     *
     * @param film The film that is to be deleted from the favorites list.
     */
    fun deleteFilm(film: FilmEntityModel) {
        viewModelScope.launch {
            try {
                deleteFilmUseCase(film)
                loadFavoriteFilms()
            } catch (e: Exception) {
                _errorMessage.value = "Film silinirken hata oluştu: ${e.message}"
            }
        }
    }
}