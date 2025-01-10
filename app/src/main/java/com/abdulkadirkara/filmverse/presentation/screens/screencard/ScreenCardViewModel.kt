package com.abdulkadirkara.filmverse.presentation.screens.screencard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.common.networkResponse.onEmpty
import com.abdulkadirkara.common.networkResponse.onError
import com.abdulkadirkara.common.networkResponse.onLoading
import com.abdulkadirkara.common.networkResponse.onSuccess
import com.abdulkadirkara.domain.usecase.DeleteMovieUseCase
import com.abdulkadirkara.domain.usecase.GetMovieCartUseCase
import com.abdulkadirkara.domain.usecase.UpdateCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for managing and processing movie cart data.
 * This class interacts with use cases to retrieve, update, and delete items from the movie cart.
 * It handles UI state changes based on the results from the use cases and provides live data
 * to update the UI accordingly.
 *
 * @param getMovieCartUseCase The use case to fetch the current movie cart data.
 * @param deleteMovieUseCase The use case to delete a movie from the cart.
 * @param updateCartItemCountUseCase The use case to update the count of a specific item in the cart.
 */
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
        getMovieCard()
    }

    /**
     * Updates the count of a specific item in the cart.
     *
     * @param newCount The new count to be set for the item.
     */
    fun updateCartItemCount(newCount: Int) {
        viewModelScope.launch {
            updateCartItemCountUseCase.execute(newCount)
        }
    }

    /**
     * Fetches the movie cart data and groups items by movie name.
     * Updates the movie card state with the grouped data and the total product count.
     * Handles different UI states such as loading, success, error, and empty.
     */
    private fun getMovieCard() {
        viewModelScope.launch {
            getMovieCartUseCase().collect { it ->
                // Handle different network response states
                it.onError {
                    _movieCardState.value = CardUIState.Error(it.toString())
                }.onEmpty {
                    _movieCardState.value = CardUIState.Empty
                    _productCount.value = 0
                }.onLoading {
                    _movieCardState.value = CardUIState.Loading
                }.onSuccess { cartList ->
                    // On success, group the cart items by movie name and calculate order amounts
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
                    // Update movie card state and product count
                    _movieCardState.value = CardUIState.Success(groupedList)
                    _productCount.value = groupedList.sumOf { it.orderAmount }
                }
            }
        }
    }

    /**
     * Deletes a specific movie from the cart based on its cart ID.
     *
     * @param cartIds A list of cart IDs for the items to be deleted.
     */
    fun deleteMovieCard(cartIds: List<Int>) {
        viewModelScope.launch {
            // For each cart ID(api cardids), invoke delete operation
            cartIds.forEach { cartId ->
                deleteMovieUseCase(cartId).collect { response ->
                    response.onSuccess {
                        updateProductCount(cartId)
                    }
                }
            }
        }
    }

    /**
     * Deletes selected movies from the cart based on the selected states.
     * This method is typically used when confirming the cart items to be removed.
     *
     * @param selectedStates A map of item indices and their selection state (true/false) indicating
     *                       which items are selected for deletion.
     */
    fun deleteSelectedMovies(selectedStates: Map<Int, Boolean>) {
        // Filter the selected items based on the selectedStates
        val selectedCartIds = movieCardState.value?.let { state ->
            if (state is CardUIState.Success) {
                // Collect the cart IDs of the selected items for deletion
                state.data
                    .filterIndexed { index, _ -> selectedStates[index] == true }
                    .map { it.cartId }
            } else {
                emptyList()
            }
        } ?: emptyList()

        // Delete the selected movies by their cart IDs
        selectedCartIds.forEach { cartId ->
            deleteMovieCard(cartId)
        }
    }

    /**
     * Updates the product count and the state of the movie card list after an item is deleted.
     *
     * @param cartId The cart ID of the deleted movie.
     */
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