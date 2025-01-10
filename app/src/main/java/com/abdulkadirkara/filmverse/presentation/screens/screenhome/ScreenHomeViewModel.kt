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
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.model.FilmImageEntity
import com.abdulkadirkara.domain.usecase.DeleteFilmUseCase
import com.abdulkadirkara.domain.usecase.GetAllCategoriesUseCase
import com.abdulkadirkara.domain.usecase.GetAllFilmsUseCase
import com.abdulkadirkara.domain.usecase.GetAllImagesUseCase
import com.abdulkadirkara.domain.usecase.GetAllMoviesUseCase
import com.abdulkadirkara.domain.usecase.InsertFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen of the FilmVerse application.
 *
 * This ViewModel manages the UI state for displaying film images, categories, and movies.
 * It provides functionality for filtering and favoriting movies, and utilizes Hilt for
 * dependency injection, adhering to the MVVM architecture.
 *
 * @property getAllImagesUseCase Use case to fetch all film images.
 * @property getAllCategoriesUseCase Use case to fetch all film categories.
 * @property getAllMoviesUseCase Use case to fetch all movies.
 * @property getAllFilmsUseCase Use case to fetch all films (favorites).
 * @property insertFilmUseCase Use case to insert a film into favorites.
 * @property deleteFilmUseCase Use case to delete a film from favorites.
 */
