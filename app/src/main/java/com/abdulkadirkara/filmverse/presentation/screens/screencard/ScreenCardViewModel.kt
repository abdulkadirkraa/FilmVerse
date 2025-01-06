package com.abdulkadirkara.filmverse.presentation.screens.screencard

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
    private val _movieCardState = MutableLiveData<CardUIState<List<ScreenCardUIData>>>()
    val movieCardState: LiveData<CardUIState<List<ScreenCardUIData>>> = _movieCardState


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
                    val groupedList = cartList.groupBy { it.name }.map { entry ->
                        ScreenCardUIData(
                            cartId = entry.value.map { it.cartId },
                            name = entry.key,
                            image = entry.value.first().image,
                            category = entry.value.first().category,
                            price = entry.value.first().price,
                            orderAmount = entry.value.sumOf { it.orderAmount },
                            isChecked = true
                        )
                    }
                    _movieCardState.value = CardUIState.Success(groupedList)
                    _productCount.value = groupedList.sumOf { it.orderAmount }
                }
            }
        }
    }

    fun deleteMovieCard(cartIds: List<Int>, userName: String) {
        viewModelScope.launch {
            cartIds.forEach { cartId ->
                deleteMovieUseCase(cartId, userName).collect { response ->
                    response.onSuccess {
                        updateProductCount(cartId)
                    }
                }
            }
        }
    }

    fun deleteSelectedMovies(selectedStates: Map<Int, Boolean>) {
        val selectedCartIds = movieCardState.value?.let { state ->
            if (state is CardUIState.Success) {
                state.data
                    .filterIndexed { index, _ -> selectedStates[index] == true }
                    .map { it.cartId }
            } else {
                emptyList()
            }
        } ?: emptyList()

        selectedCartIds.forEach { cartId ->
            deleteMovieCard(cartId, ApiConstants.USER_NAME)
        }
    }

    private fun updateProductCount(cartId: Int) {
        val currentState = _movieCardState.value as? CardUIState.Success ?: return
        val updatedList = currentState.data.filterNot { it.cartId.contains(cartId) }
            .map { screenCard ->
                screenCard.copy(
                    cartId = screenCard.cartId.filterNot { it == cartId }
                )
            }
        _movieCardState.value = CardUIState.Success(updatedList)
        _productCount.value = updatedList.sumOf { it.orderAmount }
    }
}