package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.repository.CartRepository
import javax.inject.Inject

/**
 * Use case for updating the cart item count in the repository.
 *
 * @param cartRepository The repository responsible for managing the cart.
 */
class UpdateCartItemCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    /**
     * Updates the cart item count.
     *
     * @param newCount The new count of items in the cart.
     */
    suspend fun execute(newCount: Int) {
        cartRepository.updateCartItemCount(newCount)
    }
}