@HiltViewModel
class ScreenHomeViewModel @Inject constructor(
    private val getAllImagesUseCase: GetAllImagesUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val getAllFilmsUseCase: GetAllFilmsUseCase,
    private val insertFilmUseCase: InsertFilmUseCase,
    private val deleteFilmUseCase: DeleteFilmUseCase
) : ViewModel() {

    // LiveData to hold the state of images displayed on the Home screen.
    // This state can be Loading, Empty, Success with a list of FilmImageEntity, or Error.
    private val _imageState =
        MutableLiveData<HomeUIState<List<FilmImageEntity>>>(HomeUIState.Loading)
    // Public LiveData to observe the image state from the UI layer.
    val imageState: LiveData<HomeUIState<List<FilmImageEntity>>> = _imageState
    private val _categoryState =
        MutableLiveData<HomeUIState<List<FilmCategoryEntity>>>(HomeUIState.Loading)
    val categoryState: LiveData<HomeUIState<List<FilmCategoryEntity>>> = _categoryState
    private val _filteredMovies =
        MutableLiveData<HomeUIState<List<FilmCardEntity>>>(HomeUIState.Loading)
    val filteredMovies: LiveData<HomeUIState<List<FilmCardEntity>>> = _filteredMovies

    // List to store all movies for filtering purposes
    private var allMovies: List<FilmCardEntity> = emptyList()

    // List to store favorite movies
    private var favoriteMovies: List<FilmEntityModel> = emptyList()

    // State to hold the count of selected filters, which will be displayed as a badge of filter icon
    private val _filterCount = mutableIntStateOf(0)
    val filterCount: State<Int> = _filterCount

    // State to hold selected items, which will be used for filtering
    private val _selectedCategories = mutableStateOf<Set<String>>(emptySet())
    val selectedCategories: State<Set<String>> = _selectedCategories
    private val _selectedDirectors = mutableStateOf<Set<String>>(emptySet())
    val selectedDirectors: State<Set<String>> = _selectedDirectors
    private val _selectedRating = mutableFloatStateOf(0f)
    val selectedRating: State<Float> = _selectedRating

    // Derived state to check if any filter is selected, which can be used to enable/disable UI components
    private val _isFilterSelected = derivedStateOf {
        // Checks if any filter criteria (category, director, or rating) is selected
        _selectedCategories.value.isNotEmpty() ||
        _selectedDirectors.value.isNotEmpty() ||
        _selectedRating.floatValue > 0f
    }
    val isFilterSelected: State<Boolean> = _isFilterSelected

    init {
        fetchAllData()
    }

    /**
     * Fetches all necessary data (images, categories, movies) for the Home screen.
     */
    private fun fetchAllData() {
        getAllImages()
        getAllCategories()
        getAllMovies()
    }

    /**
     * Updates the selected categories, directors, or rating and recalculates the total count of selected filters.
     *
     * These functions allow the user to update their filter preferences (categories, directors, and rating).
     * Each time a selection is made, the filter count is recalculated to reflect the number of active filters.
     * The updated filter count will be used for displaying the number of active filters in the UI (e.g., a badge).
     *
     * @param categories A set of selected categories for filtering the content.
     * @param directors A set of selected directors for filtering the content.
     * @param rating A float value representing the selected rating filter.
     */
    fun updateSelectedCategories(categories: Set<String>) {
        // Update the selected categories and recalculate the filter count
        _selectedCategories.value = categories
        updateFilterCount()
    }

    fun updateSelectedDirectors(directors: Set<String>) {
        // Update the selected directors and recalculate the filter count
        _selectedDirectors.value = directors
        updateFilterCount()
    }

    fun updateSelectedRating(rating: Float) {
        // Update the selected rating and recalculate the filter count
        _selectedRating.floatValue = rating
        updateFilterCount()
    }

    /**
     * Recalculates the total count of selected filters by checking the number of selected categories, directors,
     * and whether a rating filter has been applied.
     *
     * This function sums the number of selected categories and directors. It also checks if a rating has been selected
     * (greater than 0) and includes it in the count.
     *
     * The result is stored in the `_filterCount` state, which can be used to display the active filter count in the UI.
     */
    private fun updateFilterCount() {
        val selectedFilterCount = listOf(
            _selectedCategories.value.size,
            _selectedDirectors.value.size,
            if (_selectedRating.floatValue > 0) 1 else 0
        ).sum()

        _filterCount.intValue = selectedFilterCount
    }

    /**
     * Fetches all images for the Home screen and updates the UI state accordingly.
     * <p>
     * Collects data from the `GetAllImagesUseCase` and handles different states:
     * - Empty: When no images are available.
     * - Loading: While the data is being fetched.
     * - Success: When the data is successfully fetched.
     * - Error: If an error occurs during fetching.
     */
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
                    _imageState.value = HomeUIState.Error(it.message)
                }
            }
        }
    }

    /**
     * Fetches all categories for the Home screen and updates the UI state.
     * <p>
     * Collects data from the `GetAllCategoriesUseCase` and handles different states:
     * - Empty: When no categories are available.
     * - Loading: While the data is being fetched.
     * - Success: Adds a "Tümü" category for all movies, then updates the state.
     * - Error: If an error occurs during fetching.
     */
    private fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { it ->
                it.onEmpty {
                    _categoryState.value = HomeUIState.Empty
                }.onLoading {
                    _categoryState.value = HomeUIState.Loading
                }.onSuccess { categories ->
                    val allCategories =
                        listOf(FilmCategoryEntity(category = "Tümü", isClicked = true)) + categories
                    _categoryState.value = HomeUIState.Success(allCategories)
                }.onError {
                    _categoryState.value = HomeUIState.Error(it.message)
                }
            }
        }
    }

    /**
     * Fetches all movies from the `GetAllMoviesUseCase` and updates the filtered movie list.
     * <p>
     * This function handles different states of the movie data retrieval process:
     * - **Empty**: No movies are available.
     * - **Loading**: Data is being fetched.
     * - **Success**: The movie list is updated with all available movies, and any applied filters are applied.
     * - **Error**: If an error occurs during fetching, an error message is displayed.
     * <p>
     * Additionally, this function triggers the fetching of favorite movies and applies any active filters once the movies are successfully retrieved.
     */
    private fun getAllMovies() {
        viewModelScope.launch {
            getAllMoviesUseCase().collect { it ->
                it.onEmpty {
                    _filteredMovies.value = HomeUIState.Empty
                }.onLoading {
                    _filteredMovies.value = HomeUIState.Loading
                }.onSuccess {
                    _filteredMovies.value = HomeUIState.Success(it)
                    allMovies = it
                    fetchFavoritesAndUpdateMovies() // Fetch favorites after getting all movies
                    applyFilters() // Apply any active filters to the movie list
                }.onError {
                    _filteredMovies.value = HomeUIState.Error(it.message)
                }
            }
        }
    }

    /**
     * Fetches the list of favorite movies and updates the movie list to reflect favorite status.
     * <p>
     * Collects data from the `GetAllFilmsUseCase` and updates the UI state or handles errors.
     */
    private fun fetchFavoritesAndUpdateMovies() {
        viewModelScope.launch {
            try {
                getAllFilmsUseCase().collect { films ->
                    if (films.isEmpty()) {
                        _filteredMovies.value = HomeUIState.Empty
                    } else {
                        favoriteMovies = films
                        updateMoviesWithFavorites() // Update the main movie list with favorite statuses
                    }
                }
            } catch (e: Exception) {
                _filteredMovies.value =
                    HomeUIState.Error("Favori filmleri yüklerken bir hata oluştu: ${e.message}")
            }
        }
    }

    /**
     * Updates the movie list with favorite status based on the current favorite movies.
     * <p>
     * Handles UI state changes for empty, success, or error states.
     */
    fun updateMoviesWithFavorites() {
        try {
            val updatedMovies = allMovies.map { movie ->
                movie.copy(isFavorite = favoriteMovies.any { it.name == movie.name })
            }
            if (updatedMovies.isEmpty()) {
                _filteredMovies.value = HomeUIState.Empty
            } else {
                _filteredMovies.value = HomeUIState.Success(updatedMovies)
            }
        } catch (e: Exception) {
            _filteredMovies.value =
                HomeUIState.Error("Filmler güncellenirken bir hata oluştu: ${e.message}")
        }
    }

    /**
     * Toggles the favorite status of a movie.
     * <p>
     * Adds the movie to the favorites if not already favorited; otherwise, removes it.
     * <p>
     * Uses the `InsertFilmUseCase` and `DeleteFilmUseCase` for database operations.
     *
     * @param movie The movie whose favorite status is to be toggled.
     */
    fun toggleFavorite(movie: FilmCardEntity) {
        viewModelScope.launch {
            if (movie.isFavorite) {
                deleteFilmUseCase(
                    FilmEntityModel(
                        id = 0,
                        category = movie.category,
                        description = movie.description,
                        director = movie.director,
                        imagePath = movie.image,
                        name = movie.name,
                        rating = movie.rating,
                        price = movie.price,
                        year = movie.year
                    )
                )
            } else {
                insertFilmUseCase(
                    FilmEntityModel(
                        id = 0,
                        category = movie.category,
                        description = movie.description,
                        director = movie.director,
                        imagePath = movie.image,
                        name = movie.name,
                        rating = movie.rating,
                        price = movie.price,
                        year = movie.year
                    )
                )
            }
            fetchFavoritesAndUpdateMovies()
        }
    }

    /**
     * Selects a category and updates the state to mark the selected category.
     * <p>
     * Updates the UI to reflect the currently selected category.
     *
     * @param category The category to be selected.
     */
    fun selectCategory(category: FilmCategoryEntity) {
        if (_categoryState.value is HomeUIState.Success) {
            val currentState =
                (_categoryState.value as HomeUIState.Success<List<FilmCategoryEntity>>)
            val updatedCategories = currentState.data.map {
                it.copy(isClicked = it == category)
            }
            _categoryState.value = HomeUIState.Success(updatedCategories)
        }
    }

    /**
     * Applies the selected filters to the movie list.
     * <p>
     * Filters movies by categories, directors, and rating, and updates the filtered movie list.
     */
    fun applyFilters() {
        _filteredMovies.value = HomeUIState.Loading
        val filteredList = allMovies.filter { movie ->
            (_selectedCategories.value.isEmpty() || movie.category in _selectedCategories.value) &&
                    (_selectedDirectors.value.isEmpty() || movie.director in _selectedDirectors.value) &&
                    movie.rating >= _selectedRating.floatValue
        }
        _filteredMovies.value =
            if (filteredList.isEmpty()) HomeUIState.Empty else HomeUIState.Success(filteredList)
    }

    /**
     * Clears all selected filters.
     * <p>
     * Resets the selected categories, directors, and rating filters.
     */
    fun clearFilters() {
        updateSelectedCategories(emptySet())
        updateSelectedDirectors(emptySet())
        updateSelectedRating(0f)
    }
}