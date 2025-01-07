package com.abdulkadirkara.filmverse.presentation.screens.screenhome

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmCategoryEntity
import com.abdulkadirkara.domain.model.FilmImageEntity
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
    private val _imageState = MutableLiveData<HomeUIState<List<FilmImageEntity>>>(HomeUIState.Loading)
    val imageState: LiveData<HomeUIState<List<FilmImageEntity>>> = _imageState

    private val _categoryState = MutableLiveData<HomeUIState<List<FilmCategoryEntity>>>(HomeUIState.Loading)
    val categoryState: LiveData<HomeUIState<List<FilmCategoryEntity>>> = _categoryState

    private val _filteredMovies = MutableLiveData<HomeUIState<List<FilmCardEntity>>>(HomeUIState.Loading)
    val filteredMovies: LiveData<HomeUIState<List<FilmCardEntity>>> = _filteredMovies
    private var allMovies: List<FilmCardEntity> = emptyList()

    private val _filterCount = mutableIntStateOf(0)
    val filterCount: State<Int> = _filterCount

    private val _selectedCategories = mutableStateOf<Set<String>>(emptySet())
    val selectedCategories: State<Set<String>> = _selectedCategories

    private val _selectedDirectors = mutableStateOf<Set<String>>(emptySet())
    val selectedDirectors: State<Set<String>> = _selectedDirectors

    private val _selectedRating = mutableFloatStateOf(0f)
    val selectedRating: State<Float> = _selectedRating

    private val _isFilterSelected = derivedStateOf {
        _selectedCategories.value.isNotEmpty() ||
                _selectedDirectors.value.isNotEmpty() ||
                _selectedRating.floatValue > 0f
    }
    val isFilterSelected: State<Boolean> = _isFilterSelected

    fun updateSelectedCategories(categories: Set<String>) {
        _selectedCategories.value = categories
        updateFilterCount()
    }

    fun updateSelectedDirectors(directors: Set<String>) {
        _selectedDirectors.value = directors
        updateFilterCount()
    }

    fun updateSelectedRating(rating: Float) {
        _selectedRating.floatValue = rating
        updateFilterCount()
    }

    private fun updateFilterCount() {
        val selectedFilterCount = listOf(
            _selectedCategories.value.size,
            _selectedDirectors.value.size,
            if (_selectedRating.floatValue > 0) 1 else 0
        ).sum()

        _filterCount.intValue = selectedFilterCount
    }

    init {
        getAllImages()
        getAllCategories()
        getAllMovies()
    }

    private fun getAllImages() {
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

    private fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { it ->
                it.onEmpty {
                    _categoryState.value = HomeUIState.Empty
                }.onLoading {
                    _categoryState.value = HomeUIState.Loading
                }.onSuccess { categories ->
                    val allCategories = listOf(
                        FilmCategoryEntity(category = "Tümü", isClicked = true)
                    ) + categories
                    _categoryState.value = HomeUIState.Success(allCategories)
                }.onError {
                    _categoryState.value = HomeUIState.Error(it.toString())
                }
            }
        }
    }


    private fun getAllMovies(){
        viewModelScope.launch {
            getAllMoviesUseCase().collect{ it ->
                it.onEmpty {
                    _filteredMovies.value = HomeUIState.Empty
                }.onLoading {
                    _filteredMovies.value = HomeUIState.Loading
                }.onSuccess {
                    _filteredMovies.value = HomeUIState.Success(it)
                    allMovies = it
                    applyFilters()
                }.onError {
                    _filteredMovies.value = HomeUIState.Error(it.toString())
                }
            }
        }
    }

    fun selectCategory(category: FilmCategoryEntity) {
        if (_categoryState.value is HomeUIState.Success) {
            val currentState = (_categoryState.value as HomeUIState.Success<List<FilmCategoryEntity>>)
            val updatedCategories = currentState.data.map {
                it.copy(isClicked = it == category)
            }
            _categoryState.value = HomeUIState.Success(updatedCategories)
        }
    }

    fun applyFilters() {
        _filteredMovies.value = HomeUIState.Loading
        val filteredList = allMovies.filter { movie ->
            (_selectedCategories.value.isEmpty() || movie.category in _selectedCategories.value) &&
                    (_selectedDirectors.value.isEmpty() || movie.director in _selectedDirectors.value) &&
                    movie.rating >= _selectedRating.floatValue
        }
        _filteredMovies.value = if (filteredList.isEmpty()) HomeUIState.Empty else HomeUIState.Success(filteredList)
    }

    fun clearFilters() {
        updateSelectedCategories(emptySet())
        updateSelectedDirectors(emptySet())
        updateSelectedRating(0f)
    }
}