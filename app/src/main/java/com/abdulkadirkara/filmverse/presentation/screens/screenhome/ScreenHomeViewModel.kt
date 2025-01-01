package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import com.abdulkadirkara.domain.model.FilmCardUI
import com.abdulkadirkara.domain.model.FilmCategoryUI
import com.abdulkadirkara.domain.model.FilmImageUI
import com.abdulkadirkara.domain.usecase.GetAllCategoriesUseCase
import com.abdulkadirkara.domain.usecase.GetAllImagesUseCase
import com.abdulkadirkara.domain.usecase.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenHomeViewModel @Inject constructor(
    private val getAllImagesUseCase: GetAllImagesUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
) : ViewModel() {
    private val _imageState = MutableLiveData<HomeUIState<List<FilmImageUI>>>(HomeUIState.Loading)
    val imageState: LiveData<HomeUIState<List<FilmImageUI>>> = _imageState

    private val _categoryState = MutableLiveData<HomeUIState<List<FilmCategoryUI>>>(HomeUIState.Loading)
    val categoryState: LiveData<HomeUIState<List<FilmCategoryUI>>> = _categoryState
    private val _selectedCategory = MutableLiveData<FilmCategoryUI>()
    val selectedCategory: LiveData<FilmCategoryUI> = _selectedCategory

    private val _movieState = MutableLiveData<HomeUIState<List<FilmCardUI>>>(HomeUIState.Loading)
    val movieState: LiveData<HomeUIState<List<FilmCardUI>>> = _movieState

    init {
        getAllImages()
        getAllCategories()
        getAllMovies()
        _selectedCategory.value = FilmCategoryUI(isClicked = true, category = "Tümü")
    }

    fun getAllImages() {
        viewModelScope.launch {
            getAllImagesUseCase().collect { it ->
                it.onEmpty {
                    _imageState.value = HomeUIState.Empty
                }.onLoading {
                    _imageState.value = HomeUIState.Loading
                }.onSuccess {
                    _imageState.value = HomeUIState.Success(it)
                }.onError {
                    _imageState.value = HomeUIState.Error(it.toString())
                }
            }
        }
    }

    fun getAllCategories(){
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { it ->
                it.onEmpty {
                    _categoryState.value = HomeUIState.Empty
                }.onLoading {
                    _categoryState.value = HomeUIState.Loading
                }.onSuccess {
                    _categoryState.value = HomeUIState.Success(it)
                }.onError {
                    _categoryState.value = HomeUIState.Error(it.toString())
                }
            }
        }
    }

    fun getAllMovies(){
        viewModelScope.launch {
            getAllMoviesUseCase().collect{ it ->
                it.onEmpty {
                    _movieState.value = HomeUIState.Empty
                }.onLoading {
                    _movieState.value = HomeUIState.Loading
                }.onSuccess {
                    _movieState.value = HomeUIState.Success(it)
                }.onError {
                    _movieState.value = HomeUIState.Error(it.toString())
                }
            }
        }
    }

    fun selectCategory(category: FilmCategoryUI) {
        _categoryState.value = when (val currentState = _categoryState.value) {
            is HomeUIState.Success -> {
                val updatedCategories = currentState.data.map {
                    it.copy(isClicked = it == category)
                }
                HomeUIState.Success(updatedCategories)
            }
            else -> _categoryState.value
        }
        _selectedCategory.value = category
    }
}