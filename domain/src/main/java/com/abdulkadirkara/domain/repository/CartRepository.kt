package com.abdulkadirkara.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing cart-related data.
 *
 * This interface provides methods to retrieve and update the cart item count.
 */
interface CartRepository {

    /**
     * Retrieves the current count of items in the cart as a reactive data stream.
     *
     * @return A [Flow] emitting the current cart item count.
     */
    fun getCartItemCount(): Flow<Int>

    /**
     * Updates the cart item count with a new value.
     *
     * @param newCount The new cart item count to be set.
     */
    suspend fun updateCartItemCount(newCount: Int)
}
