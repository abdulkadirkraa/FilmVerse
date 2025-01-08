package com.abdulkadirkara.filmverse.presentation.screens.screenfavorites

import android.util.Log
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

@HiltViewModel
class ScreenFavoritesViewModel @Inject constructor(
    private val getAllFilmsUseCase: GetAllFilmsUseCase,
    private val deleteFilmUseCase: DeleteFilmUseCase
) : ViewModel() {

    private val _favoriteFilms = MutableStateFlow<List<FilmEntityModel>>(emptyList())
    val favoriteFilms: StateFlow<List<FilmEntityModel>> get() = _favoriteFilms
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        loadFavoriteFilms()
    }

    fun loadFavoriteFilms() {
        viewModelScope.launch {
            getAllFilmsUseCase()
                .catch {
                    _errorMessage.value = "Favori filmler yüklenirken hata oluştu: ${it.message}"
                }.collect { films ->
                _favoriteFilms.value = films
            }
        }
    }

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