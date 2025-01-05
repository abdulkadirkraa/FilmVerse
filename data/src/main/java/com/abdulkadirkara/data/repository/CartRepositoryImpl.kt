package com.abdulkadirkara.data.repository

import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.local.datastore.CartDataStore
import com.abdulkadirkara.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDataStore: CartDataStore,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : CartRepository {

    override fun getCartItemCount(): Flow<Int> {
        return cartDataStore.cartItemCountFlow
    }

    override suspend fun updateCartItemCount(newCount: Int) {
        withContext(ioDispatcher) {
            cartDataStore.updateCartItemCount(newCount)
        }
    }
}