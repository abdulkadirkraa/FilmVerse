package com.abdulkadirkara.domain.usecase

import com.abdulkadirkara.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemCountUseCase @Inject constructor(private val cartRepository: CartRepository) {

    fun execute(): Flow<Int> = cartRepository.getCartItemCount()
}