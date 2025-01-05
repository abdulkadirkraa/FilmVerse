package com.abdulkadirkara.filmverse.presentation.screens.screencard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.NetworkResponse
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import com.abdulkadirkara.data.remote.ApiConstants
import com.abdulkadirkara.domain.model.CRUDResponseUI
import com.abdulkadirkara.domain.model.FilmCardItem
import com.abdulkadirkara.domain.usecase.DeleteMovieUseCase
import com.abdulkadirkara.domain.usecase.GetMovieCartUseCase
import com.abdulkadirkara.domain.usecase.UpdateCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenCardViewModel @Inject constructor(
    private val getMovieCartUseCase: GetMovieCartUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val updateCartItemCountUseCase: UpdateCartItemCountUseCase
) : ViewModel() {
    private val _movieCardState = MutableLiveData<CardUIState<List<FilmCardItem>>>()
    val movieCardState: LiveData<CardUIState<List<FilmCardItem>>> = _movieCardState

    private val _deleteMovieCardResult = MutableLiveData<NetworkResponse<CRUDResponseUI>>()
    val deleteMovieCardResult: MutableLiveData<NetworkResponse<CRUDResponseUI>>
        get() = _deleteMovieCardResult

    private val _productCount = MutableLiveData<Int>()
    val productCount: LiveData<Int> get() = _productCount

    init {
        getMovieCard(ApiConstants.USER_NAME)
    }

    fun updateCartItemCount(newCount: Int) {
        viewModelScope.launch {
            updateCartItemCountUseCase.execute(newCount)
        }
    }

    private fun getMovieCard(userName: String) {
        viewModelScope.launch {
            getMovieCartUseCase(userName = userName).collect { it ->
                it.onError {
                    _movieCardState.value = CardUIState.Error(it.toString())
                }.onEmpty {
                    _movieCardState.value = CardUIState.Empty
                    _productCount.value = 0
                }.onLoading {
                    _movieCardState.value = CardUIState.Loading
                }.onSuccess { cartList ->
                    _movieCardState.value = CardUIState.Success(cartList)
                    _productCount.value = cartList.sumOf { it.orderAmount }
                }
            }
        }
    }

    fun deleteMovieCard(cartId: Int, userName: String, orderAmount: Int) {
        viewModelScope.launch {
            deleteMovieUseCase(cartId, userName).collect { response ->
                response.onSuccess {
                    updateProductCount(cartId)
                    updateCartItemCountUseCase.execute(-orderAmount)
                }
                _deleteMovieCardResult.value = response
            }
        }
    }

    private fun updateProductCount(cartId: Int) {
        val currentState = _movieCardState.value
        if (currentState is CardUIState.Success) {
            val updatedList = currentState.data.filterNot { it.cartId == cartId }
            _movieCardState.value = CardUIState.Success(updatedList)
            _productCount.value = updatedList.sumOf { it.orderAmount }
        }
    }

}