package com.abdulkadirkara.domain.repository

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItemCount(): Flow<Int>
    suspend fun updateCartItemCount(newCount: Int)
}