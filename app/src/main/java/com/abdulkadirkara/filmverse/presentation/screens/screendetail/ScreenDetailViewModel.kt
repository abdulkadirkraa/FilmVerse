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

@HiltViewModel
class ScreenDetailViewModel @Inject constructor(
    private val insertMovieUseCase: InsertMovieUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val updateCartItemCountUseCase: UpdateCartItemCountUseCase,
    private val insertFilmUseCase: InsertFilmUseCase,
    private val deleteFilmUseCase: DeleteFilmUseCase
) : ViewModel() {

    private val _insertMovieCardResult = MutableLiveData<NetworkResponse<CRUDResponseEntity>>()
    val insertMovieCardResult: MutableLiveData<NetworkResponse<CRUDResponseEntity>>
        get() = _insertMovieCardResult

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount
    private val _favoriteStatus = MutableStateFlow(false)
    val favoriteStatus: StateFlow<Boolean> = _favoriteStatus

    init {
        getCartItemCount()
    }

    fun getCartItemCount() {
        viewModelScope.launch {
            getCartItemCountUseCase.execute().collect { count ->
                _cartItemCount.value = count
            }
        }
    }

    fun updateCartItemCount(newCount: Int) {
        viewModelScope.launch {
            updateCartItemCountUseCase.execute(newCount)
            _cartItemCount.value = newCount
        }
    }

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
    ){
        viewModelScope.launch {
            insertMovieUseCase(
                name, image, price, category, rating, year, director, description, orderAmount
            ).collect{
                _insertMovieCardResult.value = it
            }
        }
    }

    fun toggleFavorite(movie: FilmCardEntity) {
        viewModelScope.launch {
            if (movie.isFavorite) {
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