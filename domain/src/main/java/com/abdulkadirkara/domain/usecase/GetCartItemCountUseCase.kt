package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the count of items in the cart.
 *
 * @param cartRepository The repository responsible for fetching cart information.
 */
class GetCartItemCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    /**
     * Retrieves the current number of items in the cart.
     *
     * @return A [Flow] emitting the number of items in the cart as an integer.
     */
    fun execute(): Flow<Int> = cartRepository.getCartItemCount()
}