package com.abdulkadirkara.filmverse.presentation.screens.screendetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.domain.model.CRUDResponseEntity
import com.abdulkadirkara.domain.model.FilmCardEntity
import com.abdulkadirkara.domain.model.FilmEntityModel
import com.abdulkadirkara.domain.usecase.DeleteFilmUseCase
import com.abdulkadirkara.domain.usecase.GetCartItemCountUseCase
import com.abdulkadirkara.domain.usecase.InsertFilmUseCase
import com.abdulkadirkara.domain.usecase.InsertMovieUseCase
import com.abdulkadirkara.domain.usecase.UpdateCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the data and logic related to the screen detail.
 * It interacts with the domain layer to insert, delete, and manage movie-related data
 * as well as manage cart item count and favorite status.
 *
 * @param insertMovieUseCase A use case for inserting a movie into the database.
 * @param getCartItemCountUseCase A use case for getting the current cart item count.
 * @param updateCartItemCountUseCase A use case for updating the cart item count.
 * @param insertFilmUseCase A use case for adding a film to the favorites.
 * @param deleteFilmUseCase A use case for removing a film from the favorites.
 */
@HiltViewModel
class ScreenDetailViewModel @Inject constructor(
    private val insertMovieUseCase: InsertMovieUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val updateCartItemCountUseCase: UpdateCartItemCountUseCase,
    private val insertFilmUseCase: InsertFilmUseCase,
    private val deleteFilmUseCase: DeleteFilmUseCase
) : ViewModel() {

    // LiveData for storing the result of inserting a movie card
    private val _insertMovieCardResult = MutableLiveData<NetworkResponse<CRUDResponseEntity>>()
    val insertMovieCardResult: MutableLiveData<NetworkResponse<CRUDResponseEntity>>
        get() = _insertMovieCardResult

    // StateFlow for storing the current number of items in the cart
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount

    // StateFlow for storing the current favorite status of a movie
    private val _favoriteStatus = MutableStateFlow(false)
    val favoriteStatus: StateFlow<Boolean> = _favoriteStatus

    init {
        getCartItemCount()
    }

    /**
     * Fetches the current number of items in the cart by calling the GetCartItemCountUseCase.
     * The result is then stored in the _cartItemCount StateFlow.
     */
    fun getCartItemCount() {
        viewModelScope.launch {
            getCartItemCountUseCase.execute().collect { count ->
                _cartItemCount.value = count
            }
        }
    }

    /**
     * Updates the cart item count with a new value by calling the UpdateCartItemCountUseCase.
     * It also updates the _cartItemCount StateFlow.
     *
     * @param newCount The new number of items to be updated in the cart.
     */
    fun updateCartItemCount(newCount: Int) {
        viewModelScope.launch {
            updateCartItemCountUseCase.execute(newCount)
            _cartItemCount.value = newCount
        }
    }

    /**
     * Inserts a new movie card into the system by calling the InsertMovieUseCase.
     * The result of the insertion is stored in the _insertMovieCardResult LiveData.
     *
     * @param name The name of the movie.
     * @param image The image URL of the movie.
     * @param price The price of the movie.
     * @param category The category of the movie.
     * @param rating The rating of the movie.
     * @param year The release year of the movie.
     * @param director The director of the movie.
     * @param description A brief description of the movie.
     * @param orderAmount The order quantity of the movie.
     */
    fun insertMovieCard(
        name: String,
        image: String,
        price: Int,
        category: String,
        rating: Double,
        year: Int,
        director: String,
        description: String,
        orderAmount: Int
    ) {
        viewModelScope.launch {
            insertMovieUseCase(
                name, image, price, category, rating, year, director, description, orderAmount
            ).collect {
                _insertMovieCardResult.value = it
            }
        }
    }

    /**
     * Toggles the favorite status of the specified movie. If the movie is currently marked as a favorite,
     * it is removed from the favorites. Otherwise, it is added to the favorites.
     *
     * @param movie The movie for which the favorite status is toggled.
     */
    fun toggleFavorite(movie: FilmCardEntity) {
        viewModelScope.launch {
            if (movie.isFavorite) {
                // Remove the movie from the favorites
                deleteFilmUseCase(
                    FilmEntityModel(
                        id = movie.id,
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
                _favoriteStatus.value = false
            } else {
                // Add the movie to the favorites
                insertFilmUseCase(
                    FilmEntityModel(
                        id = movie.id,
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
                _favoriteStatus.value = true
            }
        }
    }
}