package com.abdulkadirkara.data.repository

import com.abdulkadirkara.data.di.coroutines.DispatcherType
import com.abdulkadirkara.data.di.coroutines.FilmVerseDispatchers
import com.abdulkadirkara.data.local.datastore.CartDataStore
import com.abdulkadirkara.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [CartRepository] interface for managing cart-related data.
 *
 * This class interacts with a [CartDataStore] to retrieve and update cart item count.
 * It uses a [CoroutineDispatcher] for performing IO operations.
 *
 * @property cartDataStore The data store for managing cart data.
 * @property ioDispatcher The [CoroutineDispatcher] for performing IO-bound operations.
 */
class CartRepositoryImpl @Inject constructor(
    private val cartDataStore: CartDataStore,
    @FilmVerseDispatchers(DispatcherType.Io) private val ioDispatcher: CoroutineDispatcher
) : CartRepository {

    /**
     * Retrieves the current count of items in the cart as a reactive data stream.
     *
     * @return A [Flow] emitting the current cart item count.
     */
    override fun getCartItemCount(): Flow<Int> {
        return cartDataStore.cartItemCountFlow
    }

    /**
     * Updates the cart item count with a new value.
     *
     * @param newCount The new cart item count to be set.
     */
    override suspend fun updateCartItemCount(newCount: Int) {
        withContext(ioDispatcher) {
            cartDataStore.updateCartItemCount(newCount)
        }
    }
}
