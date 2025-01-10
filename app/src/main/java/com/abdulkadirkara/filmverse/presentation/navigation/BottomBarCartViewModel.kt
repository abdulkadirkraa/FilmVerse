package com.abdulkadirkara.filmverse.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulkadirkara.domain.usecase.GetCartItemCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for managing and observing the cart item count in the BottomBar.
 *
 * This ViewModel uses a reactive `StateFlow` to expose the number of items
 * in the cart. It leverages the `GetCartItemCountUseCase` to fetch the
 * current count and automatically updates whenever the cart content changes.
 *
 * @constructor Injects the required dependencies using Hilt.
 * @param getCartItemCountUseCase Use case to retrieve the current cart item count.
 *
 * @property cartItemCount A `StateFlow` that holds the current count of items in the cart.
 */
@HiltViewModel
class BottomBarCartViewModel @Inject constructor(
    getCartItemCountUseCase: GetCartItemCountUseCase
) : ViewModel() {

    /**
     * Exposes the current cart item count as a `StateFlow`.
     *
     * The flow is converted into a `StateFlow` with an initial value of 0.
     * The sharing policy is set to `WhileSubscribed`, which keeps the flow active
     * while there are active subscribers. It waits for 5000 ms before stopping
     * the flow after the last subscriber disconnects.
     */
    val cartItemCount: StateFlow<Int> = getCartItemCountUseCase.execute().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
}
