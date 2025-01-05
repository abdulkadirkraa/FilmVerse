package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemCountUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(newCount: Int) {
        cartRepository.updateCartItemCount(newCount)
    }
}